package cn.dofuntech.cis.admin.service;

import java.util.List;
import java.util.Map;

import cn.dofuntech.core.service.DunfengService;
import cn.dofuntech.cis.admin.repository.domain.Schedule;
import cn.dofuntech.dfauth.bean.UserInf;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:ScheduleService.java 
 */
public interface ScheduleService extends DunfengService<Schedule> {
	/**查询排班老师
	 * @return
	 */
	String getTeachers(Map<String, Object> param);

    List<UserInf> getTeachersBySchedule(Schedule schedule) throws Exception;
}