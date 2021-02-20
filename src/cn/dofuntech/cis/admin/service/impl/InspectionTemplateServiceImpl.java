package cn.dofuntech.cis.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.InspectionTemplateMapper;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:InspectionTemplateServiceImpl.java 
 */
@Service
public class InspectionTemplateServiceImpl extends DunfengServiceImpl<InspectionTemplate> implements InspectionTemplateService {

    @Resource
    private InspectionTemplateMapper inspectionTemplateMapper;

}