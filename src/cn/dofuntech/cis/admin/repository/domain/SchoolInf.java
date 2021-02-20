package cn.dofuntech.cis.admin.repository.domain;
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * SchoolInf
 */
public class SchoolInf extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	/**
	 * school_name
	 */
	private String schoolName = EMPTY;
	/**
	 * school_bm
	 */
	private String schoolBm = EMPTY;
	/**
	 * president
	 */
	private String president = EMPTY;
	/**
	 * iname
	 */
	private String iname = EMPTY;
	/**
	 * sname
	 */
	private String sname = EMPTY;
	/**
	 * contacts_name
	 */
	private String contactsName = EMPTY;
	/**
	 * contacts_mobile
	 */
	private String contactsMobile = EMPTY;
	/**
	 * email
	 */
	private String email = EMPTY;
	/**
	 * fix_phone
	 */
	private String fixPhone = EMPTY;
	/**
	 * main_address
	 */
	private String mainAddress = EMPTY;
	/**
	 * detail_address
	 */
	private String detailAddress = EMPTY;
	/**
	 * logo
	 */
	private String logo = EMPTY;
	/**
	 * business_license
	 */
	private String businessLicense = EMPTY;
	/**
	 * id_card
	 */
	private String idCard = EMPTY;
	/**
	 * category
	 */
	private String category = EMPTY;
	/**
	 * readme
	 */
	private String readme = EMPTY;
	/**
	 * status
	 */
	private String status = EMPTY;
	/**
	 * add_time
	 */
	private Timestamp addTime;
	/**
	 * edit_time
	 */
	private Timestamp editTime;
	
	public SchoolInf() {
		super();
	}
	
	public SchoolInf(Long id, String schoolName, String schoolBm, String president, String iname, String sname, String contactsName, String contactsMobile, String email, String fixPhone, String mainAddress, String detailAddress, String logo, String businessLicense, String idCard, String category, String readme, String status, Timestamp addTime, Timestamp editTime) {
		super();
		this.id = id; 
		this.schoolName = schoolName; 
		this.schoolBm = schoolBm; 
		this.president = president; 
		this.iname = iname; 
		this.sname = sname; 
		this.contactsName = contactsName; 
		this.contactsMobile = contactsMobile; 
		this.email = email; 
		this.fixPhone = fixPhone; 
		this.mainAddress = mainAddress; 
		this.detailAddress = detailAddress; 
		this.logo = logo; 
		this.businessLicense = businessLicense; 
		this.idCard = idCard; 
		this.category = category; 
		this.readme = readme; 
		this.status = status; 
		this.addTime = addTime; 
		this.editTime = editTime; 
	}
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	} 
	public String getSchoolBm() {
		return schoolBm;
	}

	public void setSchoolBm(String schoolBm) {
		this.schoolBm = schoolBm;
	} 
	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	} 
	public String getIname() {
		return iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	} 
	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	} 
	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	} 
	public String getContactsMobile() {
		return contactsMobile;
	}

	public void setContactsMobile(String contactsMobile) {
		this.contactsMobile = contactsMobile;
	} 
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	} 
	public String getFixPhone() {
		return fixPhone;
	}

	public void setFixPhone(String fixPhone) {
		this.fixPhone = fixPhone;
	} 
	public String getMainAddress() {
		return mainAddress;
	}

	public void setMainAddress(String mainAddress) {
		this.mainAddress = mainAddress;
	} 
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	} 
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	} 
	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	} 
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	} 
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	} 
	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	} 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}
