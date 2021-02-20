/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package cn.dofuntech.cis.admin.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.dofuntech.core.util.web.CookiesUtil;
import cn.dofuntech.core.util.web.RequestUtil;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private static Logger logger          = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    private String        loginUrl        = "/login";
    private String        loginzzAdminUrl = "/loginzzadmin";
    private String        initParam       = "adminid";
    /** 
    * 免登入 免检查地址 
    */ 
    private List<String> uncheckUrls; 

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView mv) throws Exception {

    }

    /**
     * 用户授权拦截器
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        String req_uri;
        String path;
        req_uri = request.getRequestURI();
        String servletUrl = request.getServletPath();
        logger.debug(" -- req_uri url : " + req_uri);
        logger.debug(" -- servlet请求路径 : " + servletUrl);
        if(uncheckUrls.contains(servletUrl)){
        	return true; 
    	}else{ 
            path = RequestUtil.getWebRealPath(request);
            logger.debug((new StringBuilder("initParam=")).append(initParam).toString());
            long iAdminid = 0L;
            Object obj = request.getSession().getAttribute(initParam);
            if (obj != null) {
                iAdminid = NumberUtils.toLong(obj.toString());
            }
            logger.debug((new StringBuilder("check user login :")).append(iAdminid).toString());
            if (iAdminid < 1L) {
                String itype = CookiesUtil.getString("itype", request);
                String redirectUrl = loginUrl;
                logger.debug((new StringBuilder("path=")).append(path).append(",loginUrl=").append(redirectUrl).toString());
                response.sendRedirect((new StringBuilder(String.valueOf(path))).append(request.getContextPath()).append(redirectUrl).toString());
                return false;
            }
            logger.debug(" -- Refrence url : " + request.getHeader("referer"));
    	}
        return true;
    }

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}
    
}
