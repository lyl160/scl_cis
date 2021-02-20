/**
 * 
 */
package cn.dofuntech.core.cache.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.dofuntech.core.cache.CacheManager;

/**
 * @author lxu
 *
 */
public class EhcacheManagerImpl implements CacheManager, InitializingBean {
	
	private Cache ehcache;

	/**
	 * @param ehcache the ehcache to set
	 */
	public void setEhcache(Cache ehcache) {
		this.ehcache = ehcache;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ehcache, "ehcache bean should not be null in ehcache manager!");
	}

	/* (non-Javadoc)
	 * @see com.letv.core.cache.CacheManager#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Element element = ehcache.get(key);
		if (element != null) {
			ehcache.replace(element);
			return element.getObjectValue();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.letv.core.cache.CacheManager#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void set(String key, T value) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		Element element = new Element(key, value);
		ehcache.put(element);
	}

	/* (non-Javadoc)
	 * @see com.letv.core.cache.CacheManager#replace(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void replace(String key, T value) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		Element element = ehcache.get(key);
		if (element != null) {
			ehcache.replace(element);
		}
	}

	/* (non-Javadoc)
	 * @see com.letv.core.cache.CacheManager#delete(java.lang.String)
	 */
	@Override
	public void delete(String key) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		ehcache.remove(key);
	}

}
