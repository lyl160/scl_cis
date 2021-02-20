/**
 * 
 */
package cn.dofuntech.wechat.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import cn.dofuntech.wechat.model.PayMessage;
import cn.dofuntech.wechat.model.PayOrder;
import cn.dofuntech.wechat.service.WechatService;
import cn.dofuntech.wechat.utils.MD5Util;
import cn.dofuntech.wechat.utils.SHA1;
import cn.dofuntech.wechat.utils.XStreamFactory;
import com.thoughtworks.xstream.XStream;

/**
 * @author lxu
 *
 */
@Service
public class WechatServiceImpl implements WechatService {

    private static final transient Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    @Value("#{properties['wechat.appid']}")
    private String                        appid;

    @Value("#{properties['wechat.appsecret']}")
    private String                        appsecret;

    @Value("#{properties['wechat.token']}")
    private String                        token;

    @Value("#{properties['pay.api.key']}")
    private String                        payKey;

    @Value("#{properties['pay.mch.id']}")
    private String                        mchId;

    @Value("#{properties['pay.notify.url']}")
    private String                        notifyUrl;

    @Value("#{properties['web.context.path']}")
    private String                        webContextPath;

    /*
     * (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#getAccessToken()
     */
    @Override
    public String getAccessToken() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appsecret;

        //		URL urlGet = new URL(url);
        //		HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
        //		http.setRequestMethod("GET");
        //		http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
        //        http.setDoOutput(true);        
        //        http.setDoInput(true);
        //        
        //        http.connect();
        //        
        //        InputStream is = http.getInputStream();
        //        int size = is.available();
        //        byte[] jsonBytes = new byte[size];
        //        is.read(jsonBytes);
        //        String message = new String(jsonBytes,"UTF-8");
        //        http.disconnect();
        //        
        //        JSONObject msgJson = JSONObject.parseObject(message);
        //        return msgJson.getString("access_token");

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            logger.info("调用微信服务器获取 access_token 接口, 返回值: " + jsonStr);
            JSONObject msgJson = JSONObject.parseObject(jsonStr);
            return msgJson.getString("access_token");
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
    }

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#getJsapiTicket(java.lang.String)
     */
    @Override
    public String getJsapiTicket(String accessToken) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            logger.info("调用微信服务器获取 jsapi_ticket 接口, 返回值: " + jsonStr);
            JSONObject msgJson = JSONObject.parseObject(jsonStr);
            return msgJson.getString("ticket");
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
    }

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#getWebAccessToken(java.lang.String)
     */
    @Override
    public String getWebAccessToken(String code) throws IOException {
        logger.info("调用微信服务器获取网页授权access_token, 参数: [code]" + code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            logger.info("调用微信服务器获取网页授权access_token, 返回值: " + jsonStr);
            return jsonStr;
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
    }

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#refreshWebAccessToken(java.lang.String)
     */
    @Override
    public String refreshWebAccessToken(String refreshToken) throws IOException {
        logger.info("调用微信服务器获取网页授权access_token, 参数: [refreshToken]" + refreshToken);
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appid + "&grant_type=refresh_token&refresh_token=" + refreshToken;

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            logger.info("调用微信服务器刷新网页授权access_token, 返回值: " + jsonStr);
            return jsonStr;
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
    }

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#getUserInfo(java.lang.String, java.lang.String)
     */
    @Override
    public String getUserInfo(String accessToken, String openid) throws IOException {
        logger.info("调用微信服务器拉取用户信息接口, 参数: [accessToken]" + accessToken + ", [openid]" + openid);
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            jsonStr = new String(jsonStr.getBytes("ISO8859-1"), "UTF-8");
            logger.info("调用微信服务器拉取用户信息接口, 返回值: " + jsonStr);
            return jsonStr;
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
    }

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#order(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */

    /**
     * 使用XString解析XML
     */
    private static XStream xstream = XStreamFactory.init(true);

    /* (non-Javadoc)
     * @see com.leshifu.wechat.service.WechatService#unifiedOrder()
     */

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static Map<String, String> parseXml(String string) {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 读取输入流
        try {
            Document document = DocumentHelper.parseText(string);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        }
        catch (DocumentException ex) {
            ex.printStackTrace();
            map.put("return_code", "FAIL");
            map.put("return_msg", "解析请求XML时发生错误: " + ex.getMessage());
        }

        return map;
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.wechat.service.WechatService#unifiedOrder(cn.dofuntech.wechat.model.PayMessage)
     */
    @Override
    public Map<String, String> unifiedOrder(PayMessage pay) throws IOException {

        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        Map<String, String> result = new HashMap<String, String>();

        // 生成XML字符串
        //      xstream.alias("xml", pay.getClass());
        //      String inputXML = xstream.toXML(pay);
        String inputXML = pay.toXML();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            logger.info("调用微信统一下单接口，请求参数：" + inputXML);
            StringEntity entity = new StringEntity(inputXML, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String resp = EntityUtils.toString(response.getEntity(), "UTF-8");
                logger.info("调用微信统一下单接口，请求状态:" + response.getStatusLine() + ", 返回内容：" + resp);
                result = parseXml(resp);
                logger.info("解析返回内容：" + result);
            }
            else {
                result.put("return_code", "FAIL");
                result.put("return_msg", "返回错误: " + response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase());
            }
        }
        finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.close();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.wechat.service.WechatService#getJsapiSign(java.lang.String)
     */
    @Override
    public Map<String, Object> getJsapiSign(String url, String jsapiTicket) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        String nonceStr = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);

        // 注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        logger.info("JSAPI签名前的字符串:" + string1);
        String signature = SHA1.encrypt(string1);
        logger.info("JSAPI签名字符串:" + signature);

        result.put("appId", appid);
        result.put("debug", 1);
        result.put("url", url);
        result.put("jsapi_ticket", jsapiTicket);
        result.put("nonceStr", nonceStr);
        result.put("timestamp", timestamp);
        result.put("signature", signature);
        return result;
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.wechat.service.WechatService#payOrder(cn.dofuntech.wechat.model.PayOrder)
     */
    @Override
    public Map<String, Object> payOrder(PayOrder order) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Date startDate = new Date();
        //获取订单ID
        String orderId = order.getTradeNo();
        logger.info("本地订单ID: " + orderId);
        Date expireDate = DateUtils.addHours(startDate, 2);

        String nonceStr = RandomStringUtils.randomAlphanumeric(32);
        // 调用微信统一下单API返回预付单信息
        PayMessage pay = new PayMessage();
        pay.setAppid(appid);
        pay.setMch_id(mchId);
        pay.setDevice_info("WEB");
        pay.setNonce_str(nonceStr);
        pay.setBody(order.getBody());
        pay.setDetail(order.getDetail());
        pay.setOut_trade_no(orderId);
        pay.setFee_type("CNY");
        pay.setTotal_fee(order2wxAmount(new BigDecimal(order.getPrice())));
        pay.setSpbill_create_ip(order.getIp());
        pay.setTime_start(wxDateFormat.format(startDate));
        pay.setTime_expire(wxDateFormat.format(expireDate));
        pay.setNotify_url(webContextPath + "/" + notifyUrl);
        pay.setTrade_type("JSAPI");
        pay.setOpenid(order.getOpenId());
        pay.setKey(payKey);
        // 生成签名
        pay.sign();
        logger.info("调用微信支付统一订单接口，参数: " + pay);
        Map<String, String> orderResult = unifiedOrder(pay);
        String perpayId = orderResult.get("prepay_id");
        logger.info("微信支付生成的预付单（prepay_id）" + perpayId);

        // 生成JSAPI页面调用的支付参数并签名
        long timestamp = System.currentTimeMillis() / 1000;
        String pack = "prepay_id=" + perpayId;

        // 返回参数
        String string1 = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=" + pack + "&signType=MD5&timeStamp=" + timestamp + "&key=" + payKey;
        logger.info("支付签名前的字符串: " + string1);
        String paySign = MD5Util.stringToMD5(string1);
        logger.info("支付签名字符串 paySign: " + paySign);
        result.put("appId", appid);
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonceStr);
        result.put("package", pack);
        result.put("signType", "MD5");
        result.put("paySign", paySign);
        return result;
    }

    private static BigDecimal HUNDRED      = new BigDecimal(100);

    SimpleDateFormat          wxDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 订单金额转换成微信支付金额
     *  
     * @param amount
     * @return
     */
    private int order2wxAmount(BigDecimal amount) {
        if (amount == null) {
            return 0;
        }
        return amount.multiply(HUNDRED).intValue();
    }

}
