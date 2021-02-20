/**
 * 
 */
package cn.dofuntech.core.factory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 读取properties文件的FactoryBean
 * <p/>
 * properties文件在Spring的配置文件中配置后可以直接使用PropertiesFactoryBean将properties文件中的内容存入<code>Map<String, String></code>中返回<br>
 * <p/>
 * <b>示例</b>
 * <p/>
 * demo.properties文件中有下列内容：
 * 
 * Spring的配置文件如下所示：
 * <pre>
 * &lt;beans&gt;
 * 	...
 * 	&lt;bean id="demoProperties" class="cn.txjr.core.factory.PropertiesFactoryBean"&gt;
 *		&lt;property name="properties"&gt;
 *			&lt;list&gt;
 *				&lt;value&gt;demo.properties&lt;/value&gt;
 *			&lt;/list&gt;
 *		&lt;/property&gt;
 *	&lt;/bean&gt;
 * 	...
 * &lt;beans&gt;
 * </pre>
 * 若将<code>demoProperties</code>注入到普通Bean中，则<code>demoProperties</code>为一个<code>Map<String, String></code>类型的属性，可以通过key获得value的值。如上例所示：
 * <pre>
 * demoProperties.get("demo.name")
 * demoProperties.get("demo.sex")
 * demoProperties.get("demo.birthday")
 * demoProperties.get("demo.address")
 * </pre>
 *
 */
public class PropertiesFactoryBean implements FactoryBean<Map<String, String>>, InitializingBean {

    private static final String PACKAGE_SEPERATOR = "/";

    /**
     * 解析properties文件后存放结果数据
     */
    private Map<String, String> map;

    /**
     * 需要解析的properties文件
     */
    private List<String>        properties;

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public Map<String, String> getObject() throws Exception {
        return map;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        map = new HashMap<String, String>();

        for (String propertyPath : properties) {
            if (!propertyPath.startsWith(PACKAGE_SEPERATOR)) {
                propertyPath = PACKAGE_SEPERATOR + propertyPath;
            }

            ResourceBundle rb = new PropertyResourceBundle(this.getClass().getResourceAsStream(propertyPath));
            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                map.put(key, rb.getString(key));
            }
        }
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
