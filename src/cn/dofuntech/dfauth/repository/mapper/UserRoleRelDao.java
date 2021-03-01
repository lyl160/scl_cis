package cn.dofuntech.dfauth.repository.mapper;


import cn.dofuntech.core.mybatis.DfBaseDao;
import cn.dofuntech.dfauth.bean.UserRoleRelInf;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 用户角色关系 数据库操作接口
 * 
 * @author luokai
 * 
 */
@Repository
public interface UserRoleRelDao extends DfBaseDao<UserRoleRelInf, Exception> {
	
	/**
	 * 根据用户ID列表删除用户角色关系
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int deleteUsersRels(Map<String, Object> con) throws Exception;

	/**
	 * 根据角色ID列表删除用户角色关系
	 *
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int deleteRolesRels(Map<String, Object> con) throws Exception;

	public List<UserRoleRelInf> query(Map<String, Object> map) throws Exception;

	public List<UserRoleRelInf> queryManager(Map<String, Object> map) throws Exception;

}
