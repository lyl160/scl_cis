package cn.dofuntech.cis.admin.service;

import java.util.List;
import java.util.Map;

import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.core.service.DunfengService;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:InspectionLogsService.java 
 */
public interface InspectionLogsService extends DunfengService<InspectionLogs> {
	
  List<InspectionLogs> querydate(Map<String,Object> map);
  
  List<InspectionLogs> queryOnedate(Map<String,Object> map);
  
  List<InspectionLogs> queryOnedateAll(Map<String,Object> map);


    void deleteTodayLogsByClazzId(InspectionLogs clazzId);

    void deleteTodayLogsByTeamId(InspectionLogs teamId);
}