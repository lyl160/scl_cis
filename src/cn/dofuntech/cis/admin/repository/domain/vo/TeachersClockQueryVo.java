package cn.dofuntech.cis.admin.repository.domain.vo;

/**
 * DynamicAttr
 */
public class TeachersClockQueryVo {

    private String userId;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 截止日期
     */
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
