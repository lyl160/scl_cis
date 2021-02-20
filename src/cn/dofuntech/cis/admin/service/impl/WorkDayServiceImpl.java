package cn.dofuntech.cis.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dofuntech.cis.admin.repository.domain.WorkDay;
import cn.dofuntech.cis.admin.repository.mapper.WorkDayMapper;
import cn.dofuntech.cis.admin.service.WorkDayService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.dfauth.service.UserService;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:WorkDayServiceImpl.java 
 */
@Service
public class WorkDayServiceImpl extends DunfengServiceImpl<WorkDay> implements WorkDayService {

    @Resource
    private WorkDayMapper workDayMapper;
    @Autowired
    UserService userService;


}