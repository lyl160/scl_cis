package cn.dofuntech.cis.admin.repository.domain;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * NoticeMx
 */
public class NoticeMx extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * user_name
	 */
	private String userName = EMPTY;
	/**
	 * duty_date
	 */
	private String dutyDate = EMPTY;
	/**
	 * start_time
	 */
	private String startTime = EMPTY;
	/**
	 * end_time
	 */
	private String endTime = EMPTY;
	/**
	 * place
	 */
	private String place = EMPTY;
	private String templateName = EMPTY;
	private String schoolId = EMPTY;

	public NoticeMx() {
		super();
	}
	
	public NoticeMx(Long id, String userName, String dutyDate, String startTime, String endTime, String place) {
		super();
		this.id = id; 
		this.userName = userName; 
		this.dutyDate = dutyDate; 
		this.startTime = startTime; 
		this.endTime = endTime; 
		this.place = place; 
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
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	} 
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	} 
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
