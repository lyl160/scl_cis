/**
 * 
 */
package cn.dofuntech.core.jdbc.namedparam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.jdbc.dialect.Dialect;
import cn.dofuntech.core.page.PageBounds;
import cn.dofuntech.core.page.Paginator;

/**
 * 基于NamedParameter的Dao基类。
 * 可以和Entity很好的结合，推荐使用
 * 
 * @author lxu
 *
 */
public abstract class NamedParameterBaseDao<T> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String GENERATED_KEY = "GENERATED_KEY";
	
	@Resource(name = "dialect")
	protected Dialect dialect;
	
	@Resource(name = "namedParameterJdbcTemplate")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * @return the jdbcTemplate
	 */
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	/**
	 * 翻页查询
	 * 
	 * @param sql 普通的select语句，不要带有任何的翻页参数
	 * @param paramMap
	 * @param rowMapper
	 * @param paginator
	 * @return
	 */
	protected List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper, Paginator paginator) {
		return query(sql, paramMap, rowMapper, PageBounds.wrap(paginator));
	}
	
	/**
	 * 翻页查询
	 * 
	 * @param sql 普通的select语句，不要带有任何的翻页参数
	 * @param paramSource
	 * @param rowMapper
	 * @param paginator
	 * @return
	 */
	protected List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper, Paginator paginator) {
		return query(sql, paramSource, rowMapper, PageBounds.wrap(paginator));
	}
	
	/**
	 * 翻页查询
	 * 
	 * @param sql 普通的select语句，不要带有任何的翻页参数
	 * @param paramMap
	 * @param rowMapper
	 * @param pageBounds
	 * @return
	 */
	protected List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper, PageBounds pageBounds) {
		return query(sql, new MapSqlParameterSource(paramMap), rowMapper, pageBounds);
	}
	
	/**
	 * 翻页查询
	 * 
	 * @param sql 普通的select语句，不要带有任何的翻页参数
	 * @param paramSource
	 * @param rowMapper
	 * @param pageBounds
	 * @return
	 */
	protected List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper, PageBounds pageBounds) {
		pageBounds.setTotalCount(getJdbcTemplate().queryForObject(dialect.getCountString(sql), paramSource, Integer.class));
		return getJdbcTemplate().query(dialect.getLimitString(sql, pageBounds.getOffset(), pageBounds.getLimit()), paramSource, rowMapper);
	}
	
	/**
	 * 新增数据库，如果是自增长主键，则返写主键到<code>paramMap</code>中的"GENERATED_KEY"中
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	protected int insert(String sql, Map<String, Object> paramMap, String... generatedColumns) {
		if (generatedColumns == null || generatedColumns.length == 0) {
			return getJdbcTemplate().update(sql, paramMap);
		} else {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int result = getJdbcTemplate().update(sql, new MapSqlParameterSource(paramMap), keyHolder, generatedColumns);
			paramMap.put(GENERATED_KEY, keyHolder.getKey());
			return result;
		}
	}
	
	/**
	 * 新增数据库，返写主键
	 * 
	 * @param sql
	 * @param entity
	 * @return
	 */
	protected int insert(String sql, AutoIdEntity entity, String... generatedColumns) {
		if (generatedColumns == null || generatedColumns.length == 0) {
			generatedColumns = new String[]{"id"};
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result = getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity), keyHolder, generatedColumns);
		entity.setId(keyHolder.getKey().longValue());
		return result;
	}
	
	/**
	 * 修改数据库（包括非自增长主键实体的新增）
	 * 
	 * @param sql
	 * @param entity
	 * @return
	 */
	protected int update(String sql, Object entity) {
		return getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	/**
	 * 删除数据库
	 * 
	 * @param sql
	 * @param key
	 * @param id
	 * @return
	 */
	protected int delete(String sql, String key, Serializable id) {
		Map<String, Serializable> paramMap = new HashMap<String, Serializable>();
		paramMap.put(key, id);
		return getJdbcTemplate().update(sql, paramMap);
	}
	
	/**
	 * 删除数据库
	 * 
	 * @param sql
	 * @param entity
	 * @return
	 */
	protected int delete(String sql, Object entity) {
		return getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
}
