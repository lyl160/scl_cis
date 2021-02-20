package cn.dofuntech.cis.admin.repository.domain;

import java.sql.Timestamp;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * Place
 */
public class Place extends DefaultValue {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * place_name
     */
    private String placeName = EMPTY;
    /**
     * default_flag
     */
    private String defaultFlag = EMPTY;
    /**
     * add_time
     */
    private Timestamp addTime;
    /**
     * edit_time
     */
    private Timestamp editTime;
    /**
     * school_id
     */
    private Long schoolId = LONG_EMPTY;
    /**
     * user_id
     */
    private Long userId = LONG_EMPTY;
    private String schoolname;//学校名称

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public Place() {
        super();
    }

    public Place(Long id, Long userId, String placeName, String defaultFlag, Timestamp addTime, Timestamp editTime, Long schoolId) {
        super();
        this.id = id;
        this.placeName = placeName;
        this.defaultFlag = defaultFlag;
        this.addTime = addTime;
        this.editTime = editTime;
        this.schoolId = schoolId;
        this.userId = userId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
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

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
}
