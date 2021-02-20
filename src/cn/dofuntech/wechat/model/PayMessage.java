/**
 * 
 */
package cn.dofuntech.wechat.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.wechat.utils.MD5Util;

/**
 * @author lxu
 *
 */
public class PayMessage {

    private static final transient Logger logger      = LoggerFactory.getLogger(PayMessage.class);

    /**
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    private String                        appid;

    /**
     * 微信支付分配的商户号
     */
    private String                        mch_id;

    /**
     * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
     */
    private String                        device_info = "";

    /**
     * 随机字符串，不长于32位。
     */
    private String                        nonce_str;

    /**
     * 签名，详见签名生成算法
     */
    private String                        sign;

    /**
     * 商品或支付单简要描述
     */
    private String                        body;

    /**
     * 商品名称明细列表
     */
    private String                        detail;

    /**
     * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     */
    private String                        attach;

    /**
     * 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
     */
    private String                        out_trade_no;

    /**
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    private String                        fee_type    = "CNY";

    /**
     * 订单总金额，只能为整数，详见支付金额
     */
    private Integer                       total_fee;

    /**
     * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     */
    private String                        spbill_create_ip;

    /**
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
     */
    private String                        time_start;

    /**
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     * <b>注意：最短失效时间间隔必须大于5分钟</b>
     */
    private String                        time_expire;

    /**
     * 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
     */
    private String                        goods_tag;

    /**
     * 接收微信支付异步通知回调地址
     */
    private String                        notify_url;

    /**
     * 取值如下：JSAPI，NATIVE，APP，WAP,详细说明见参数规定
     */
    private String                        trade_type  = "JSAPI";

    /**
     * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
     */
    private String                        product_id;

    /**
     * no_credit--指定不能使用信用卡支付	
     */
    private String                        limit_pay;

    /**
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
     */
    private String                        openid;

    /**
     * API的商户Key
     */
    private String                        key;

    /**
     * 签名算法
     * @return
     */
    public String sign() {

        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        if (StringUtils.isNotEmpty(appid)) {
            parameters.put("appid", appid);
        }
        if (StringUtils.isNotEmpty(mch_id)) {
            parameters.put("mch_id", mch_id);
        }
        if (StringUtils.isNotEmpty(device_info)) {
            parameters.put("device_info", device_info);
        }
        if (StringUtils.isNotEmpty(nonce_str)) {
            parameters.put("nonce_str", nonce_str);
        }
        if (StringUtils.isNotEmpty(body)) {
            parameters.put("body", body);
        }
        if (StringUtils.isNotEmpty(detail)) {
            parameters.put("detail", detail);
        }
        if (StringUtils.isNotEmpty(attach)) {
            parameters.put("attach", attach);
        }
        if (StringUtils.isNotEmpty(out_trade_no)) {
            parameters.put("out_trade_no", out_trade_no);
        }
        if (StringUtils.isNotEmpty(fee_type)) {
            parameters.put("fee_type", fee_type);
        }
        if (total_fee != null) {
            parameters.put("total_fee", total_fee);
        }
        if (StringUtils.isNotEmpty(spbill_create_ip)) {
            parameters.put("spbill_create_ip", spbill_create_ip);
        }
        if (StringUtils.isNotEmpty(time_start)) {
            parameters.put("time_start", time_start);
        }
        if (StringUtils.isNotEmpty(time_expire)) {
            parameters.put("time_expire", time_expire);
        }
        if (StringUtils.isNotEmpty(goods_tag)) {
            parameters.put("goods_tag", goods_tag);
        }
        if (StringUtils.isNotEmpty(notify_url)) {
            parameters.put("notify_url", notify_url);
        }
        if (StringUtils.isNotEmpty(trade_type)) {
            parameters.put("trade_type", trade_type);
        }
        if (StringUtils.isNotEmpty(product_id)) {
            parameters.put("product_id", product_id);
        }
        if (StringUtils.isNotEmpty(limit_pay)) {
            parameters.put("limit_pay", limit_pay);
        }
        if (StringUtils.isNotEmpty(openid)) {
            parameters.put("openid", openid);
        }

        // 微信支付签名算法sign
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> set = parameters.entrySet(); // 所有参与传参的参数按照accsii排序（升序）
        Iterator<Map.Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        logger.info("微信支付签名前，字符串内容: " + sb.toString());
        this.sign = MD5Util.stringToMD5(sb.toString(), "UTF-8");
        logger.info("微信支付签名字符串: " + this.sign);
        return this.sign;
    }

    /**
     * @return the appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return the mch_id
     */
    public String getMch_id() {
        return mch_id;
    }

    /**
     * @param mch_id the mch_id to set
     */
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    /**
     * @return the device_info
     */
    public String getDevice_info() {
        return device_info;
    }

    /**
     * @param device_info the device_info to set
     */
    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    /**
     * @return the nonce_str
     */
    public String getNonce_str() {
        return nonce_str;
    }

    /**
     * @param nonce_str the nonce_str to set
     */
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    /**
     * @return the sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the attach
     */
    public String getAttach() {
        return attach;
    }

    /**
     * @param attach the attach to set
     */
    public void setAttach(String attach) {
        this.attach = attach;
    }

    /**
     * @return the out_trade_no
     */
    public String getOut_trade_no() {
        return out_trade_no;
    }

    /**
     * @param out_trade_no the out_trade_no to set
     */
    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    /**
     * @return the fee_type
     */
    public String getFee_type() {
        return fee_type;
    }

    /**
     * @param fee_type the fee_type to set
     */
    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    /**
     * @return the total_fee
     */
    public Integer getTotal_fee() {
        return total_fee;
    }

    /**
     * @param total_fee the total_fee to set
     */
    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    /**
     * @return the spbill_create_ip
     */
    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    /**
     * @param spbill_create_ip the spbill_create_ip to set
     */
    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    /**
     * @return the time_start
     */
    public String getTime_start() {
        return time_start;
    }

    /**
     * @param time_start the time_start to set
     */
    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    /**
     * @return the time_expire
     */
    public String getTime_expire() {
        return time_expire;
    }

    /**
     * @param time_expire the time_expire to set
     */
    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    /**
     * @return the goods_tag
     */
    public String getGoods_tag() {
        return goods_tag;
    }

    /**
     * @param goods_tag the goods_tag to set
     */
    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    /**
     * @return the notify_url
     */
    public String getNotify_url() {
        return notify_url;
    }

    /**
     * @param notify_url the notify_url to set
     */
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    /**
     * @return the trade_type
     */
    public String getTrade_type() {
        return trade_type;
    }

    /**
     * @param trade_type the trade_type to set
     */
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    /**
     * @return the product_id
     */
    public String getProduct_id() {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    /**
     * @return the limit_pay
     */
    public String getLimit_pay() {
        return limit_pay;
    }

    /**
     * @param limit_pay the limit_pay to set
     */
    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    /**
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PayMessage [appid=" + appid + ", mch_id=" + mch_id + ", device_info=" + device_info + ", nonce_str=" + nonce_str + ", sign=" + sign + ", body=" + body + ", detail=" + detail + ", attach=" + attach + ", out_trade_no=" + out_trade_no + ", fee_type=" + fee_type + ", total_fee=" + total_fee + ", spbill_create_ip=" + spbill_create_ip + ", time_start=" + time_start + ", time_expire=" + time_expire + ", goods_tag=" + goods_tag + ", notify_url=" + notify_url + ", trade_type=" + trade_type + ", product_id=" + product_id + ", limit_pay=" + limit_pay + ", openid=" + openid + ", key=" + key + "]";
    }

    /**
     * 
     * @return
     */
    public String toXML() {
        StringBuffer sb = new StringBuffer("<xml>").append("\n");
        sb.append("<appid>").append(appid).append("</appid>").append("\n");
        sb.append("<mch_id>").append(mch_id).append("</mch_id>").append("\n");
        if (StringUtils.isNotEmpty(device_info)) {
            sb.append("<device_info>").append(device_info).append("</device_info>").append("\n");
        }
        sb.append("<nonce_str>").append(nonce_str).append("</nonce_str>").append("\n");
        sb.append("<sign>").append(sign).append("</sign>").append("\n");
        sb.append("<body>").append(body).append("</body>").append("\n");
        if (StringUtils.isNotEmpty(detail)) {
            sb.append("<detail>").append(detail).append("</detail>").append("\n");
        }
        if (StringUtils.isNotEmpty(attach)) {
            sb.append("<attach>").append(attach).append("</attach>").append("\n");
        }
        sb.append("<out_trade_no>").append(out_trade_no).append("</out_trade_no>").append("\n");
        if (StringUtils.isNotEmpty(fee_type)) {
            sb.append("<fee_type>").append(fee_type).append("</fee_type>").append("\n");
        }
        sb.append("<total_fee>").append(total_fee).append("</total_fee>").append("\n");
        sb.append("<spbill_create_ip>").append(spbill_create_ip).append("</spbill_create_ip>").append("\n");
        if (StringUtils.isNotEmpty(time_start)) {
            sb.append("<time_start>").append(time_start).append("</time_start>").append("\n");
        }
        if (StringUtils.isNotEmpty(time_expire)) {
            sb.append("<time_expire>").append(time_expire).append("</time_expire>").append("\n");
        }
        if (StringUtils.isNotEmpty(goods_tag)) {
            sb.append("<goods_tag>").append(goods_tag).append("</goods_tag>").append("\n");
        }
        sb.append("<notify_url>").append(notify_url).append("</notify_url>").append("\n");
        sb.append("<trade_type>").append(trade_type).append("</trade_type>").append("\n");
        if (StringUtils.isNotEmpty(product_id)) {
            sb.append("<product_id>").append(product_id).append("</product_id>").append("\n");
        }
        if (StringUtils.isNotEmpty(limit_pay)) {
            sb.append("<limit_pay>").append(limit_pay).append("</limit_pay>").append("\n");
        }
        sb.append("<openid>").append(openid).append("</openid>").append("\n");
        sb.append("</xml>");
        return sb.toString();
    }
}
