package cn.dofuntech.cis.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dofuntech.cis.admin.repository.domain.TeacherClazzRelInf;
import cn.dofuntech.cis.admin.repository.mapper.TeacherClazzRelInfMapper;
import cn.dofuntech.cis.admin.service.TeacherClazzRelInfService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:ClazzInfServiceImpl.java 
 */
@Service
public class TeacherClazzRelInfServiceImpl extends DunfengServiceImpl<TeacherClazzRelInf> implements TeacherClazzRelInfService {

    @Resource
    private TeacherClazzRelInfMapper teacherClazzRelInfMapper;

}