package cn.dofuntech.core.util.web;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtil
{
  private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

  public static String getString(String sKey, HttpServletRequest request)
  {
    String sReturn = "";
    if (request.getParameterValues(sKey) != null) {
      sReturn = "";
      String[] sArray = request.getParameterValues(sKey);
      for (int i = 0; i < sArray.length - 1; i++) {
        sReturn = sReturn + new String(sArray[i]) + ",";
      }
      sReturn = sReturn + new String(sArray[(sArray.length - 1)]);
    }
    return sReturn;
  }

  public static int getInt(String cKey, int defVal, HttpServletRequest request)
  {
    String cReturn = getStringByOther(cKey, request);
    return NumberUtils.toInt(cReturn, defVal);
  }

  public static int getInt(String cKey, HttpServletRequest request)
  {
    return getInt(cKey, 0, request);
  }

  public static long getLong(String cKey, long defVal, HttpServletRequest request) {
    String cReturn = getStringByOther(cKey, request);
    return NumberUtils.toLong(cReturn, defVal);
  }

  public static long getLong(String cKey, HttpServletRequest request)
  {
    return getLong(cKey, 0L, request);
  }

  public static double getDouble(String cKey, double defVal, HttpServletRequest request) {
    String cReturn = getStringByOther(cKey, request);
    return NumberUtils.toDouble(cReturn, defVal);
  }

  public static double getDouble(String cKey, HttpServletRequest request)
  {
    return getDouble(cKey, 0.0D, request);
  }

  public static float getFloat(String cKey, float defval, HttpServletRequest request) {
    String cReturn = getStringByOther(cKey, request);
    return NumberUtils.toFloat(cReturn, defval);
  }

  public static float getFloat(String cKey, HttpServletRequest request)
  {
    return getFloat(cKey, 0.0F, request);
  }

  private static String getStringByOther(String cKey, HttpServletRequest request)
  {
    String cReturn = null;
    if (request.getParameter(cKey) != null) {
      String sV = request.getParameter(cKey);
      if (sV != null) {
        sV = sV.trim();
        for (int i = 0; i < sV.length(); i++) {
          int myASCII = sV.charAt(i);
          if ((myASCII < 45) || (myASCII > 57)) {
            return null;
          }
          if (myASCII == 47) {
            return null;
          }
        }

        cReturn = sV;
      }
    }
    return cReturn;
  }

  public static Date getDate(String cKey, HttpServletRequest request)
  {
    Date date = null;
    String cReturn = getStringByOther(cKey, request);
    if (cReturn != null) {
      try {
        date = DateUtils.parseDate(cReturn, new String[] { "yyyy-MM-dd" });
      } catch (Exception ex) {
        logger.error("get date type error：" + ex.getMessage());
        logger.debug(null, ex);
      }
    }
    return date;
  }

  public static Date getDatetime(String cKey, HttpServletRequest request) {
    Date date = null;
    String cReturn = getStringByOther(cKey, request);
    if (cReturn != null) {
      try {
        date = DateUtils.parseDate(cReturn, new String[] { "yyyy-MM-dd HH:mm:ss" });
      } catch (Exception ex) {
        logger.error("get date type error：" + ex.getMessage());
        logger.debug(null, ex);
      }
    }
    return date;
  }

  public static String getRemoteAddr(HttpServletRequest req)
  {
    String ip = req.getHeader("X-Forwarded-For");
    if (StringUtils.isNotBlank(ip)) {
      String[] ips = StringUtils.split(ip, ',');
      if (ips != null) {
        for (String tmpip : ips) {
          if (!StringUtils.isBlank(tmpip))
          {
            tmpip = tmpip.trim();
            if ((isIPAddr(tmpip)) && (!tmpip.startsWith("10.")) && (!tmpip.startsWith("192.168.")) && (!"127.0.0.1".equals(tmpip)))
              return tmpip.trim();
          }
        }
      }
    }
    ip = req.getHeader("x-real-ip");
    if (isIPAddr(ip))
      return ip;
    ip = req.getRemoteAddr();
    if (ip.indexOf('.') == -1)
      ip = "127.0.0.1";
    return ip;
  }

  public static int getHttpPort(HttpServletRequest req)
  {
    try
    {
      return new URL(req.getRequestURL().toString()).getPort(); } catch (MalformedURLException excp) {
    }
    return 80;
  }

  public static Map<String, Object> getParams(HttpServletRequest request)
  {
    Map properties = request.getParameterMap();

    Map returnMap = new HashMap();
    Iterator entries = properties.entrySet().iterator();

    String name = "";
    String value = "";
    while (entries.hasNext()) {
      Map.Entry entry = (Map.Entry)entries.next();
      name = (String)entry.getKey();
      Object valueObj = entry.getValue();
      if (valueObj == null) {
        value = "";
      } else if ((valueObj instanceof String[])) {
        String[] values = (String[])valueObj;
        value = StringUtils.join(values, ",");
      } else {
        value = valueObj.toString();
      }
      returnMap.put(name, value);
    }
    return returnMap;
  }

  private static boolean isIPAddr(String addr)
  {
    if (StringUtils.isEmpty(addr))
      return false;
    String[] ips = StringUtils.split(addr, '.');
    if (ips.length != 4)
      return false;
    try {
      int ipa = Integer.parseInt(ips[0]);
      int ipb = Integer.parseInt(ips[1]);
      int ipc = Integer.parseInt(ips[2]);
      int ipd = Integer.parseInt(ips[3]);
      return (ipa >= 0) && (ipa <= 255) && (ipb >= 0) && (ipb <= 255) && (ipc >= 0) && 
        (ipc <= 255) && (ipd >= 0) && (
        ipd <= 255); } catch (Exception localException) {
    }
    return false;
  }

  public static String getDomainOfServerName(String host)
  {
    if (isIPAddr(host))
      return null;
    String[] names = StringUtils.split(host, '.');
    int len = names.length;
    if (len == 1) return null;
    if (len == 3) {
      return makeup(new String[] { names[(len - 2)], names[(len - 1)] });
    }
    if (len > 3) {
      String dp = names[(len - 2)];
      if ((dp.equalsIgnoreCase("com")) || (dp.equalsIgnoreCase("gov")) || (dp.equalsIgnoreCase("net")) || (dp.equalsIgnoreCase("edu")) || (dp.equalsIgnoreCase("org"))) {
        return makeup(new String[] { names[(len - 3)], names[(len - 2)], names[(len - 1)] });
      }
      return makeup(new String[] { names[(len - 2)], names[(len - 1)] });
    }
    return host;
  }

  public static boolean isRobot(HttpServletRequest req)
  {
    String ua = req.getHeader("user-agent");
    if (StringUtils.isBlank(ua)) return false;
    return (ua != null) && (
      (ua.indexOf("Baiduspider") != -1) || (ua.indexOf("Googlebot") != -1) || 
      (ua.indexOf("sogou") != -1) || 
      (ua.indexOf("sina") != -1) || 
      (ua.indexOf("iaskspider") != -1) || 
      (ua.indexOf("ia_archiver") != -1) || 
      (ua.indexOf("Sosospider") != -1) || 
      (ua.indexOf("YoudaoBot") != -1) || 
      (ua.indexOf("yahoo") != -1) || 
      (ua.indexOf("yodao") != -1) || 
      (ua.indexOf("MSNBot") != -1) || 
      (ua.indexOf("spider") != -1) || 
      (ua.indexOf("Twiceler") != -1) || 
      (ua.indexOf("Sosoimagespider") != -1) || 
      (ua.indexOf("naver.com/robots") != -1) || 
      (ua.indexOf("Nutch") != -1) || 
      (ua.indexOf("spider") != -1));
  }

  private static String makeup(String[] ps)
  {
    StringBuilder s = new StringBuilder();
    for (int idx = 0; idx < ps.length; idx++) {
      if (idx > 0)
        s.append('.');
      s.append(ps[idx]);
    }
    return s.toString();
  }

  /*public static <T> T form(Class<T> clazz, HttpServletRequest request)
  {
    try
    {
      Object bean = clazz.newInstance();
      BeanUtilsBean2.getInstance().populate(bean, request.getParameterMap());
      return bean; } catch (Exception ex) {
    }
    return null;
  }*/

  public static String getWebRealPath(HttpServletRequest request)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("http://");
    sb.append(request.getServerName());
    if (request.getServerPort() != 80) {
      sb.append(":");
      sb.append(request.getServerPort());
    }
    return sb.toString();
  }

  public static String getIpAddr(HttpServletRequest request)
  {
    String ip = request.getHeader("x-forwarded-for");
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /*public static <T> T populateObject(Class<T> clazz, HttpServletRequest request)
  {
    try
    {
      Object bean = clazz.newInstance();
      BeanUtilsBean.getInstance().populate(bean, request.getParameterMap());
      return bean; } catch (Exception e) {
    }
    return null;
  }*/

  public static void renderText(StringBuilder text, HttpServletResponse response)
  {
    try {
      response.setContentType("text/plain; charset=utf-8");
      response.getWriter().print(text);
    }
    catch (IOException localIOException) {
      try {
        response.getWriter().close(); } catch (IOException localIOException1) {  } } finally { try { response.getWriter().close();
      } catch (IOException localIOException2)
      {
      } }
  }

  public static void renderXml(StringBuilder text, HttpServletResponse response) {
    try {
      response.setContentType("text/xml; charset=utf-8");
      response.getWriter().print(text);
    }
    catch (IOException localIOException) {
      try {
        response.getWriter().close(); } catch (IOException localIOException1) {  } } finally { try { response.getWriter().close();
      } catch (IOException localIOException2)
      {
      } }
  }

  public static void renderJson(Object obj, HttpServletResponse response) {
    try {
      response.getWriter().print(JSON.toJSONString(obj));
    }
    catch (Exception localException) {
      try {
        response.getWriter().close(); } catch (IOException localIOException) {  } } finally { try { response.getWriter().close(); }
      catch (IOException localIOException1)
      {
      }
    }
  }
}