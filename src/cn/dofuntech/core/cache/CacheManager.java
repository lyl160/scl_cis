/**
 * 
 */
package cn.dofuntech.core.cache;

/**
 * Cache管理方法
 * <p>
 * 在Spring的配置文件中需要维护 cacheManager 的bean， 如下： <br/>
 * &lt;bean id="cacheManager" class="cn.txjr.core.cache.impl.CacheManagerImpl"
 * /&gt;
 * </p>
 * 
 * @author lxu
 * 
 */
public interface CacheManager {

	/**
	 * get value via key
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key);

	/**
	 * set value into key
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 */
	public <T> void set(String key, T value);

	/**
	 * replace value into key
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 */
	public <T> void replace(String key, T value);

	/**
	 * delete value of key
	 * 
	 * @param <T>
	 * @param key
	 */
	public void delete(String key);
}
