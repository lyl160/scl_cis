package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * ClazzInf
 */
public class ClazzInf extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * clazz
	 */
	private String clazz = EMPTY;
	/**
	 * grade
	 */
	private String grade = EMPTY;
	/**
	 * header_teacher
	 */
	private String headerTeacher = EMPTY;
	/**
	 * total_peo
	 */
	private String totalPeo = EMPTY;
	/**
	 * logo
	 */
	private String logo = EMPTY;
	/**
	 * slogan
	 */
	private String slogan = EMPTY;
	/**
	 * readme
	 */
	private String readme = EMPTY;
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
	/**
	 * team_id
	 */
	private Long teamId = LONG_EMPTY;
	
	//新增字段
	private String scoreNum;//总得分
	
	private int es;//1、升 2降 0不变
	
	
	private String status; //是否可查
	private String hid;      //历史id
	private String upgrade;  //是否可以升级
	private String h_clazz;   //原来班级
	private String h_grade;    //原来年级
	private String teamName;
    private Long num1; //当天提交的校务巡查记录
    private Long num2;//当天提交的后勤巡查记录
    private String category2;//二级分类id

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getH_clazz() {
		return h_clazz;
	}

	public void setH_clazz(String h_clazz) {
		this.h_clazz = h_clazz;
	}

	public String getH_grade() {
		return h_grade;
	}

	public void setH_grade(String h_grade) {
		this.h_grade = h_grade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String gethid() {
		return hid;
	}

	public void sethid(String hid) {
		this.hid = hid;
	}

	public String getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}

	public int getEs() {
		return es;
	}

	public void setEs(int es) {
		this.es = es;
	}

	public String getScoreNum() {
		return scoreNum;
	}

	public void setScoreNum(String scoreNum) {
		this.scoreNum = scoreNum;
	}

	public ClazzInf() {
		super();
	}
	
	public ClazzInf(Long id, String clazz, String grade, String headerTeacher, String totalPeo, String logo, String slogan, String readme, Long schoolId, Timestamp addTime, Timestamp editTime) {
		super();
		this.id = id; 
		this.clazz = clazz; 
		this.grade = grade; 
		this.headerTeacher = headerTeacher; 
		this.totalPeo = totalPeo; 
		this.logo = logo; 
		this.slogan = slogan; 
		this.readme = readme; 
		this.schoolId = schoolId; 
		this.addTime = addTime; 
		this.editTime = editTime; 
	}
	
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	} 
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	} 
	public String getHeaderTeacher() {
		return headerTeacher;
	}

	public void setHeaderTeacher(String headerTeacher) {
		this.headerTeacher = headerTeacher;
	} 
	public String getTotalPeo() {
		return totalPeo;
	}

	public void setTotalPeo(String totalPeo) {
		this.totalPeo = totalPeo;
	} 
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	} 
	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	} 
	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
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

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

    public Long getNum1() {
        return num1;
    }

    public void setNum1(Long num1) {
        this.num1 = num1;
    }

    public Long getNum2() {
        return num2;
    }

    public void setNum2(Long num2) {
        this.num2 = num2;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }
}
