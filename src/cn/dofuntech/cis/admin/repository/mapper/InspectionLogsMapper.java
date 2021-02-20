package cn.dofuntech.cis.admin.repository.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:InspectionLogsMapper.java 
 */

@Repository
public interface InspectionLogsMapper extends BaseMapper<InspectionLogs> {
	
	
   public List<InspectionLogs>	querydate(Map<String,Object> map);
   
   
   public List<InspectionLogs>  queryOnedate(Map<String,Object> map);
   
   public List<InspectionLogs>  queryOnedateAll(Map<String,Object> map);

}