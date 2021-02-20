/**
 * 
 */
package cn.dofuntech.core.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

/**
 * 用于分页查询的PreparedStatementCallback,会移动游标(cursor)至offset,并根据limit参数抽取数据，
 * 执行查询前会设置PreparedStatement.setMaxRows(limit); <br>
 * <b>不可用于dialect截取后的Limit SQL中</b>
 * <br>
 * <b>使用：</b>
 * <code>
 * (List) getJdbcTemplate().execute(sql, new OffsetLimitPreparedStatementCallback(offset,limit,rowMapper));
 * </code>
 * 
 * @author lxu
 *
 */
public class OffsetLimitPreparedStatementCallback<T> implements PreparedStatementCallback<List<T>> {

	private int offset;
	private int limit;
	private RowMapper<T> rowMapper;
	
	public OffsetLimitPreparedStatementCallback(int offset, int limit, RowMapper<T> rowMapper) {
		Assert.notNull(rowMapper, "'rowMapper' must be not null");
		this.offset = offset;
		this.limit = limit;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<T> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
		ps.setMaxRows(limit);
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			OffsetLimitResultSetExtractor<T> rse = new OffsetLimitResultSetExtractor<T>(offset,limit,rowMapper);
			return rse.extractData(rs);
		} finally {
			JdbcUtils.closeResultSet(rs);
		}
	}

}
