/**
 * 
 */
package cn.dofuntech.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.core.page.PageBounds;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.service.BaseService;

/**
 * Service的CRUD基类
 * 
 * @author lxu
 *
 */
public abstract class BaseServcieImpl<T> implements BaseService<T>  {

    protected Logger        logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected BaseMapper<T> mapper;

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#get(java.lang.Long)
     */
    @Override
    public T get(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return get(map);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#get(java.lang.Long)
     */
    @Override
    public T get(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return mapper.get(map);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#query()
     */
    @Override
    public List<T> query() {
        return mapper.query();
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#query(cn.dofuntech.core.page.Paginator)
     */
    @Override
    public List<T> query(Paginator page) {
        return mapper.query(PageBounds.wrap(page));
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#query(java.util.Map)
     */
    @Override
    public List<T> query(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return query();
        }
        return mapper.queryByMap(map);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#query(java.util.Map, cn.dofuntech.core.page.Paginator)
     */
    @Override
    public List<T> query(Map<String, Object> map, Paginator page) {
        if (map == null || map.isEmpty()) {
            return query(page);
        }
        return mapper.queryByMap(map, PageBounds.wrap(page));
    }
    
    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#query(java.util.Map, cn.dofuntech.core.page.Paginator)
     */
    public List<T> queryWX(Map<String, Object> map, Paginator page) {
        if (map == null || map.isEmpty()) {
            return query(page);
        }
        return mapper.queryByMap(map, PageBounds.wrap(page));
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#insert(java.lang.Object)
     */
    @Override
    public void insert(T entity) {
        if (entity == null) {
            return;
        }
        mapper.insert(entity);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#update(java.lang.Object)
     */
    @Override
    public void update(T entity) {
        if (entity == null) {
            return;
        }
        mapper.update(entity);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#save(java.lang.Object)
     */
    @Override
    public void save(T entity) {
        if (!(entity instanceof AutoIdEntity)) {
            return;
        }
        AutoIdEntity baseEntity = (AutoIdEntity) entity;
        if (baseEntity.getId() == null || baseEntity.getId() == 0) {
            insert(entity);
        }
        else {
            update(entity);
        }
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.service.BaseService#delete(java.lang.Long)
     */
    @Override
    public void delete(T entity) {
        if (entity == null) {
            return;
        }
        mapper.delete(entity);
    }
    
    

}
