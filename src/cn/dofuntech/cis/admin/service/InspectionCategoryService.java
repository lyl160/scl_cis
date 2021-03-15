package cn.dofuntech.cis.admin.service;

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.core.service.DunfengService;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@ @ 2016年2月2日)
 * @version 1.0
 * filename:InspectionCategoryService.java 
 */
public interface InspectionCategoryService extends DunfengService<InspectionCategory> {

    boolean inTime(InspectionCategory category, Date date);

    void resetStartAndEndTime(InspectionCategory category, Date date);

    List<InspectionCategory> queryByParam(Map<String, Object> map);

    Long getTemplateIdByType(String schoolId, String type);
}