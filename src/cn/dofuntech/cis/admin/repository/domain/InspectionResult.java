package cn.dofuntech.cis.admin.repository.domain;

import java.sql.Timestamp;
import java.util.List;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * InspectionResult
 */
public class InspectionResult extends DefaultValue {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * logs_id
     */
    private Long logsId = LONG_EMPTY;
    /**
     * attr_id
     */
    private Long attrId = LONG_EMPTY;
    /**
     * attr_name
     */
    private String attrName = EMPTY;
    /**
     * attr_value
     */
    private String attrValue = EMPTY;
    /**
     * add_time
     */
    private Timestamp addTime;
    /**
     * edit_time
     */
    private Timestamp editTime;
    /**
     * item_score
     */
    private String itemScore = EMPTY;
    /**
     * item_code
     */
    private String itemCode = EMPTY;

    /**
     * 新增字段学校id
     */
    private Long schoolId = LONG_EMPTY;
    private Long templateId = LONG_EMPTY;
    private String monday = EMPTY;
    private String sunday = EMPTY;
    private Long clazzId = LONG_EMPTY;
    private String clazz;
    private String num;
    private String[] attrs;
    private String schoolName;
    private float sum;
    private List<InspectionResult> list;
    private String categoryName;
    private String templateName;
    private InspectionLogs inspectionLogs;
    private DynamicAttr dynamicAttr;
    private String dictName;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<InspectionResult> getList() {
        return list;
    }

    public void setList(List<InspectionResult> list) {
        this.list = list;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String[] getAttrs() {
        return attrs;
    }

    public void setAttrs(String[] attrs) {
        this.attrs = attrs;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Long getClazzId() {
        return clazzId;
    }

    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public InspectionResult() {
        super();
    }

    public InspectionResult(Long id, Long logsId, Long attrId, String attrName, String attrValue, Timestamp addTime, Timestamp editTime) {
        super();
        this.id = id;
        this.logsId = logsId;
        this.attrId = attrId;
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.addTime = addTime;
        this.editTime = editTime;
    }

    public Long getLogsId() {
        return logsId;
    }

    public void setLogsId(Long logsId) {
        this.logsId = logsId;
    }

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public String getItemScore() {
        return itemScore;
    }

    public void setItemScore(String itemScore) {
        this.itemScore = itemScore;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public InspectionLogs getInspectionLogs() {
        return inspectionLogs;
    }

    public void setInspectionLogs(InspectionLogs inspectionLogs) {
        this.inspectionLogs = inspectionLogs;
    }

    public DynamicAttr getDynamicAttr() {
        return dynamicAttr;
    }

    public void setDynamicAttr(DynamicAttr dynamicAttr) {
        this.dynamicAttr = dynamicAttr;
    }


    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
}
