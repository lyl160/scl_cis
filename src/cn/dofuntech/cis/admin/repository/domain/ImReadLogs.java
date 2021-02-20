package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * ImReadLogs
 */
public class ImReadLogs extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * message_id
	 */
	private Long messageId = LONG_EMPTY;
	/**
	 * user_id
	 */
	private Long userId = LONG_EMPTY;
	/**
	 * user_name
	 */
	private String userName = EMPTY;
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
	 * 新增字段学校id
	 */
	private Long schoolId = LONG_EMPTY;


    //辅助字段
    InspectionMessage inspectionMessage;

	
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public ImReadLogs() {
		super();
	}
	
	public ImReadLogs(Long id, Long messageId, Long userId, String userName, String isread, Timestamp readtime, Timestamp addtime, Timestamp edittime) {
		super();
		this.id = id; 
		this.messageId = messageId; 
		this.userId = userId; 
		this.userName = userName; 
		this.isread = isread; 
		this.readtime = readtime; 
		this.addtime = addtime; 
		this.edittime = edittime; 
	}
	
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	} 
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	} 
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

    public InspectionMessage getInspectionMessage() {
        return inspectionMessage;
    }

    public void setInspectionMessage(InspectionMessage inspectionMessage) {
        this.inspectionMessage = inspectionMessage;
    }
}
