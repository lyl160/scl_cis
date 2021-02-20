package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import java.util.List;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * Team
 */
public class Team extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * team_name
	 */
	private String teamName = EMPTY;
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

	/**
	 * 团队--班级一对多
	 */
	private List<ClazzInf> clazzList;
	
	public Team() {
		super();
	}
	
	public Team(Long id, String teamName, Timestamp addtime, Timestamp edittime) {
		super();
		this.id = id; 
		this.teamName = teamName; 
		this.addtime = addtime; 
		this.edittime = edittime; 
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
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

	public List<ClazzInf> getClazzList() {
		return clazzList;
	}

	public void setClazzList(List<ClazzInf> clazzList) {
		this.clazzList = clazzList;
	} 
	
	
}
