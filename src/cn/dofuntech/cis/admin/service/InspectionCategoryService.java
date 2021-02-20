package cn.dofuntech.cis.admin.service;

import java.util.Date;

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.core.service.DunfengService;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:InspectionCategoryService.java 
 */
public interface InspectionCategoryService extends DunfengService<InspectionCategory> {

    boolean inTime(InspectionCategory category, Date date);

    void resetStartAndEndTime(InspectionCategory category, Date date);
}