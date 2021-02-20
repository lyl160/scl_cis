package cn.dofuntech.core.util.web;

import java.awt.Color;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import cn.dofuntech.core.page.Paginator;
import com.github.pagehelper.PageHelper;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月5日)
 * @version 1.0
 * filename:WebUtil.java 
 */
public class WebUtil {

    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new java.awt.Color(r, g, b);
    }

    /**
     * 从request中获得参数Map，并返回可读的Map
     * 
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, String[]>> entries = properties.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        String name = StringUtils.EMPTY;
        String value = StringUtils.EMPTY;
        while (entries.hasNext()) {
            entry = entries.next();
            name = entry.getKey();
            String[] valueObj = entry.getValue();
            if (null == valueObj) {
                value = StringUtils.EMPTY;
            }
            else {
                if (valueObj.length == 1) {
                    value = valueObj[0];
                }
                else {
                    value = StringUtils.join(valueObj, ",");
                }
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 
     * @param request
     * @param name
     * @return
     */
    public static long getLong(HttpServletRequest request, String name) {
        return NumberUtils.toLong(StringUtils.trim(request.getParameter(name)));
    }

    /**
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getString(HttpServletRequest request, String name) {
        return StringUtils.trim(request.getParameter(name));
    }

    /**
     * 
     * @param request
     * @param name
     * @return
     */
    public static int getInt(HttpServletRequest request, String name) {
        return NumberUtils.toInt(StringUtils.trim(request.getParameter(name)));
    }

    /**
     * 判断是否为ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }

    /**
     * 取得带相同前缀的Request Parameters.
     * 
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                }
                else if (values.length > 1) {
                    params.put(unprefixed, values);
                }
                else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 构建分页参数对象
     * @param request
     * @return
     */
    public static void buildPage(HttpServletRequest req) {
        int pageNum = getInt(req, "page");
        int pageSize = getInt(req, "rows");
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        PageHelper.startPage(pageNum, pageSize);
    }

    /**
     * 构建分页参数对象
     * @param request
     * @return
     */
    public static Paginator buildPaginator(Map<String, Object> params) {
        int pageNum = MapUtils.getIntValue(params, "page");
        int pageSize = MapUtils.getIntValue(params, "rows");
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        return new Paginator(pageNum, pageSize);
    }

    public static <T> MybatisPage<T> buildMybatisPage(HttpServletRequest req) {
        return buildMybatisPage(req, "filter_");
    }

    public static <T> MybatisPage<T> buildMybatisPage(HttpServletRequest req, String prefix) {
        buildPage(req);
        MybatisPage<T> batisPage = new MybatisPage<T>();
        Map<String, Object> filterParamMap = getParametersStartingWith(req, prefix);
        batisPage.putAll(filterParamMap);
        return batisPage;
    }

    /**
     * @param httpServletRequest
     * @return
     */
    public static String getFullContextPath(HttpServletRequest httpServletRequest) {
        int port = httpServletRequest.getServerPort();
        String portStr = (port == 80 ? "" : ":" + port);
        return new StringBuilder().append(httpServletRequest.getScheme()).append("://").append(httpServletRequest.getServerName() + portStr).append(httpServletRequest.getContextPath()).toString();
    }
}
