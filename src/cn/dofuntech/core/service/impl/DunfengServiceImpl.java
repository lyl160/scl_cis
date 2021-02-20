package cn.dofuntech.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import cn.dofuntech.core.service.DunfengService;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu(@2015年10月28日)
 * @version 1.0
 * filename:DunfengServiceImpl.java 
 */
public class DunfengServiceImpl<T> extends BaseServcieImpl<T> implements DunfengService<T> {

    /* (non-Javadoc)
     * @see com.hld.base.HaoldService#getByProp(java.lang.String, java.lang.Object)
     */
    @Override
    public T getByProp(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return mapper.get(map);
    }

    /* (non-Javadoc)
     * @see com.hld.base.HaoldService#queryByProp(java.lang.String, java.lang.Object)
     */
    @Override
    public List<T> queryByProp(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return mapper.queryByMap(map);
    }

    /**
     * 根据ID抓取对应对象组装成List
     * @param ids
     * @param mapping
     * @return
     */
    protected <K> List<K> fetchFromMap(String ids, Map<String, K> mapping) {
        List<K> crowList = new ArrayList<K>();
        for (String id : StringUtils.split(ids, ",")) {
            if (mapping.containsKey(id)) {
                crowList.add(mapping.get(id));
            }
        }
        return crowList;
    }

    /* (non-Javadoc)
     * @see cn.puredee.base.PuredeeService#deleteBatch(java.util.List)
     */
    @Override
    public void deleteBatch(List<T> entitys) {
        if (CollectionUtils.isNotEmpty(entitys)) {
            for (T entity : entitys) {
                mapper.delete(entity);
            }
        }
    }
}
