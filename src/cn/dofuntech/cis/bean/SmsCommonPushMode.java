package cn.dofuntech.cis.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 短信模板
 * 
 * @author luokai
 * 
 */
public class SmsCommonPushMode implements Serializable {

    private static String       DEFAULT_NULL     = "";                    //空串

    private static final long   serialVersionUID = -7941739771720194274L;
    private String              v                = DEFAULT_NULL;
    private Map<String, Object> data             = Collections.emptyMap();
    private String              content;
    private String              modelNo;
    private String              mobile           = DEFAULT_NULL;
    private String              mid              = DEFAULT_NULL;
    private String              form             = SmsMode.FROM;
    private String              type             = SmsMode.TYPE;

    public SmsCommonPushMode() {
        super();
    }

    /**部分参数构造器
     * @param validateCode 验证码
     * @param mobile 手机号
     */
    public SmsCommonPushMode(String modelNo, Map<String, Object> data) {
        super();
        this.modelNo = modelNo;
        this.data = data;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    /**
     * @return the data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the modelNo
     */
    public String getModelNo() {
        return modelNo;
    }

    /**
     * @param modelNo the modelNo to set
     */
    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**构造MQ需要的json字符串
     * @param smode
     * @return
     */
    public String formatData() {
        JSONObject json = new JSONObject();//1级对象
        JSONObject json_data = new JSONObject();//2级对象
        JSONObject json_content = new JSONObject();//3级对象
        json_content.put("modelNo", this.modelNo);
        json_content.put("mobileNo", this.mobile);
        json_content.put("signName", SmsMode.SIGN_NAME);
        json_content.putAll(this.data);
        json_data.put("content", json_content);
        json_data.put("mobile", this.mobile);
        json.put("v", this.v);
        json.put("data", json_data);
        json.put("mid", this.mid);
        json.put("from", this.form);
        json.put("type", this.type);
        return json.toString();
    }
}