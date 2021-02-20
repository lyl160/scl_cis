package cn.dofuntech.dfauth.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanDebugger
{
  private static Logger log = LoggerFactory.getLogger(BeanDebugger.class);

  public static String dump(Object bean)
  {
    StringBuffer beanInf = new StringBuffer();
    PropertyDescriptor[] descriptors = getAvailablePropertyDescriptors(bean);
    beanInf.append(bean.getClass().getName()).append("=[");
    for (int i = 0; (descriptors != null) && (i < descriptors.length); i++) {
      Method readMethod = descriptors[i].getReadMethod();
      try {
        Object value = readMethod.invoke(bean, new Object[0]);
        if ((value != null) && (!value.equals(""))) {
          beanInf.append(descriptors[i].getName() + "=" + value);
          if (i - 1 != descriptors.length)
            beanInf.append(",");
        }
      }
      catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    beanInf.append("]");
    return beanInf.toString();
  }

  public static PropertyDescriptor[] getAvailablePropertyDescriptors(Object bean)
  {
    try
    {
      BeanInfo info = Introspector.getBeanInfo(bean.getClass());
      if (info != null) {
        PropertyDescriptor[] pd = info.getPropertyDescriptors();
        Vector columns = new Vector();

        for (int i = 0; i < pd.length; i++) {
          String fieldName = pd[i].getName();

          if ((fieldName != null) && (!fieldName.equals("class"))) {
            columns.add(pd[i]);
          }
        }

        PropertyDescriptor[] arrays = new PropertyDescriptor[columns.size()];

        for (int j = 0; j < columns.size(); j++) {
          arrays[j] = ((PropertyDescriptor)columns.get(j));
        }

        return arrays;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
    return null;
  }
}