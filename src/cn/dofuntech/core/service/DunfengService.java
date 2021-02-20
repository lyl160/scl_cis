package cn.dofuntech.core.service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu(@2015年10月28日)
 * @version 1.0
 * filename:HaoldService.java 
 */
public interface DunfengService<T> extends BaseService<T> {

    /**
     * 获得实体
     * 
     * @param id
     * @return
     */
    public T getByProp(String key, Object value);

    /**
     * 获得实体
     * 
     * @param id
     * @return
     */
    public List<T> queryByProp(String key, Object value);

    /**
     * 批量删除
     * @param ids
     */
    public void deleteBatch(List<T> entitys);


}
