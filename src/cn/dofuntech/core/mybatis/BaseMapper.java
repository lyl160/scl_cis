/**
 * 
 */
package cn.dofuntech.core.mybatis;

import java.util.List;
import java.util.Map;

import cn.dofuntech.core.page.PageBounds;

/**
 * Mapper 基类
 * 
 * @author lxu
 *
 */
public abstract interface BaseMapper<T> {

    /**
     * 获得实体
     * 
     * @param map
     * @return
     */
    public T get(Map<String, Object> map);

    /**
     * 
     * @return
     */
    public List<T> query();

    /**
     * 
     * @param pageBounds
     * @return
     */
    public List<T> query(PageBounds pageBounds);

    /**
     * 
     * @param map
     * @return
     */
    public List<T> queryByMap(Map<String, Object> map);

    /**
     * 
     * @param map
     * @param pageBounds
     * @return
     */
    public List<T> queryByMap(Map<String, Object> map, PageBounds pageBounds);

    /**
     * 新增实体
     * 
     * @param entity
     */
    public void insert(T entity);

    /**
     * 修改实体
     * 
     * @param entity
     */
    public void update(T entity);

    /**
     * 删除实体
     * 
     * @param id
     */
    public void delete(T entity);

	
}
