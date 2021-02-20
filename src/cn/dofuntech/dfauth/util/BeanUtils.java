package cn.dofuntech.dfauth.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils
{
  public static Map<String, Object> converToMap(Object bean)
    throws Exception
  {
    Class type = bean.getClass();
    Map map = new HashMap();
    BeanInfo beanInfo = Introspector.getBeanInfo(type);
    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (int i = 0; i < propertyDescriptors.length; i++) {
      PropertyDescriptor descriptor = propertyDescriptors[i];
      String propertyName = descriptor.getName();
      if (!propertyName.equals("class")) {
        Method readMethod = descriptor.getReadMethod();
        Object result = readMethod.invoke(bean, new Object[0]);
        if (result != null) {
          map.put(propertyName, result);
        }
      }
    }
    return map;
  }
}