package cn.dofuntech.dfauth.service;

import java.util.List;
import java.util.Map;

import cn.dofuntech.dfauth.bean.UserInf;


/**
 * 用户service层
 * 
 * @author luokai
 * 
 */
public interface UserService extends BaseService<UserInf, Exception> {
	/**
	 * 根据用户条件获取用户总数
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int countEntity(UserInf entity) throws Exception;

	/**
	 * 根据用户条件获取用户总数
	 * 
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	public int countByMap(Map<String, Object> parameters) throws Exception;

	/**
	 * 查询分页列表
	 * 
	 * @param user
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws Exception
	 */
	public List<UserInf> getListPage(UserInf user, String currentOrgId) throws Exception;

	/**
	 * 根据ID列表修改用户状态
	 * 
	 * @param ids
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int modifyUsersStatus(String ids, int status) throws Exception;

	/**
	 * 根据ID列表移除用户
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int removeUsers(String ids) throws Exception;

	public int modifyUserRole(UserInf user, String roleId, String roleIdNew) throws Exception;

	/**
	 * 修改用户密码
	 * 
	 * @param ids
	 *            用户ID
	 * @param npwd
	 *            新密码
	 * @param opwd
	 *            旧密码
	 * @return
	 * @throws Exception
	 */
	public int modifyUserPwd(String ids, String npwd, String opwd) throws Exception;
	
	/**
	 * 添加代理商用户,初始化代理商超级管理员用户。
	 * @param agentId 代理商Id
	 * @param logonName
	 * @return <=0 失败 1成功
	 * @throws Exception
	 */
	public int initAgentAdmin(String agentIds, String logonName) throws Exception;
	
	/**
	 * 重置代理商密码
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public int resetAgentAdminPwd(String agentId) throws Exception;
	
	
	
	
	/**
	 * 查看所有的老师
	 */
	public List<UserInf> queryAll(Map<String,Object> map)throws Exception;
	
	/**
	 * 查询用户角色信息
	 */
	
	public UserInf  queryrole(UserInf user);
	
	/**
	 * 查询用户信息
	 */
	public UserInf queryMap(Map<String,Object> map)throws Exception;
}
