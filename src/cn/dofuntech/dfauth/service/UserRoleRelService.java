package cn.dofuntech.dfauth.service;

import cn.dofuntech.dfauth.bean.UserRoleRelInf;

import java.util.List;
import java.util.Map;


/**
 * 用户、角色关系接口
 * @author luokai
 *
 */
public interface UserRoleRelService extends BaseService<UserRoleRelInf, Exception> {

	/**
	 * 批量删除用户角色关系
	 *
	 * @param list 用户角色关系List
	 * @return
	 * @throws Exception
	 */
	public int removeRelList(List<UserRoleRelInf> list) throws Exception;

	public List<UserRoleRelInf> query(Map<String, Object> map) throws Exception;

	public List<UserRoleRelInf> queryManager(Map<String, Object> map) throws Exception;
}
