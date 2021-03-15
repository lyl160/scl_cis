package cn.dofuntech.cis.admin.service.impl;

import cn.dofuntech.cis.admin.repository.domain.Schedule;
import cn.dofuntech.cis.admin.repository.mapper.ScheduleMapper;
import cn.dofuntech.cis.admin.service.ScheduleService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:ScheduleServiceImpl.java 
 */
@Service
public class ScheduleServiceImpl extends DunfengServiceImpl<Schedule> implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Autowired
    UserService userService;

	@Override
	public String getTeachers(Map<String, Object> param) {
		return scheduleMapper.getTeachers(param);
	}

    @Override
    public List<UserInf> getTeachersBySchedule(Schedule schedule) throws Exception {
        List<UserInf> userinfoList = new ArrayList<>();
        String userString = schedule.getUsers();
        if (StringUtils.isNotEmpty(userString)) {
            String[] userArr = userString.split(",");
            for (String userId : userArr) {
                UserInf userInfo = new UserInf();
                userInfo.setId(Integer.parseInt(userId));
                userInfo = userService.getEntity(userInfo);
                userinfoList.add(userInfo);
            }
        }

        return userinfoList;
    }

    @Override
    public List<Schedule> getScheduleListByParam(Map<String, Object> paramMap) {
        return scheduleMapper.queryByMap(paramMap);
    }

}