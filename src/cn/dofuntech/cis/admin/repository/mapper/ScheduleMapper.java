package cn.dofuntech.cis.admin.repository.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;
import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.cis.admin.repository.domain.Schedule;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:ScheduleMapper.java 
 */

@Repository
public interface ScheduleMapper extends BaseMapper<Schedule> {
	/**查询排班老师
	 * @return
	 */
	String getTeachers(Map<String, Object> param);
}