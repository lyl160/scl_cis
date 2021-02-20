package cn.dofuntech.cis.admin.service;

import cn.dofuntech.core.service.DunfengService;

import java.util.List;
import java.util.Map;

import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:InspectionMessageService.java 
 */
public interface InspectionMessageService extends DunfengService<InspectionMessage> {
	
	
	public List<InspectionMessage> queryAll(Map<String, Object> map);
	
	
	

}