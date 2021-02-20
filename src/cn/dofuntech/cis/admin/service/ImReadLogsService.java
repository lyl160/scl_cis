package cn.dofuntech.cis.admin.service;

import cn.dofuntech.core.service.DunfengService;

import java.util.List;
import java.util.Map;

import cn.dofuntech.cis.admin.repository.domain.ImReadLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:ImReadLogsService.java 
 */
public interface ImReadLogsService extends DunfengService<ImReadLogs> {
	
	
	public List<ImReadLogs> queryAll(Map<String, Object> map);
	
	
	public void updateOne(ImReadLogs ir);
	

}