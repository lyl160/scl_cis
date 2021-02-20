/**
 * 
 */
package cn.dofuntech.core.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.core.jdbc.dialect.Dialect;
import cn.dofuntech.core.page.PageBounds;

/**
 * 为MyBatis提供基于方言(Dialect)的分页查询的插件
 * <p>将拦截Executor.query()方法实现分页方言的插入.</p>
 * 
 * @author lxu
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class OffsetLimitInterceptor implements Interceptor {

    private final Logger logger                 = LoggerFactory.getLogger(this.getClass());

    static int           MAPPED_STATEMENT_INDEX = 0;
    static int           PARAMETER_INDEX        = 1;
    static int           ROWBOUNDS_INDEX        = 2;
    static int           RESULT_HANDLER_INDEX   = 3;

    Dialect              dialect;

    /* (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();

        //get arguments
        final Object[] queryArgs = invocation.getArgs();

        // if there are no rowBounds, then return
        RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        if (!dialect.supportsLimit() || (rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET && rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT)) {
            return invocation.proceed();
        }

        //------start to prepare parameters--------
        MappedStatement mappedStatement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        ResultHandler resultHandler = (ResultHandler) queryArgs[RESULT_HANDLER_INDEX];

        PageBounds pageBounds;
        if (rowBounds instanceof PageBounds) {
            pageBounds = (PageBounds) rowBounds;
        }
        else {
            return invocation.proceed();
        }

        int offset = pageBounds.getOffset();
        int limit = pageBounds.getLimit();

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String originalSql = boundSql.getSql().trim();

        // rewrite the SQL statement with limit string through dialect
        String limitSql = "";
        if (dialect.supportsLimitOffset()) {
            limitSql = dialect.getLimitString(originalSql, offset, limit);
        }
        else {
            limitSql = dialect.getLimitString(originalSql, 0, limit);
        }

        final BoundSql limitBoundSql = copyFromBoundSql(mappedStatement, boundSql, limitSql);
        MappedStatement limitMappedStatement = copyFromMappedStatement(mappedStatement, mappedStatement.getResultMaps(), new SqlSource() {

            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return limitBoundSql;
            }

        });
        RowBounds limitRowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        //set back the modified arguments
        queryArgs[MAPPED_STATEMENT_INDEX] = limitMappedStatement;
        queryArgs[ROWBOUNDS_INDEX] = limitRowBounds;

        // query total count of this sql statement
        String countSql = dialect.getCountString(originalSql);
        final BoundSql countBoundSql = copyFromBoundSql(mappedStatement, boundSql, countSql);

        // form resultMap using mybatis builders. SQL must is [select count(1) from tables ...]. Or use getTotalCount(originalSql, ..) instead of executor.query(countMappedStatement, ..)
        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(mappedStatement.getConfiguration(), "totalCount", "count(1)", Integer.class);
        List<ResultMapping> resultMappings = Arrays.asList(resultMappingBuilder.build());
        ResultMap.Builder resultMapBuilder = new ResultMap.Builder(mappedStatement.getConfiguration(), "totalCount", Integer.class, resultMappings);
        List<ResultMap> resultMaps = Arrays.asList(resultMapBuilder.build());
        MappedStatement countMappedStatement = copyFromMappedStatement(mappedStatement, resultMaps, new SqlSource() {

            @Override
            public BoundSql getBoundSql(Object parameterObject) {
                return countBoundSql;
            }

        });

        // use mybatis cache
        Integer totalCount = null;
        Cache cache = mappedStatement.getCache();
        if (cache != null && mappedStatement.isUseCache()) {
            CacheKey cacheKey = executor.createCacheKey(mappedStatement, parameter, limitRowBounds, countBoundSql);
            totalCount = (Integer) cache.getObject(cacheKey);
            if (totalCount == null) {
                totalCount = (Integer) executor.query(countMappedStatement, parameter, limitRowBounds, resultHandler).get(0);
                cache.putObject(cacheKey, totalCount);
            }
        }
        else {
            totalCount = (Integer) executor.query(countMappedStatement, parameter, limitRowBounds, resultHandler).get(0);
        }
        pageBounds.setTotalCount(totalCount == null ? 0 : totalCount.intValue());

        return invocation.proceed();
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /* (non-Javadoc)
     * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
     */
    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");

        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }

        logger.debug(this.getClass().getSimpleName() + ".dialect=" + dialectClass);
    }

    /**
     *
     * @param mappedStatement
     * @param boundSql
     * @param sql
     * @return
     */
    private BoundSql copyFromBoundSql(MappedStatement mappedStatement, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * @param mappedStatement
     * @param resultMaps
     * @param sqlSource
     * @return
     * @see org.apache.ibatis.builder.MapperBuilderAssistant
     */
    private MappedStatement copyFromMappedStatement(MappedStatement mappedStatement, List<ResultMap> resultMaps, SqlSource sqlSource) {

        MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());

        //set new resultMap
        builder.resultMaps(resultMaps);

        //copy all those attributes from old MappedStatement to new MappedStatement
        builder.resource(mappedStatement.getResource());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.timeout(mappedStatement.getTimeout());
        builder.statementType(mappedStatement.getStatementType());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        builder.keyProperty(limitedArraytoString(mappedStatement.getKeyProperties()));
        builder.keyColumn(limitedArraytoString(mappedStatement.getKeyColumns()));
        builder.databaseId(mappedStatement.getDatabaseId());

        return builder.build();
    }

    private static String limitedArraytoString(String in[]) {
        StringBuffer sb = new StringBuffer();
        if (in == null || in.length == 0) {
            return null;
        }
        else {
            for (String s : in) {
                sb.append(s).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 直接执行count语句，以防Dialect中的getCountString被修改成非count(1)。不推荐使用！
     *
     * @param sql
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unused")
    private int getTotalCount(final String sql, final MappedStatement mappedStatement, final Object parameterObject, final BoundSql boundSql) throws SQLException {
        final String countSql = dialect.getCountString(sql);
        logger.debug("Total count SQL [{}] ", countSql);
        logger.debug("Total count Parameters: {} ", parameterObject);

        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            countStmt = connection.prepareStatement(countSql);
            // Page SQL和Count SQL的参数是一样的，在绑定参数时可以使用一样的boundSql
            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            handler.setParameters(countStmt);

            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            logger.debug("Total count: {}", count);
            return count;
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            }
            finally {
                try {
                    if (countStmt != null) {
                        countStmt.close();
                    }
                }
                finally {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }
            }
        }
    }
}
