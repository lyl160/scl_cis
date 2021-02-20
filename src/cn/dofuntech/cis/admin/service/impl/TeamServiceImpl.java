package cn.dofuntech.cis.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.TeamMapper;
import cn.dofuntech.cis.admin.service.TeamService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.Team;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:TeamServiceImpl.java 
 */
@Service
public class TeamServiceImpl extends DunfengServiceImpl<Team> implements TeamService {

    @Resource
    private TeamMapper teamMapper;

}