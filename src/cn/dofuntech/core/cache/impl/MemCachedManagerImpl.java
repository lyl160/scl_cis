/**
 * 
 */
package cn.dofuntech.core.cache.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.danga.MemCached.MemCachedClient;
import cn.dofuntech.core.cache.CacheManager;

/**
 * @author lxu
 *
 */
public class MemCachedManagerImpl implements CacheManager, InitializingBean {

    private static final int ONE_MINUTE        = 1000 * 60;

    @Autowired
    private MemCachedClient  memCachedClient;

    private int              sessionExpireMins = 60;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(memCachedClient, "memCachedClient should not be null in memcached manager!");
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.cache.CacheManager#get(java.lang.String)
     */
    @Override
    public Object get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object value = memCachedClient.get(key);
        if (value != null) {
            replace(key, value);
        }
        return value;
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.cache.CacheManager#set(java.lang.String, java.lang.Object)
     */
    @Override
    public <T> void set(String key, T value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        Date expiry = new Date(System.currentTimeMillis() + ONE_MINUTE * sessionExpireMins);
        memCachedClient.set(key, value, expiry);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.cache.CacheManager#replace(java.lang.String, java.lang.Object)
     */
    @Override
    public <T> void replace(String key, T value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        Date expiry = new Date(System.currentTimeMillis() + ONE_MINUTE * sessionExpireMins);
        memCachedClient.replace(key, value, expiry);
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.core.cache.CacheManager#delete(java.lang.String)
     */
    @Override
    public void delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        memCachedClient.delete(key);
    }

    /**
     * @return the sessionExpireMins
     */
    public int getSessionExpireMins() {
        return sessionExpireMins;
    }

    /**
     * @param sessionExpireMins
     */
    public void setSessionExpireMins(int sessionExpireMins) {
        this.sessionExpireMins = sessionExpireMins;
    }

}
