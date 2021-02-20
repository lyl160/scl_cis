package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * InspectionTemplate
 */
public class InspectionTemplate extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * name
	 */
	private String name = EMPTY;
	/**
	 * remark
	 */
	private String remark = EMPTY;
	/**
	 * school_id
	 */
	private Long schoolId = LONG_EMPTY;
	/**
	 * add_time
	 */
	private Timestamp addTime;
	/**
	 * edit_time
	 */
	private Timestamp editTime;
	
	private String schoolname;//学校名称
	
	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public InspectionTemplate() {
		super();
	}
	
	public InspectionTemplate(Long id, String name, String remark, Long schoolId, Timestamp addTime, Timestamp editTime) {
		super();
		this.id = id; 
		this.name = name; 
		this.remark = remark; 
		this.schoolId = schoolId; 
		this.addTime = addTime; 
		this.editTime = editTime; 
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	} 
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
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
}
