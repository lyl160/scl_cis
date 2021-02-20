package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * Schedule
 */
public class Schedule extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * template_id
	 */
	private Long templateId = LONG_EMPTY;
	/**
	 * users
	 */
	private String users = EMPTY;
	/**
	 * duty_date
	 */
	private String dutyDate = EMPTY;
	/**
	 * week
	 */
	private String week = EMPTY;
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
	
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	//非数据库字段
	private String templateName;//巡检模板名称
	private String usersName;//值班老师名称

	
	

	public Schedule() {
		super();
	}
	
	public Schedule(Long id, Long templateId, String users, String dutyDate, String week, Timestamp addtime, Timestamp edittime) {
		super();
		this.id = id; 
		this.templateId = templateId; 
		this.users = users; 
		this.dutyDate = dutyDate; 
		this.week = week; 
		this.addtime = addtime; 
		this.edittime = edittime; 
	}
	
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	} 
	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	} 
	public String getDutyDate() {
		return dutyDate;
	}

	public void setDutyDate(String dutyDate) {
		this.dutyDate = dutyDate;
	} 
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getUsersName() {
		return usersName;
	}

	public void setUsersName(String usersName) {
		this.usersName = usersName;
	} 
	
}
