package cn.dofuntech.dfauth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dofuntech.dfauth.bean.UserRoleRelInf;
import cn.dofuntech.dfauth.repository.mapper.UserRoleRelDao;
import cn.dofuntech.dfauth.service.UserRoleRelService;


/**
 * 用户、角色关系实现类
 * 
 * @author luokai
 * 
 */
@Service
public class UserRoleRelServiceImpl implements UserRoleRelService {

	/**
	 * 用户、角色关系数据访问对象
	 */
	@Autowired
	private UserRoleRelDao userRoleRelDao;

	public UserRoleRelInf getEntity(UserRoleRelInf entity) throws Exception {
		return null;
	}

	public List<UserRoleRelInf> getList(UserRoleRelInf entity) {
		return null;
	}

	public int addEntity(UserRoleRelInf entity) {
		return 0;
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int addList(List<UserRoleRelInf> list) throws Exception {
		return userRoleRelDao.insertRelList(list);
	}

	public int modifyEntity(UserRoleRelInf entity) throws Exception {

		return 0;
	}

	public int removeEntity(UserRoleRelInf entity) throws Exception {
		return 0;
	}



	public Integer getCount(UserRoleRelInf entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, Object> getDataPage(UserRoleRelInf entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserRoleRelInf> getListPage(UserRoleRelInf entity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public int removeRelList(List<UserRoleRelInf> list) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserRoleRelInf> query(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return userRoleRelDao.query(map);
	}




}
