package cn.dofuntech.dfauth.constants;

/**
 * 权限模块常量定义
 * @author luokai
 *
 */
public class Constant {
	/**
	 * 系统初始角色,最高角色权限
	 */
	public final static String TOP_ROLE_ID = "0001";
	/**
	 * 系统最高机构权限
	 */
	public final static String TOP_ORG_ID = "000000001";
	
	/**
	 * 系统超级管理员用户
	 */
	//public final static String TOP_USER_ID = "adminauth";
	/**
	 * 系统超级管理员用户编号
	 */
	//public final static Integer TOP_USER_NO = 1000;
	
	/**
	 * 内置用户
	 */
	public final static String TOPINNER_USER_ID = "subplatformadminauth";
	
	/**
	 * 运营商系统ID
	 */
	public final static String SYS_TDWEB  = "0001";
	/**
	 * 交易系统ID
	 */
	public final static String SYS_TDCCTP = "0003";
	/**
	 * 代理商系统ID
	 */
	public final static String SYS_TDPRM  = "0002";
	
	/**
	 * 系统默认代理商ID
	 */
	public final static String SYS_AGENT_ID  = "2015000000";
	
	/**
	 * 系统默认代理商最高权限角色ID
	 */
	public final static String SYS_AGENT_ADMIN_ROLE  = "0002";
	/**
	 * 平台交易手续费率
	 */
	public final static String SYS_TRAN_RATE  = "0.001";
	
	
	/**学校id（讯飞系统）
	 * @author kai.luo
	 *
	 */
	public interface SchoolId{
		String GJ = "1010000001000022449";
		String YG = "1010000001000022450";
		String CBD = "2300000001000285349";
	}
	
	/**学校id（讯飞系统）
	 * @author kai.luo
	 *
	 */
	public interface RoleConstants{
		String STUDENT = "student";
		String TEACHER = "teacher";
	}

}
