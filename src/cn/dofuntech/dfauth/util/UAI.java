package cn.dofuntech.dfauth.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户权限信息,存入Session中。
 * @author luokai
 *
 */
public class UAI {
	
	/**运营系统顶级角色ID*/
	public final static String TOP_ROLE_ID = "1";
	/**代理商系统顶级角色Id*/
	public final static String TOP_AGENT_ROLE_ID = "2";
	/**
	 * 系统最高机构权限
	 */
	public final static String TOP_ORG_ID = "000000001";
	
	/**
	 * 系统超级管理员用户
	 */
	public final static String TOP_USER_ID = "adminauth";
	/**
	 * 系统超级管理员用户编号
	 */
	public final static Integer TOP_USER_NO = 1000;

	//用户信息
	private Integer id;
	private String userId;
	private String userName;
	private String userRandom;
	private String orgId ;
	private String orgName;
	private String roleId;
	private String roleName;
	private String path;
	private String agentId;
	
	private Integer _USER_NO;
	private String  _ROLE_ID;
	private String sysId;
	


	//权限树

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	private Map<String,Boolean> menuAuth = new HashMap<String,Boolean>();

	public UAI() {
		this._USER_NO = TOP_USER_NO;
		this._ROLE_ID = TOP_ROLE_ID;
	}

	public UAI(Integer id, String userId, String userName,String userRandom,String orgId
			,String roleId,String roleName,String orgName,String path,String agentId) {
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.userRandom = userRandom;
		this.orgId = orgId;
		this.roleId = roleId;
		this.roleName = roleName;
		this.orgName = orgName;
		this.path = path;
		this.agentId=agentId;
		this._USER_NO = TOP_USER_NO;
		this._ROLE_ID = TOP_ROLE_ID;
	}
	

	public String getUserRandom() {
		return userRandom;
	}

	public void setUserRandom(String userRandom) {
		this.userRandom = userRandom;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Map<String, Boolean> getMenuAuth() {
		return menuAuth;
	}

	public void setMenuAuth(Map<String, Boolean> menuAuth) {
		this.menuAuth = menuAuth;
	}


	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOrgName() {
		return orgName;
	}

	public Integer get_USER_NO() {
		return _USER_NO;
	}

	public String get_ROLE_ID() {
		return _ROLE_ID;
	}

	@Override
	public String toString() {
		return "UAI [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", userRandom=" + userRandom + ", orgId=" + orgId
				+ ", orgName=" + orgName + ", roleId=" + roleId + ", roleName="
				+ roleName + ", path=" + path + ", _USER_NO=" + _USER_NO
				+ ", _ROLE_ID=" + _ROLE_ID + ", sysId=" + sysId + ", menuAuth="
				+ menuAuth + ",agentId="+agentId+"]";
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	

	
	
	
	
	

}
