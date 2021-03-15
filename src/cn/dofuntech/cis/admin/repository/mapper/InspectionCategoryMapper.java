package cn.dofuntech.cis.admin.repository.mapper;

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
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
 * filename:InspectionCategoryMapper.java 
 */

@Repository
public interface InspectionCategoryMapper extends BaseMapper<InspectionCategory> {

    List<InspectionCategory> queryByParam(Map<String, Object> map);

}