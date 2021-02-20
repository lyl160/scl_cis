package cn.dofuntech.dfauth.bean;


public class Attributes{

	String url;
	
	/**
	 * 菜单类型
	 */
	private Integer menuType;
	
	/**
	 * 菜单状态（0：禁用 1：启用）
	 */
	private Integer menuStatus;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(Integer menuStatus) {
		this.menuStatus = menuStatus;
	}
	
}
