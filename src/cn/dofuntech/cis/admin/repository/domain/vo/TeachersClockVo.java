package cn.dofuntech.cis.admin.repository.domain.vo;

/**
 * DynamicAttr
 */
public class TeachersClockVo {

    /**
     * 教师ID
     */
    private String userId;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 总共打卡次数
     */
    private Integer totalCount;

    /**
     * 实际打卡次数
     */
    private Integer clockCount;

    /**
     * 未打卡次数
     */
    private Integer unClockCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getClockCount() {
        return clockCount;
    }

    public void setClockCount(Integer clockCount) {
        this.clockCount = clockCount;
    }

    public Integer getUnClockCount() {
        return unClockCount;
    }

    public void setUnClockCount(Integer unClockCount) {
        this.unClockCount = unClockCount;
    }
}
