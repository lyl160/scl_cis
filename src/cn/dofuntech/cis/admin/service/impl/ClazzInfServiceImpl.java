package cn.dofuntech.cis.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.ClazzInfMapper;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.ClazzInf;

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
public class ClazzInfServiceImpl extends DunfengServiceImpl<ClazzInf> implements ClazzInfService {

    @Resource
    private ClazzInfMapper clazzInfMapper;

}