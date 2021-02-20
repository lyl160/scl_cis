package cn.dofuntech.cis.admin.repository.domain;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * ClazzInf
 */
public class TeacherClazzRelInf extends DefaultValue {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TeacherClazzRelInf(Integer id, Integer schoolId, String clazz, String userId, String userName) {
        this.id = id + 0L;
        this.schoolId = schoolId + 0L;
        this.clazz = clazz;
        this.userId = userId;
        this.userName = userName;
    }

    public TeacherClazzRelInf(Long schoolId, String clazz, String userId, String userName) {
        this.schoolId = schoolId;
        this.clazz = clazz;
        this.userId = userId;
        this.userName = userName;
    }

    private Long schoolId = LONG_EMPTY;
    /**
     * clazz
     */
    private String clazz = EMPTY;
    /**
     * 用户编号
     */
    private String userId = EMPTY;

    /**
     * 用户名称
     */
    private String userName = EMPTY;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
