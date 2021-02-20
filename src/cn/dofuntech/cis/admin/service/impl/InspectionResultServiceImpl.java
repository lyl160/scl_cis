package cn.dofuntech.cis.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.InspectionResultMapper;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:InspectionResultServiceImpl.java 
 */
@Service
public class InspectionResultServiceImpl extends DunfengServiceImpl<InspectionResult> implements InspectionResultService {

    @Resource
    private InspectionResultMapper inspectionResultMapper;

	@Override
	public List<InspectionResult> queryCPR(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return inspectionResultMapper.queryCPR(map);
	}

	@Override
	public List<InspectionResult> queryNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return inspectionResultMapper.queryNum(map);
	}

	@Override
	public List<InspectionResult> querySum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return inspectionResultMapper.querySum(map);
	}

}