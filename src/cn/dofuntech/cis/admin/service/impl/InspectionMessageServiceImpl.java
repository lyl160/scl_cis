package cn.dofuntech.cis.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.InspectionMessageMapper;
import cn.dofuntech.cis.admin.service.InspectionMessageService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:InspectionMessageServiceImpl.java 
 */
@Service
public class InspectionMessageServiceImpl extends DunfengServiceImpl<InspectionMessage> implements InspectionMessageService {

    @Resource
    private InspectionMessageMapper inspectionMessageMapper;

	@Override
	public List<InspectionMessage> queryAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return inspectionMessageMapper.queryAll(map);
	}

}