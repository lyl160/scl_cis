/**
 * 
 */
package cn.dofuntech.core.cache.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.dofuntech.core.cache.CacheManager;

/**
 * @author lxu
 *
 */
public class LocalCacheManagerImpl implements CacheManager {
	
	private static ConcurrentMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();

	/* (non-Javadoc)
	 * @see cn.dofuntech.core.cache.CacheManager#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		return map.get(key);
	}

	/* (non-Javadoc)
	 * @see cn.dofuntech.core.cache.CacheManager#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void set(String key, T value) {
		map.put(key, value);
	}

	/* (non-Javadoc)
	 * @see cn.dofuntech.core.cache.CacheManager#replace(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void replace(String key, T value) {
		map.put(key, value);
	}

	/* (non-Javadoc)
	 * @see cn.dofuntech.core.cache.CacheManager#delete(java.lang.String)
	 */
	@Override
	public void delete(String key) {
		map.remove(key);
	}

}
