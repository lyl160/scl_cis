package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * TowCodeResult
 */
public class TowCodeResult extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * create_time
	 */
	private Timestamp createTime;
	/**
	 * two_text
	 */
	private String twoText = EMPTY;
	/**
	 * two_id
	 */
	private Long twoId = LONG_EMPTY;
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public TowCodeResult() {
		super();
	}
	
	public TowCodeResult(Long id, Timestamp createTime, String twoText, Long twoId) {
		super();
		this.id = id; 
		this.createTime = createTime; 
		this.twoText = twoText; 
		this.twoId = twoId; 
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	} 
	public String getTwoText() {
		return twoText;
	}

	public void setTwoText(String twoText) {
		this.twoText = twoText;
	} 
	public Long getTwoId() {
		return twoId;
	}

	public void setTwoId(Long twoId) {
		this.twoId = twoId;
	} 
}
