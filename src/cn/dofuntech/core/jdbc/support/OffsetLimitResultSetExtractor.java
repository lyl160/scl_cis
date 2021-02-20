/**
 * 
 */
package cn.dofuntech.core.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/**
 * 用于分页查询的ResultSetExtractor, 会移动游标(cursor)至offset, 并根据limit参数抽取数据。<br>
 * <b>不可用于dialect截取后的Limit SQL中</b>
 * <br>
 * <b>使用：</b>
 * <code>
 * (List) getJdbcTemplate().query(sql, args, new OffsetLimitResultSetExtractor(offset,limit,rowMapper))
 * </code>
 *
 * @author lxu
 * 
 */
public class OffsetLimitResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

	private int offset;
	private int limit;
	private RowMapper<T> rowMapper;
	
	public OffsetLimitResultSetExtractor(int offset, int limit, RowMapper<T> rowMapper) {
		Assert.notNull(rowMapper,"'rowMapper' must be not null");
		this.offset = offset;
		this.limit = limit;
		this.rowMapper = rowMapper;
	}

	@Override
	public List<T> extractData(ResultSet rs) throws SQLException,	DataAccessException {
		List<T> results = new ArrayList<T>(limit);
		if (offset > 0) {
			rs.absolute(offset);
		}
		
		int rowNum = 0;
		while (rs.next()) {
			results.add(this.rowMapper.mapRow(rs, rowNum++));
			if ((rowNum + 1) >= limit) {
				break;
			}
		}
		return results;
	}

}
