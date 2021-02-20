package cn.dofuntech.cis.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 短信模板
 * 
 * @author luokai
 * 
 */
public class InformationPushMode implements Serializable {
	
	private static String DEFAULT_NULL = "";//空串
	private static String FROM = "777";//
	private static String TYPE = "411";//

	private static final long serialVersionUID = -7941739771720194274L;
	private String v = DEFAULT_NULL;
	private String[] data;
	private String content;
	private String templateId;
	private String mobile= DEFAULT_NULL;
	private String mid = DEFAULT_NULL;
	private String form = FROM;
	private String type = TYPE;

	public InformationPushMode() {
		super();
	}
	
	/**部分参数构造器
	 * @param validateCode 验证码
	 * @param mobile 手机号
	 */
	public InformationPushMode(String templateId, String[] data) {
		super();
		this.templateId = templateId;
		this.data = data;
	}
	
	public static String getFROM() {
		return FROM;
	}

	public static void setFROM(String fROM) {
		FROM = fROM;
	}

	public static String getTYPE() {
		return TYPE;
	}

	public static void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}


	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public  String formatData() {
		JSONObject json = new JSONObject();//1级对象
		JSONObject json_data = new JSONObject();//2级对象
		JSONObject json_content = new JSONObject();//3级对象
		json_content.put("templateId",this.templateId);
		json_content.put("data", this.data);
		json_data.put("content",json_content);
		json_data.put("mobile",this.mobile);
		json.put("v", this.v);
		json.put("data", json_data);
		json.put("mid", this.mid);
		json.put("from", this.form);
		json.put("type", this.type);
		return json.toString();
	}
}