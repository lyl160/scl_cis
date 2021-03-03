package cn.dofuntech.cis.admin.repository.domain;

import cn.dofuntech.core.entity.DefaultValue;

import java.sql.Timestamp;
import java.util.List;

/**
 * InspectionMessage
 */
public class InspectionMessage extends DefaultValue {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	/**
	 * type 综述类型：1校务巡查反馈、2一周综述、3校园大事记、4教师执勤、5护校队巡查、6后勤巡查反馈
	 */
    private Long type = LONG_EMPTY;
	/**
	 * remark
	 */
	private String remark = EMPTY;
	/**
	 * user_name
	 */
	private String userName = EMPTY;
	/**
	 * user_id
	 */
	private Long userId = LONG_EMPTY;
	/**
	 * imgs
	 */
	private String imgs = EMPTY;
	/**
	 * fmt_date
	 */
	private String fmtDate = EMPTY;
	/**
	 * status
	 */
	private String status = EMPTY;
	/**
	 * add_time
	 */
	private Timestamp addTime;
	/**
	 * edit_time
	 */
	private Timestamp editTime;
	
	private String title;
	private Long receiver;
	private String startHours;
	private String endHours;
	private String days;
	//图片存放集合
	private  List<String> listimgs;
	
	/**
	 * 新增字段学校id
	 */
	private Long schoolId = LONG_EMPTY;
	
	private String schoolName;
	private String place;
	private String ksTime;
	private String jsTime;
    private String titleDiy;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

	public String getKsTime() {
		return ksTime;
	}

	public void setKsTime(String ksTime) {
		this.ksTime = ksTime;
	}

	public String getJsTime() {
		return jsTime;
	}

	public void setJsTime(String jsTime) {
		this.jsTime = jsTime;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getStartHours() {
		return startHours;
	}

	public void setStartHours(String startHours) {
		this.startHours = startHours;
	}

	public String getEndHours() {
		return endHours;
	}

	public void setEndHours(String endHours) {
		this.endHours = endHours;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public List<String> getListimgs() {
		return listimgs;
	}

	public void setListimgs(List<String> listimgs) {
		this.listimgs = listimgs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public InspectionMessage() {
		super();
	}
	
	public InspectionMessage(Long id, String remark, String userName, Long userId, String imgs, String fmtDate, String status, Timestamp addTime, Timestamp editTime) {
		super();
		this.id = id; 
		this.remark = remark; 
		this.userName = userName; 
		this.userId = userId; 
		this.imgs = imgs; 
		this.fmtDate = fmtDate; 
		this.status = status; 
		this.addTime = addTime; 
		this.editTime = editTime; 
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	} 
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	} 
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	} 
	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	} 
	public String getFmtDate() {
		return fmtDate;
	}

	public void setFmtDate(String fmtDate) {
		this.fmtDate = fmtDate;
	} 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	} 
	public Timestamp getEditTime() {
		return editTime;
	}

	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}

    public String getTitleDiy() {
        return titleDiy;
    }

    public void setTitleDiy(String titleDiy) {
        this.titleDiy = titleDiy;
    }
}
