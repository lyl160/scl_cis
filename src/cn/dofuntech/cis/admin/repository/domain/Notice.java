package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * Notice
 */
public class Notice extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * title
	 */
	private String title = EMPTY;
	/**
	 * content
	 */
	private String content = EMPTY;
	/**
	 * type 1值班提醒
	 */
	private String type = EMPTY;
	/**
	 * user_id
	 */
	private Long userId = LONG_EMPTY;
	/**
	 * isread
	 */
	private String isread = EMPTY;
	/**
	 * readtime
	 */
	private Timestamp readtime;
	/**
	 * addtime
	 */
	private Timestamp addtime;
	/**
	 * edittime
	 */
	private Timestamp edittime;
	/**
	 * duty_date
	 */
	private String dutyDate = EMPTY;
	
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * 新增字段学校id
	 */
	private Long schoolId = LONG_EMPTY;
	//临时字段
	private String userName;
	
	public Notice() {
		super();
	}
	
	public Notice(Long id, String title, String content, String type, Long userId, String isread, Timestamp readtime, Timestamp addtime, Timestamp edittime) {
		super();
		this.id = id; 
		this.title = title; 
		this.content = content; 
		this.type = type; 
		this.userId = userId; 
		this.isread = isread; 
		this.readtime = readtime; 
		this.addtime = addtime; 
		this.edittime = edittime; 
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	} 
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	} 
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	} 
	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	} 
	public Timestamp getReadtime() {
		return readtime;
	}

	public void setReadtime(Timestamp readtime) {
		this.readtime = readtime;
	} 
	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	} 
	public Timestamp getEdittime() {
		return edittime;
	}

	public void setEdittime(Timestamp edittime) {
		this.edittime = edittime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(String dutyDate) {
		this.dutyDate = dutyDate;
	} 
	
}
