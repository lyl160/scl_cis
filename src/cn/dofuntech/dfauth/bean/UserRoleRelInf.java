package cn.dofuntech.dfauth.bean;

import java.io.Serializable;

/**
 * 用户角色关联实体，对应AUTH_USER_ROLE_REL_INF
 * @author luokai
 *
 */
public class UserRoleRelInf extends BaseBean implements Serializable {

	public UserRoleRelInf(String roleId, Integer userId) {
		this.roleId = roleId;
		this.userId = userId;
	}
	public UserRoleRelInf() {
	}
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7005300613218792375L;


	private String roleId;
	private Integer userId;
	
	private RoleInf role ;
	private String agentId;
	
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public RoleInf getRole() {
		return role;
	}
	public void setRole(RoleInf role) {
		this.role = role;
	}

}
