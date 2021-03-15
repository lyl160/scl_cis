package cn.dofuntech.cis.admin.repository.domain.vo;

/**
 * DynamicAttr
 */
public class TeachersClockInfoVo {

    /**
     * 教师ID
     */
    private String userId;

    /**
     * 值班日期
     */
    private String dutyDate;

    /**
     * 地点
     */
    private String place;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类+时间
     */
    private String titleDesc;

    /**
     * 消息ID
     */
    private Long messageId;

    private Integer type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleDesc() {
        return titleDesc;
    }

    public void setTitleDesc(String titleDesc) {
        this.titleDesc = titleDesc;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
