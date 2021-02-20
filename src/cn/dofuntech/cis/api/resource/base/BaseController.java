package cn.dofuntech.cis.api.resource.base;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.wechat.service.WechatService;

public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Context
    protected HttpServletRequest request;
    @Context
    protected HttpServletResponse response;

    @Autowired
    protected WechatService wechatService;

    @Autowired
    protected UserService userService;

    /**
     * JSON返回参数
     * <ul>
     * <li>1: 表示正常返回
     * <li>0: 表示空值异常
     * <li>-1: 表示未知异常
     * </ul>
     */
    protected static final String CODE = "code";

    /**
     * 用于access_token的返回值
     */
    protected static final String TOKEN = "token";

    /**
     * 用于jsapi_ticket的返回值
     */
    protected static final String TICKET = "ticket";

    // ---- access_token ----
    protected static final String ACCESSTOKEN = "ACCESSTOKEN";
    protected static final String EXPIRETIME = "EXPIRETIME";
    // ---- jsapi_ticket ----
    protected static final String JSAPITICKET = "JSAPITICKET";
    protected static final String JSAPIEXPIRETIME = "JSAPIEXPIRETIME";
    // ---- 网页授权获取用户基本信息 ----
    protected static final String OPENID = "OPENID";
    protected static final String NICKNAME = "NICKNAME";

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    public String getTocken() {
        String token = request.getHeader("token");
        return token;
    }

    /**
     * 获取用户ID
     *
     * @param request
     * @return
     */
    public UserInf getUser() {
        String userId = request.getHeader("userId");
        UserInf user = new UserInf();
        //测试暂时数据
        user.setUserId(userId);
        UserInf entity = null;
        if (userId == null || "".equals(userId)) {
            return entity;
        }
        try {
            entity = userService.getEntity(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 获取Header
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeadersInfo() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取access_token
     * <pre>
     * 因为access_token有获取限制，所以放在Session中，超过2小时(7200秒)重新后台获取
     * </pre>
     *
     * @param request
     * @return
     */
    protected String getAccessTokenWithSession(HttpServletRequest request) {
        ServletContext session = request.getServletContext();

        long currentTime = System.currentTimeMillis() / 1000;
        Long expireTime = (Long) session.getAttribute(EXPIRETIME);
        if (expireTime == null || (currentTime - expireTime > 7200)) {
            String accessToken = "";
            try {
                accessToken = wechatService.getAccessToken();
                logger.info("通过接口获得access_token: " + accessToken);
                session.setAttribute(ACCESSTOKEN, accessToken);
                session.setAttribute(EXPIRETIME, currentTime);
            } catch (IOException e) {
                e.printStackTrace();
                accessToken = (String) session.getAttribute(ACCESSTOKEN);
                if (accessToken == null) {
                    accessToken = "";
                }
            }
            return accessToken;
        } else {
            return (String) session.getAttribute(ACCESSTOKEN);
        }
    }

    /**
     * 获取jsapi_ticket
     * <pre>
     * 将jsapi_ticket缓存2小时(7200秒)
     * </pre>
     *
     * @param request
     * @return
     */
    protected String getJsapiTicketWithSession(HttpServletRequest request, String url) {
        ServletContext session = request.getServletContext();

        long currentTime = System.currentTimeMillis() / 1000;
        Long expireTime = (Long) session.getAttribute(JSAPIEXPIRETIME);
        if (expireTime == null || (currentTime - expireTime > 7200)) {
            String jsapiTicket = "";
            try {
                jsapiTicket = wechatService.getJsapiTicket(getAccessTokenWithSession(request));
                logger.info("通过接口获得jsapi_ticket: " + jsapiTicket);
                session.setAttribute(JSAPITICKET, jsapiTicket);
                session.setAttribute(JSAPIEXPIRETIME, currentTime);
            } catch (IOException e) {
                e.printStackTrace();
                jsapiTicket = (String) session.getAttribute(JSAPITICKET);
                if (jsapiTicket == null) {
                    jsapiTicket = "";
                }
            }
            return jsapiTicket;
        } else {
            return (String) session.getAttribute(JSAPITICKET);
        }
    }

    /**
     * 拼接字符串
     *
     * @param list
     * @return
     */
    protected String implode(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        for (String str : list) {
            result.append(str);
        }
        return result.toString();
    }

}
