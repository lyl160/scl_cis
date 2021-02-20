package cn.dofuntech.core.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.core.util.Encrypt;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月5日)
 * @version 1.0
 * filename:CookiesUtil.java 
 */
public class CookiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookiesUtil.class);

    public CookiesUtil() {
    }

    public static void setString3Des(String cName, String cValue, int iTimeout, String cDomain, HttpServletRequest request, HttpServletResponse response) {
        setString3Des(cName, cValue, iTimeout, cDomain, "/", request, response);
    }

    public static void setString3Des(String cName, String cValue, int iTimeout, String cDomain, String cPath, HttpServletRequest request, HttpServletResponse response) {
        String sEncrypt = Encrypt.encodeBase643Des("19FF904AFEE13360193C9E7845C1B2270123456712345678", cValue);
        setString(cName, sEncrypt, cPath, iTimeout, cDomain, request, response);
    }

    public static void setString3Des(String cName, String cValue, HttpServletRequest request, HttpServletResponse response) {
        setString3Des(cName, cValue, 0, null, request, response);
    }

    public static void setString3Des(String cName, String cValue, int iTimeout, HttpServletRequest request, HttpServletResponse response) {
        setString3Des(cName, cValue, iTimeout, null, request, response);
    }

    public static void setString3Des(String cName, String cValue, String cDomain, HttpServletRequest request, HttpServletResponse response) {
        setString3Des(cName, cValue, 0, cDomain, request, response);
    }

    public static String getString3Des(String cName, HttpServletRequest request) {
        String sReturn = getString(cName, request);
        logger.debug((new StringBuilder("\u83B7\u53D6\u89E3\u5BC6\u7684\u6570\u636E\uFF1A")).append(sReturn).toString());
        if (StringUtils.isNotBlank(sReturn))
            return Encrypt.decodeBase643Des("19FF904AFEE13360193C9E7845C1B2270123456712345678", sReturn);
        else
            return null;
    }

    public static void setString(String cName, String cValue, int iTimeout, HttpServletRequest request, HttpServletResponse response) {
        setString(cName, cValue, "/", iTimeout, null, request, response);
    }

    public static void setString(String cName, String cValue, HttpServletRequest request, HttpServletResponse response) {
        setString(cName, cValue, "/", 0, null, request, response);
    }

    public static void setString(String cName, String cValue, String cPath, int iTimeout, String cDomain, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(cName))
            return;
        Delete(cName, request, response);
        Cookie cookie = new Cookie(cName, cValue);
        cookie.setPath(cPath);
        if (cDomain != null)
            cookie.setDomain(cDomain);
        if (iTimeout > 0)
            cookie.setMaxAge(iTimeout);
        response.addCookie(cookie);
    }

    public static boolean Delete(String cName, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null)
            return false;
        for (int i = 0 ; i < cookies.length ; i++)
            if (cookies[i].getName().equals(cName)) {
                cookies[i].setValue("");
                cookies[i].setMaxAge(0);
                response.addCookie(cookies[i]);
                return true;
            }

        return false;
    }

    public static String getString(String cName, HttpServletRequest request) {
        if (StringUtils.isBlank(cName))
            return null;
        Cookie cookies[] = request.getCookies();
        if (cookies == null)
            return null;
        for (int i = 0 ; i < cookies.length ; i++)
            if (cookies[i].getName().equalsIgnoreCase(cName)) {
                String v = cookies[i].getValue();
                if (v == null)
                    return null;
                else
                    return v;
            }

        return null;
    }

    public static int getInt(String cName, HttpServletRequest request) {
        int iReturn = 0;
        String cKey = getString(cName, request);
        if (StringUtils.isBlank(cKey))
            return 0;
        try {
            iReturn = NumberUtils.toInt(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6int\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return iReturn;
    }

    public static long getLong(String cName, HttpServletRequest request) {
        long lReturn = 0L;
        try {
            String cKey = getString(cName, request);
            if (StringUtils.isNotBlank(cKey))
                lReturn = NumberUtils.toLong(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6long\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return lReturn;
    }

    public static double getDouble(String cName, HttpServletRequest request) {
        double deturn = 0.0D;
        try {
            String cKey = getString(cName, request);
            if (StringUtils.isNotBlank(cKey))
                deturn = NumberUtils.toDouble(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6double\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return deturn;
    }

    public static int getInt3Des(String cName, HttpServletRequest request) {
        int iReturn = -1;
        String strs;
        strs = getString(cName, request);
        if (strs == null)
            return iReturn;
        String cKey = Encrypt.decodeBase643Des("19FF904AFEE13360193C9E7845C1B2270123456712345678", strs);
        if (StringUtils.isBlank(cKey))
            return -1;
        try {
            iReturn = NumberUtils.toInt(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6\u52A0\u5BC6\u7684int\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return iReturn;
    }

    public static long getLong3Des(String cName, HttpServletRequest request) {
        long lReturn = -1L;
        try {
            String cKey = Encrypt.decodeBase643Des("19FF904AFEE13360193C9E7845C1B2270123456712345678", getString(cName, request));
            if (StringUtils.isNotBlank(cKey))
                lReturn = NumberUtils.toLong(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6long\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return lReturn;
    }

    public static double getDouble3Des(String cName, HttpServletRequest request) {
        double deturn = 0.0D;
        try {
            String cKey = Encrypt.decodeBase643Des("19FF904AFEE13360193C9E7845C1B2270123456712345678", getString(cName, request));
            if (StringUtils.isNotBlank(cKey))
                deturn = NumberUtils.toDouble(cKey);
        }
        catch (NumberFormatException e) {
            logger.error((new StringBuilder("\u83B7\u53D6double\u7C7B\u578Bcookies\u540D\u79F0:")).append(cName).append("\u65F6\u95F4\u53D1\u751F\u9519\u8BEF\uFF1A").append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return deturn;
    }

}
