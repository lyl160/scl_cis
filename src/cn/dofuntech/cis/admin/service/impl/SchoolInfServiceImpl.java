package cn.dofuntech.cis.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.cis.admin.repository.mapper.SchoolInfMapper;
import cn.dofuntech.cis.admin.service.SchoolInfService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.dfauth.bean.RoleInf;
import cn.dofuntech.dfauth.util.UAI;
import cn.dofuntech.cis.admin.repository.domain.SchoolInf;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:SchoolInfServiceImpl.java 
 */
@Service
public class SchoolInfServiceImpl extends DunfengServiceImpl<SchoolInf> implements SchoolInfService {

    @Resource
    private SchoolInfMapper schoolInfMapper;

}