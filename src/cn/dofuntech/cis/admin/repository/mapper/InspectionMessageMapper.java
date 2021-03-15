package cn.dofuntech.cis.admin.repository.mapper;

import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.repository.domain.vo.TeachersClockInfoVo;
import cn.dofuntech.cis.admin.repository.domain.vo.TeachersDutyVo;
import cn.dofuntech.core.mybatis.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@ 2016年2月2日)
 * @version 1.0
 * filename:InspectionMessageMapper.java 
 */

@Repository
public interface InspectionMessageMapper extends BaseMapper<InspectionMessage> {

	public List<InspectionMessage> queryAll(Map<String, Object> map);

	List<TeachersDutyVo> queryTeacherDutyList(Map<String, Object> map);

	List<TeachersClockInfoVo> queryTeacherClockInfoList(Map<String, Object> map);

}