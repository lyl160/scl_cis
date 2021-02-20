package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import java.util.Arrays;

import cn.dofuntech.core.entity.DefaultValue;

/**
 * DynamicAttr
 */
public class DynamicAttr extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * template_id
	 */
	private Long templateId = LONG_EMPTY;
	/**
	 * name
	 */
	private String name = EMPTY;
	/**
	 * type
	 */
	private String type = EMPTY;
	/**
	 * attr_option
	 */
	private String attrOption = EMPTY;
	/**
	 * seq 2级catagory分类 id
	 */
	private Long seq = LONG_EMPTY;
	/**
	 * required
	 */
	private String required = EMPTY;
	/**
	 * school_id
	 */
	private Long schoolId = LONG_EMPTY;
	/**
	 * add_time
	 */
	private Timestamp addTime;
	/**
	 * edit_time
	 */
	private Timestamp editTime;
	/**
	 * score
	 */
	private String score = EMPTY;
	/**
	 * item_code
	 */
	private String itemCode = EMPTY;
	/**
	 * u_ids
	 */
	private String uids;

	//新增字段
	
	@Override
	public String toString() {
		return "DynamicAttr [templateId=" + templateId + ", name=" + name + ", type=" + type + ", attrOption="
				+ attrOption + ", seq=" + seq + ", required=" + required + ", schoolId=" + schoolId + ", addTime="
				+ addTime + ", editTime=" + editTime + ", score=" + score + ", itemCode=" + itemCode + ", attrOptions="
				+ Arrays.toString(attrOptions) + ", scores=" + Arrays.toString(scores) + ", dname=" + dname + ", uids=" + uids + "]";
	}

	private String [] attrOptions; //属性值数组
	
	private String [] scores;//属性分数数组

	private String [] uidArr;//用户id数组

	private Long did;//项目代码id

	private String dname;//项目代码名称

	private String tname;//模板名称

	private String clazz;//班级名称


    public String getUids() {
        return uids;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String[] getAttrOptions() {
		return attrOptions;
	}

	public void setAttrOptions(String[] attrOptions) {
		this.attrOptions = attrOptions;
	}

	public String[] getScores() {
		return scores;
	}

	public void setScores(String[] scores) {
		this.scores = scores;
	}

	public DynamicAttr() {
		super();
	}
	
	public DynamicAttr(Long id, Long templateId, String name, String type, String attrOption, Long seq, String required, Long schoolId, Timestamp addTime, Timestamp editTime) {
		super();
		this.id = id; 
		this.templateId = templateId; 
		this.name = name; 
		this.type = type; 
		this.attrOption = attrOption; 
		this.seq = seq; 
		this.required = required; 
		this.schoolId = schoolId; 
		this.addTime = addTime; 
		this.editTime = editTime; 
	}
	
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	} 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 
	public String getAttrOption() {
		return attrOption;
	}

	public void setAttrOption(String attrOption) {
		this.attrOption = attrOption;
	} 
	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	} 
	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	} 
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

    public String[] getUidArr() {
        return uidArr;
    }

    public void setUidArr(String[] uidArr) {
        this.uidArr = uidArr;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
