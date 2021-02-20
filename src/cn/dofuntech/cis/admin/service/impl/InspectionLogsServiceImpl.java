package cn.dofuntech.cis.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.repository.mapper.InspectionLogsMapper;
import cn.dofuntech.cis.admin.service.InspectionLogsService;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.core.entity.DefaultValue;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;

/**
 * <p>
 * <p>
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 *
 * @author lxu(@ 2016年2月2日)
 * @version 1.0
 * filename:InspectionLogsServiceImpl.java
 */
@Service
public class InspectionLogsServiceImpl extends DunfengServiceImpl<InspectionLogs> implements InspectionLogsService {

    @Resource
    private InspectionLogsMapper inspectionLogsMapper;
    @Resource
    private InspectionResultService inspectionResultService;

    @Override
    public List<InspectionLogs> querydate(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return inspectionLogsMapper.querydate(map);
    }

    @Override
    public List<InspectionLogs> queryOnedate(Map<String, Object> map) {
        // TODO Auto-generated method stub
        if (map == null || map.isEmpty()) {
            return null;
        }
        return inspectionLogsMapper.queryOnedate(map);
    }

    @Override
    public List<InspectionLogs> queryOnedateAll(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return inspectionLogsMapper.queryOnedateAll(map);
    }

    @Override
    public void deleteTodayLogsByClazzId(InspectionLogs logs) {
        if (logs != null && !logs.getClazzId().equals(DefaultValue.LONG_EMPTY)) {
            Map logsParam = new HashMap();
            logsParam.put("templateId", logs.getTemplateId());
            logsParam.put("schoolId", logs.getSchoolId());
            logsParam.put("category2", logs.getCategory2());
            logsParam.put("category3", logs.getCategory3());
            logsParam.put("clazzId", logs.getClazzId());
            logsParam.put("addTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            List<InspectionLogs> logsList = this.query(logsParam);
            if (logsList!=null && logsList.size() > 0) {
                for (InspectionLogs log : logsList) {
                    Map resultparam = new HashMap();
                    resultparam.put("logsId", log.getId());
                    List<InspectionResult> resultList = inspectionResultService.query(resultparam);
                    if (resultList != null && resultList.size() > 0) {
                        inspectionResultService.deleteBatch(resultList);
                    }
                    this.delete(log);
                }
            }
        }
    }

    @Override
    public void deleteTodayLogsByTeamId(InspectionLogs logs) {
        if (logs != null && !logs.getTeamId().equals(DefaultValue.LONG_EMPTY)) {
            Map logsParam = new HashMap();
            logsParam.put("templateId", logs.getTemplateId());
            logsParam.put("schoolId", logs.getSchoolId());
            logsParam.put("category2", logs.getCategory2());
            logsParam.put("category3", logs.getCategory3());
            logsParam.put("teamId", logs.getTeamId());
            logsParam.put("addTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            List<InspectionLogs> logsList = this.query(logsParam);
            if (logsList!=null && logsList.size() > 0) {
                for (InspectionLogs log : logsList) {
                    Map resultparam = new HashMap();
                    resultparam.put("logsId", log.getId());
                    List<InspectionResult> resultList = inspectionResultService.query(resultparam);
                    if (resultList != null && resultList.size() > 0) {
                        inspectionResultService.deleteBatch(resultList);
                    }
                    this.delete(log);
                }
            }
        }
    }


}