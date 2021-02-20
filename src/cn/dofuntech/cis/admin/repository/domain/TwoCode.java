package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * TwoCode
 */
public class TwoCode extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * text
	 */
	private String text = EMPTY;
	/**
	 * create_time
	 */
	private Timestamp createTime;
	/**
	 * status
	 */
	private String status = EMPTY;
	/**
	 * place
	 */
	private String place = EMPTY;
	
	private String images;
	
	private String uid;
	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public TwoCode() {
		super();
	}
	
	public TwoCode(Long id, String text, Timestamp createTime, String status, String place) {
		super();
		this.id = id; 
		this.text = text; 
		this.createTime = createTime; 
		this.status = status; 
		this.place = place; 
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	} 
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	} 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	} 
}
