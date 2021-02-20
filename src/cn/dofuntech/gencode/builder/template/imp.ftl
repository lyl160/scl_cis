package ${servicePackage}.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import ${daoPackage}.${className_d}Mapper;
import ${servicePackage}.${className_d}Service;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import ${pojoPackage}.${className_d};

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:${className_d}ServiceImpl.java 
 */
@Service
public class ${className_d}ServiceImpl extends DunfengServiceImpl<${className_d}> implements ${className_d}Service {

    @Resource
    private ${className_d}Mapper ${className_u}Mapper;

}