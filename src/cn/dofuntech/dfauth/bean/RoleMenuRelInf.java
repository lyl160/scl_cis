package cn.dofuntech.dfauth.bean;

import java.io.Serializable;


/**
 * 角色菜单关联信息（N:N）
 * @author luokai
 *
 */
public class RoleMenuRelInf extends BaseBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleMenuRelInf() {
	}
	public RoleMenuRelInf(String roleId, String menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}
	/**
	 * 角色ID
	 */
	private String roleId;
	
	/**
	 * 菜单ID
	 */
	private String menuId;

	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
}
