package cn.dofuntech.cis.admin.service;

import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.repository.domain.vo.TeachersClockInfoVo;
import cn.dofuntech.cis.admin.repository.domain.vo.TeachersDutyVo;
import cn.dofuntech.core.service.DunfengService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@ @ 2016年2月2日)
 * @version 1.0
 * filename:InspectionMessageService.java 
 */
public interface InspectionMessageService extends DunfengService<InspectionMessage> {


	public List<InspectionMessage> queryAll(Map<String, Object> map);

	List<TeachersDutyVo> queryTeacherDutyList(Map<String, Object> map);

	List<TeachersClockInfoVo> queryTeacherClockInfoList(Map<String, Object> map);

}