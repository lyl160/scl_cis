package cn.dofuntech.cis.api.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 短信模板
 * 
 * @author luokai
 * 
 */
public class SmsMode implements Serializable {

    private static String     MODEL_NO         = "SMS_58880222";       //短信模板
    public static String      SIGN_NAME        = "千家惠";                //签名 （以后会改成“千家惠”）
    public static String      DEFAULT_NULL     = "";                   //空串
    public static String      FROM             = "1";                  //
    public static String      TYPE             = "611";                //

    /**
     * 
     */
    private static final long serialVersionUID = -7941739771720194274L;
    private String            v                = DEFAULT_NULL;
    private String            data;
    private String            content;
    private String            modelNo          = MODEL_NO;
    private String            validateCode;
    private String            signName         = SIGN_NAME;
    private String            mobile;
    private String            mid              = DEFAULT_NULL;
    private String            form             = FROM;
    private String            type             = TYPE;

    public SmsMode() {
        super();
    }

    /**部分参数构造器
     * @param validateCode 验证码
     * @param mobile 手机号
     */
    public SmsMode(String validateCode, String mobile) {
        super();
        this.validateCode = validateCode;
        this.mobile = mobile;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
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

    /**构造MQ需要的json字符串
     * @param smode
     * @return
     */
    public String formatData() {
        JSONObject json = new JSONObject();//1级对象
        JSONObject json_data = new JSONObject();//2级对象
        JSONObject json_content = new JSONObject();//3级对象
        json_content.put("modelNo", this.modelNo);
        json_content.put("validateCode", this.validateCode);
        json_content.put("signName", this.signName);
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