package cn.dofuntech.cis.admin.repository.domain;

import java.sql.Timestamp;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * Schedule
 */
public class WorkDay extends DefaultValue{

	 /**
     *
     */
    private static final long serialVersionUID = 1L;

	/**
	 * users
	 */
	private String year = EMPTY;
	/**
	 * duty_date
	 */
	private String month = EMPTY;
	/**
	 * work 工作日字符串
	 */
	private String workday = EMPTY;
    /**
     * holiday 休息日字符串
     */
    private String holiday = EMPTY;
	/**
	 * addtime
	 */
	private Timestamp addtime;
	/**
	 * edittime
	 */
	private Timestamp edittime;

	/**
	 * 学校id
	 */
	private Long schoolId = LONG_EMPTY;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
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
}
