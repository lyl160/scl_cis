package cn.dofuntech.cis.admin.repository.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:InspectionResultMapper.java 
 */

@Repository
public interface InspectionResultMapper extends BaseMapper<InspectionResult> {
	 List<InspectionResult> queryCPR(Map<String,Object> map);
	 List<InspectionResult> queryNum(Map<String,Object> map);
	 List<InspectionResult> querySum(Map<String,Object> map);
}