package cn.dofuntech.dfauth.bean;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * Dict
 */
public class Dict extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * dict_id
	 */
	private String dictId = EMPTY;
	/**
	 * prent_id
	 */
	private String parentId = EMPTY;
	/**
	 * dict_code
	 */
	private String dictCode = EMPTY;
	/**
	 * dict_value
	 */
	private String dictValue = EMPTY;
	/**
	 * dict_name
	 */
	private String dictName = EMPTY;
	/**
	 * seq_num
	 */
	private String seqNum = EMPTY;
	/**
	 * 备注
	 */
	private String hlp = EMPTY;
	/**
	 * dict_level
	 */
	private String dictLevel = EMPTY;
	/**
	 * status
	 */
	private String status = EMPTY;
	/**
	 * addtime
	 */
	private String addtime;
	/**
	 * edittime
	 */
	private String edittime;
	
	public Dict() {
		super();
	}
	
	public Dict(String dictId, String parentId, String dictCode, String dictValue, String dictName, String seqNum, String hlp, String dictLevel, String status, String addtime, String edittime) {
		super();
		this.dictId = dictId; 
		this.parentId = parentId; 
		this.dictCode = dictCode; 
		this.dictValue = dictValue; 
		this.dictName = dictName; 
		this.seqNum = seqNum; 
		this.hlp = hlp;
		this.dictLevel = dictLevel; 
		this.status = status; 
		this.addtime = addtime; 
		this.edittime = edittime; 
	}
	
	public Dict(String dictId, String dictCode, String dictName) {
		this.dictId = dictId;
		this.dictCode = dictCode;
		this.dictName = dictName;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	} 
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	} 
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	} 
	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	} 
	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	} 
	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	} 
	public String getDictLevel() {
		return dictLevel;
	}

	public void setDictLevel(String dictLevel) {
		this.dictLevel = dictLevel;
	} 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	} 
	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public String getHlp() {
		return hlp;
	}

	public void setHlp(String hlp) {
		this.hlp = hlp;
	}

}
