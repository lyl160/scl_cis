/**
 * 
 */
package cn.dofuntech.core.service;

import java.util.List;
import java.util.Map;

import cn.dofuntech.core.page.Paginator;

/**
 * Single Entity<T> CRUD Service
 * 
 * @author lxu
 *
 */
public abstract interface BaseService<T> {

    /**
     * 获得实体
     * 
     * @param id
     * @return
     */
    public T get(Long id);

    /**
     * 获得实体
     * 
     * @param map
     * @return
     */
    public T get(Map<String, Object> map);

    /**
     * 插叙
     * 
     * @return
     */
    public List<T> query();

    /**
     * 
     * @param page
     * @return
     */
    public List<T> query(Paginator page);

    /**
     * 
     * @param map
     * @return
     */
    public List<T> query(Map<String, Object> map);

    /**
     * 
     * @param map
     * @param page
     * @return
     */
    public List<T> query(Map<String, Object> map, Paginator page);

    /**
     * 
     * @param entity
     */
    public void insert(T entity);

    /**
     * 
     * @param entity
     */
    public void update(T entity);

    /**
     * Only For AutoIdEntity
     * 
     * @param entity
     */
    public void save(T entity);

    /**
     * 
     * @param entity
     */
    public void delete(T entity);
}
