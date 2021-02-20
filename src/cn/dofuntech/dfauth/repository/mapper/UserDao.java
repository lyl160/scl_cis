package cn.dofuntech.dfauth.repository.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.dofuntech.core.mybatis.DfBaseDao;
import cn.dofuntech.dfauth.bean.UserInf;


/**
 * 用户信息 数据库操作接口
 * 
 * @author luokai
 * 
 */
@Repository
public interface UserDao extends DfBaseDao<UserInf, Exception> {
	/**
	 * 分页查询用户信息
	 * 
	 * @param user
	 * @param start
	 * @param end
	 * @return
	 */
	public List<UserInf> selectPageList(Map<String, Object> con) throws Exception;

	/**
	 * 根据ID列表更新用户状态
	 * 
	 * @param ids
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int updateUsersStatus(Map<String, Object> con) throws Exception;

	/**
	 * 根据ID列表删除用户
	 * 
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int deleteUsers(Map<String, Object> con) throws Exception;

	public int updateUserPwd(Map<String, Object> con) throws Exception;

	/**
	 * 根据用户条件获取用户总数
	 * 
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	public int countByMap(Map<String, Object> parameters) throws Exception;
	/**
	 * 更新代理商密码
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public int updateAgentUserPwd(Map<String, Object> con) throws Exception;

	/**
	 * 验证代理商登录名是否重复
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int countLonEntity(UserInf user) throws Exception;

	public UserInf queryMap(Map<String, Object> parameters);
	
	
	public List<UserInf> queryAll(Map<String,Object> map);
	
	
	public UserInf queryrole(UserInf user);
	
}
