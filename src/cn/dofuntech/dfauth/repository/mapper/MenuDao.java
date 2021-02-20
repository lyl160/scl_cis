package cn.dofuntech.dfauth.repository.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.dofuntech.core.mybatis.DfBaseDao;
import cn.dofuntech.dfauth.bean.MenuInf;
import cn.dofuntech.dfauth.bean.RoleMenuRelInf;


/**
 * 菜单信息 数据库操作接口
 * 
 * @author luokai
 * 
 */
@Repository
public interface MenuDao extends DfBaseDao<MenuInf, Exception> {
	/**
	 * 添加菜单角色关系
	 * @param roleMenuButtonRelInf
	 * @return
	 */
	public int insertMenuRoleRel(RoleMenuRelInf roleMenuButtonRelInf)   throws Exception;
	/**
	 * 删除菜单角色关系
	 * @param roleMenuButtonRelInf
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuRoleRel(@Param(value="menuId") String menuId) throws Exception;

	List<MenuInf> selectAuthMenu(Integer u_Id);

	List<MenuInf> findListPage(HashMap<String, Object> cond);
	
	public int deleteMenuList(int[] menuId) throws Exception;
	
	public String selectMenuId(String childId) throws Exception;
	
	public int deleteMenu(String menuId) throws Exception;
	
	public List<MenuInf> selectMenuByUid(Map<String,Object> paramMap) throws Exception;
	
	public List<MenuInf> selectMenuByRid(String roleId) throws Exception;
	
	public List<MenuInf>  selectMenuByUidRid(HashMap<String, Object> map) throws Exception;

	public List<MenuInf> selectMenuByAuditId(String auditId)throws Exception;

	public List<MenuInf> selectCheckMenuByAuditId(String auditId);
	/**
	 * 根据用户id查询url
	 * @param u_Id
	 * @return
	 */
	public List<MenuInf> selectMenuUrlByUid(HashMap<String, Object> map);
	
	/**
	 * 修改菜单状态
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int updateMenuStatus(Map<String, Object> con) throws Exception;

}
