package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import java.util.List;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * InspectionCategory
 */
public class InspectionCategory extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * value
	 */
	private String value = EMPTY;
	/**
	 * ilevel
	 */
	private String ilevel = EMPTY;
	/**
	 * pid1
	 */
	private Long pid1 = LONG_EMPTY;
	/**
	 * pid2
	 */
	private Long pid2 = LONG_EMPTY;
	/**
	 * pid3
	 */
	private Long pid3 = LONG_EMPTY;
	/**
	 * img
	 */
	private String img = EMPTY;
	/**
	 * status
	 */
	private Integer status = INT_EMPTY;
	/**
	 * seq
	 */
	private Long seq = LONG_EMPTY;
	/**
	 * template_id
	 */
	private Long templateId = LONG_EMPTY;
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
	
	private String value1;
	
	private String schoolname;//学校名称
	
	private String name;//巡检名称
	
	private String startTime;
	
	private String endTime;

	private String diyTime;

    /**
     * users 校务巡查分配用户id，用逗号隔开
     */
    private String users = EMPTY;
    /**
     * users 校务巡查分配用户name，用逗号隔开
     */
    private String usersName = EMPTY;




    //非数据库字段 辅助字段
	private String templateName;//巡检模板名称

    private String [] userIdArr;//用户id数组

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	private List<InspectionCategory> list;
	
	
	
	public List<InspectionCategory> getList() {
		return list;
	}

	public void setList(List<InspectionCategory> list) {
		this.list = list;
	}

	public InspectionCategory() {
		super();
	}
	
	public InspectionCategory(Long id, String value, String ilevel, Long pid1, Long pid2, Long pid3, String img, Integer status, Long seq, Long templateId, Long schoolId, Timestamp addTime, Timestamp editTime, String startTime, String endTime) {
		super();
		this.id = id; 
		this.value = value; 
		this.ilevel = ilevel; 
		this.pid1 = pid1; 
		this.pid2 = pid2; 
		this.pid3 = pid3; 
		this.img = img; 
		this.status = status; 
		this.seq = seq; 
		this.templateId = templateId; 
		this.schoolId = schoolId; 
		this.addTime = addTime; 
		this.editTime = editTime; 
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	} 
	public String getIlevel() {
		return ilevel;
	}

	public void setIlevel(String ilevel) {
		this.ilevel = ilevel;
	} 
	public Long getPid1() {
		return pid1;
	}

	public void setPid1(Long pid1) {
		this.pid1 = pid1;
	} 
	public Long getPid2() {
		return pid2;
	}

	public void setPid2(Long pid2) {
		this.pid2 = pid2;
	} 
	public Long getPid3() {
		return pid3;
	}

	public void setPid3(Long pid3) {
		this.pid3 = pid3;
	} 
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	} 
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	} 
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	} 
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getUsersName() {
        return usersName;
    }

    public String getDiyTime() {
        return diyTime;
    }

    public void setDiyTime(String diyTime) {
        this.diyTime = diyTime;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String[] getUserIdArr() {
        return userIdArr;
    }

    public void setUserIdArr(String[] userIdArr) {
        this.userIdArr = userIdArr;
    }

}
