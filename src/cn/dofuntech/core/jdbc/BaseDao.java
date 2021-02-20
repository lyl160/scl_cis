/**
 * 
 */
package cn.dofuntech.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.dofuntech.core.jdbc.dialect.Dialect;
import cn.dofuntech.core.page.PageBounds;
import cn.dofuntech.core.page.Paginator;

/**
 * Dao基类
 * 
 * @author lxu
 *
 */
public abstract class BaseDao<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "dialect")
    protected Dialect      dialect;

    @Resource(name = "jdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 翻页查询
     * 
     * @param sql 普通的select语句，不要带有任何的翻页参数
     * @param args
     * @param rowMapper
     * @param paginator
     * @return
     */
    protected List<T> query(String sql, Object[] args, RowMapper<T> rowMapper, Paginator paginator) {
        return query(sql, args, rowMapper, PageBounds.wrap(paginator));
    }

    /**
     * 翻页查询
     * 
     * @param sql 普通的select语句，不要带有任何的翻页参数
     * @param args
     * @param rowMapper
     * @param pageBounds
     * @return
     */
    protected List<T> query(String sql, Object[] args, RowMapper<T> rowMapper, PageBounds pageBounds) {
        pageBounds.setTotalCount(getJdbcTemplate().queryForObject(dialect.getCountString(sql), args, Integer.class));
        return jdbcTemplate.query(dialect.getLimitString(sql, pageBounds.getOffset(), pageBounds.getLimit()), args, rowMapper);
    }

    /**
     * 翻页查询
     * 
     * @param sql 普通的select语句，不要带有任何的翻页参数
     * @param rowMapper
     * @param paginator
     * @param args
     * @return
     */
    protected List<T> query(String sql, RowMapper<T> rowMapper, Paginator paginator, Object... args) {
        return query(sql, args, rowMapper, paginator);
    }

    /**
     * 翻页查询
     * 
     * @param sql 普通的select语句，不要带有任何的翻页参数
     * @param rowMapper
     * @param pageBounds
     * @param args
     * @return
     */
    protected List<T> query(String sql, RowMapper<T> rowMapper, PageBounds pageBounds, Object... args) {
        return query(sql, args, rowMapper, pageBounds);
    }

    /** 自动生成主键的字段名，,默认是"id" */
    private String[] generatedColumns = { "id" };

    /**
     * @param generatedColumns the generatedColumns to set
     */
    protected void setGeneratedColumns(String... generatedColumns) {
        this.generatedColumns = generatedColumns;
    }

    /**
     * 新增数据库，返回主键
     * 
     * @param sql
     * @param args
     * @return 主键，新增失败则返回null
     */
    protected Number insert(final String sql, final Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = this.getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                // 指定返回主键的字段名
                PreparedStatement ps = con.prepareStatement(sql, generatedColumns);

                for (int i = 0 ; i < args.length ; i++) {
                    if (args[i] instanceof SqlParameterValue) {
                        SqlParameterValue paramValue = (SqlParameterValue) args[i];
                        StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue, paramValue.getValue());
                    }
                    else {
                        StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, args[i]);
                    }
                }

                return ps;
            }
        }, keyHolder);

        if (result == 0) {
            return null;
        }
        if (result > 1) {
            throw new IncorrectResultSizeDataAccessException(1, result);
        }
        return keyHolder.getKey();
    }

    /**
     * 新增数据库，返回主键
     * 
     * @param sql
     * @param pss
     * @return 主键，新增失败则返回null
     */
    protected Number insert(final String sql, final PreparedStatementSetter pss) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, generatedColumns);
                if (pss != null) {
                    pss.setValues(ps);
                }
                return ps;
            }

        }, keyHolder);

        if (result == 0) {
            return null;
        }
        if (result > 1) {
            throw new IncorrectResultSizeDataAccessException(1, result);
        }
        return keyHolder.getKey();
    }

    /**
     * 新增数据库，返回主键
     * 
     * @param psc
     * @return 主键，新增失败则返回null
     */
    protected Number insert(PreparedStatementCreator psc) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.getJdbcTemplate().update(psc, keyHolder);

        if (result == 0) {
            return null;
        }
        if (result > 1) {
            throw new IncorrectResultSizeDataAccessException(1, result);
        }
        return keyHolder.getKey();
    }
}
