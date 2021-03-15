package cn.dofuntech.cis.admin.repository.domain.vo;

/**
 * DynamicAttr
 */
public class TeachersDutyVo {

    /**
     * 教师ID
     */
    private String userId;

    private String userName;

    /**
     * 打卡次数
     */
    private Integer count;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
