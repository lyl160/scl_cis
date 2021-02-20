package cn.dofuntech.cis.admin.repository.domain;

import java.sql.Timestamp;
import java.util.List;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * InspectionLogs
 */
public class InspectionLogs extends DefaultValue {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * category1
     */
    private Long category1 = LONG_EMPTY;
    /**
     * category2
     */
    private Long category2 = LONG_EMPTY;
    /**
     * category3 未发现用处 暂时用于dict id 1教师 2	学生 3管理
     */
    private Long category3 = LONG_EMPTY;
    /**
     * clazz_id
     */
    private Long clazzId = LONG_EMPTY;
    /**
     * clazz
     */
    private String clazz = EMPTY;

    private String teacherId = EMPTY;

    private String teacherName = EMPTY;
    /**
     * grade
     */
    private String grade = EMPTY;
    /**
     * template_id
     */
    private Long templateId = LONG_EMPTY;
    /**
     * czy_id
     */
    private Long czyId = LONG_EMPTY;
    /**
     * czy
     */
    private String czy = EMPTY;
    /**
     * school_id
     */
    private String schoolId = EMPTY;
    /**
     * add_time
     */
    private Timestamp addTime;
    /**
     * edit_time
     */
    private Timestamp editTime;
    /**
     * imgs
     */
    private String imgs = EMPTY;
    /**
     * team_id
     */
    private Long teamId = LONG_EMPTY;
    /**
     * team_name
     */
    private String teamName = EMPTY;
    /**
     * total_score
     */
    private String totalScore = EMPTY;
    /**
     * desc1_
     */
    private String desc1 = EMPTY;
    //新增字段
    private List<InspectionResult> list;//动态属性字段

    private String value;//分类描述

    private String monday;//周一所在的日期
    private String sunday;//周日所在的日期

    private String str;//动态数据

    private String str1;//图片名称


    private String criticism;//批评次数
    private String praise;//表扬
    private String remind;//提醒

    private String day;//定义周一到周五其中一天的时间
    private String remark;

    private String name;//模板名称
    //图片存放集合
    private List<String> listimgs;
    private String schoolName;
    private String teacherStr;


    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<String> getListimgs() {
        return listimgs;
    }

    public void setListimgs(List<String> listimgs) {
        this.listimgs = listimgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCriticism() {
        return criticism;
    }

    public void setCriticism(String criticism) {
        this.criticism = criticism;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<InspectionResult> getList() {
        return list;
    }

    public void setList(List<InspectionResult> list) {
        this.list = list;
    }

    public InspectionLogs() {
        super();
    }

    public InspectionLogs(Long id, Long category1, Long category2, Long category3, Long clazzId, String clazz, String grade, Long templateId, Long czyId, String czy, String schoolId, Timestamp addTime, Timestamp editTime) {
        super();
        this.id = id;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.clazzId = clazzId;
        this.clazz = clazz;
        this.grade = grade;
        this.templateId = templateId;
        this.czyId = czyId;
        this.czy = czy;
        this.schoolId = schoolId;
        this.addTime = addTime;
        this.editTime = editTime;
    }

    public Long getCategory1() {
        return category1;
    }

    public void setCategory1(Long category1) {
        this.category1 = category1;
    }

    public Long getCategory2() {
        return category2;
    }

    public void setCategory2(Long category2) {
        this.category2 = category2;
    }

    public Long getCategory3() {
        return category3;
    }

    public void setCategory3(Long category3) {
        this.category3 = category3;
    }

    public Long getClazzId() {
        return clazzId;
    }

    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCzyId() {
        return czyId;
    }

    public void setCzyId(Long czyId) {
        this.czyId = czyId;
    }

    public String getCzy() {
        return czy;
    }

    public void setCzy(String czy) {
        this.czy = czy;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
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

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getTeacherStr() {
        return teacherStr;
    }

    public void setTeacherStr(String teacherStr) {
        this.teacherStr = teacherStr;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
