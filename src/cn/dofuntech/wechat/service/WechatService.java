/**
 * 
 */
package cn.dofuntech.wechat.service;

import java.io.IOException;
import java.util.Map;

import cn.dofuntech.wechat.model.PayMessage;
import cn.dofuntech.wechat.model.PayOrder;

/**
 * @author lxu
 *
 */
public interface WechatService {

    /**
     * 获得access_token
     * 
     * @return
     */
    public String getAccessToken() throws IOException;

    /**
     * 获得jsapi_ticket
     * 
     * @param accessToken
     * @return
     */
    public Map<String, Object> getJsapiSign(String url, String jsapiTicket) throws IOException;

    /**
     * @return
     * @throws IOException
     */
    public Map<String, Object> payOrder(PayOrder payOrder) throws IOException;

    /**
     * 获得jsapi_ticket
     * 
     * @param accessToken
     * @return
     */
    public String getJsapiTicket(String accessToken) throws IOException;

    /**
     * 获取页面Token
     * 
     * @param code
     * @return
     * @throws IOException
     */
    public String getWebAccessToken(String code) throws IOException;

    /**
     * 刷新页面Token
     * 
     * @param refreshToken
     * @return
     * @throws IOException
     */
    public String refreshWebAccessToken(String refreshToken) throws IOException;

    /**
     * 拉取用户信息
     * 
     * @param accessToken
     * @param openid
     * @return
     * @throws IOException
     */
    public String getUserInfo(String accessToken, String openid) throws IOException;

    /**
     * @param pay
     * @return
     * @throws IOException
     */
    public Map<String, String> unifiedOrder(PayMessage pay) throws IOException;

}
