package cn.dofuntech.dfauth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.bean.UserRoleRelInf;
import cn.dofuntech.dfauth.constants.Constant;
import cn.dofuntech.dfauth.repository.mapper.UserDao;
import cn.dofuntech.dfauth.repository.mapper.UserRoleRelDao;
import cn.dofuntech.dfauth.service.MenuService;
import cn.dofuntech.dfauth.service.RoleService;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.dfauth.util.UAI;

@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleRelDao userRoleDao;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;

	public UserInf getEntity(UserInf entity) throws Exception {
		return userDao.selectEntity(entity);
	}

	public List<UserInf> getList(UserInf entity) throws Exception {
		return userDao.selectList(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int addEntity(UserInf entity) throws Exception {
		int rv = 0;
		if(entity.getUserPwd()==null||entity.getUserPwd().length()<=0){
			entity.setUserPwd("111111");
		}
		entity.setUserPwd(entity.getUserPwd());
		entity.setUserRandom("000000");
		entity.setUserStatus(0);
		entity.setLnum(0);
		entity.setLastLoginIp("127.0.0.1");
		entity.setLastLoginTime("00000000000000");
		
		if(entity.getAgentId() == null || entity.getAgentId().equals("")){
			entity.setAgentId(Constant.SYS_AGENT_ID);
		}
		if(entity.getOrgId() == null || entity.getOrgId().equals("")){
			entity.setOrgId(Constant.TOP_ORG_ID);
		}
		
		rv = userDao.insertEntity(entity);
		String roleId[] = null;
		try{
		 roleId = entity.getRoleId().split("\\|");
		}catch(Exception e){
			throw new Exception("分割角色id出错了.",e);
		}
		for (String rid : roleId) {
			userRoleDao.insertRel(new UserRoleRelInf(rid, Integer.valueOf(entity.getUserId())));
			//todo 待处理
		}
		return rv;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int addList(List<UserInf> list) throws Exception {
		return userDao.insertList(list);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int modifyEntity(UserInf entity) throws Exception {
		return userDao.updateEntity(entity);
	}



	public int removeEntity(UserInf entity) {
		return 0;
	}


	public int countEntity(UserInf entity) throws Exception {
		int count = userDao.countEntity(entity);
		log.debug("查询当前用户总数:{}",count);
		return count;
	}

	/**
	 * 根据用户条件获取用户总数
	 * 
	 * @param Map
	 * @return
	 * @throws Exception
	 */
	public int countByMap(Map<String, Object> parameters) throws Exception {
		return userDao.countByMap(parameters);
	}

	public List<UserInf> getListPage(UserInf user,  String currentOrgId) throws Exception {
		//oracle分页
		//int start = (pageNum - 1) * numPerPage + 1;
		//mysql分页
		
		Map<String, Object> con = user.toMap();
		
		con.put("currentOrgId", currentOrgId);
		//con.put("currentOrgId", currentOrgId);
		return userDao.selectPageList(con);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int modifyUsersStatus(String ids, int status) throws Exception {

		Map<String, Object> con = new HashMap<String, Object>(2);
		con.put("userStatus", status);
		con.put("ids", ids);
		log.debug(ids);
		int num = 0;
		try {
			num = userDao.updateUsersStatus(con);
		} catch (Exception e) {
			log.error("用户状态更新异常！", e);
			throw new Exception("用户状态更新异常！", e);
		}

		return num;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int removeUsers(String ids) throws Exception {
		Map<String, Object> con = new HashMap<String, Object>(1);
		con.put("ids", ids);
		int rv = 0;
		String[] idlist = ids.split(",");
		UserInf ui = userDao.selectEntity(new UserInf(UAI.TOP_USER_ID, UAI.TOP_ORG_ID,null,0));
		for (String it : idlist) {
			if (String.valueOf(ui.getId()).equals(it)) {
				throw new Exception("超级用户【" + UAI.TOP_USER_ID + "】不能被删除！");
			}
		}
		try {
			rv = userDao.deleteUsers(con);
			rv = rv + userRoleDao.deleteUsersRels(con);
		} catch (Exception e) {
			log.error("删除用户异常！", e);
			throw new Exception("删除用户异常！");
		}
		return rv;
	}

	/**
	 * 修改用户
	 * 
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int modifyUserRole(UserInf user, String roleId, String roleIdNew) throws Exception {
		int rv = 0;
		try {
			UserInf ui = userDao.selectEntity(new UserInf(UAI.TOP_USER_ID, UAI.TOP_ORG_ID,null,0));
			if (ui.getId().equals(user.getId())) {
				throw new Exception("超级用户【" + UAI.TOP_USER_ID + "】不能被修改！");
			}

			log.debug(user.toString());
			rv = userDao.updateEntity(user);
			if (user.getRoleId() != null && !user.getRoleId().equals("")) {
				// 修改角色关系
				Map<String, Object> con = new HashMap<String, Object>();
				con.put("ids", user.getId());
				rv = rv + userRoleDao.deleteUsersRels(con);

				// 添加角色关系
				//String[] idsNew = roleIdNew.split("\\|");
				String[] idsNew = new String[]{user.getRoleId()};
				List<UserRoleRelInf> list = new ArrayList<UserRoleRelInf>();
				for (String id : idsNew) {
					list.add(new UserRoleRelInf(id, user.getId()));
				}
				rv = rv + userRoleDao.insertRelList(list);
			}
			if (rv == 0) {
				throw new Exception("修改用户信息失败！");
			}
		} catch (Exception e) {
			log.error("修改用户信息失败！", e);
			throw new Exception("修改用户信息失败！", e);
		}

		return rv;

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int modifyUserPwd(String ids, String npwd, String opwd) throws Exception {

		int rv = 0;
		String[] uids = ids.split(",");
		try {// TdExprSecurity.MD5STR("111111")
			if (npwd == null) {
				npwd = ("111111");
			} 
			for (String id : uids) {
				Map<String, Object> con = new HashMap<String, Object>();
				con.put("ids", id);
				con.put("npwd", npwd);
				con.put("random", String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));
				con.put("opwd", opwd);
				rv = userDao.updateUserPwd(con);
			}
		} catch (Exception e) {
			log.error("密码修改异常！", e);
			throw new Exception("密码修改异常！", e);
		}
		if (rv <= 0) {
			throw new Exception("密码修改失败,旧密码输入有误！");
		}

		return rv;
	}

	public Integer getCount(UserInf entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserInf> getListPage(UserInf entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int initAgentAdmin(String agentId, String logonName) throws Exception {
		/*
		//建立角色
		String roleId = seqNoService.getSeqNoNew("ROLE_ID", "4", "0");
		
		RoleInf role = new RoleInf();
		role.setRoleId(roleId);
		role.setRoleName("代理商超级管理员");
		role.setRoleDesc("代理商系统最高角色权限");
		role.setOrgId(Constant.TOP_ORG_ID);//设置默认机构
		role.setFlag("0");
		role.setSysId(Constant.SYS_TDPRM);
		role.setRoleStatus("0");  //正常
		roleService.addEntity(role);
		
		//建立角色菜单关系
		String[] mids = menuService.queryMenuIds(Constant.SYS_TDPRM);
		List<RoleMenuButtonRelInf> rmlist = new ArrayList<RoleMenuButtonRelInf>();
		for(String mid:mids){
			rmlist.add(new RoleMenuButtonRelInf(roleId,mid));
		}
		menuService.addRoleMenuButtonRelInf(rmlist);
		*/
		//建立用户
		int rt= addEntity(new UserInf(agentId,logonName,Constant.SYS_TDPRM,"代理商超级管理员用户",Constant.SYS_AGENT_ADMIN_ROLE));
		
		return rt;
	}

	@Override @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int resetAgentAdminPwd(String agentId) throws Exception {
		if(agentId == null || agentId.equals("")){
			throw new Exception("代理商ID不能为空！");
		}
		Map<String, Object> con = new HashMap<String, Object>();
//		con.put("userId", "agentadmin");
		con.put("pwd", "111111");
		con.put("random", "000000");
		con.put("agentId", agentId);
		int rt = userDao.updateAgentUserPwd(con);
		return rt;
	}

	@Override
	public List<UserInf> queryAll(Map<String,Object> map) throws Exception {
		// TODO Auto-generated method stub
		
		return userDao.queryAll(map);
	}

	@Override
	public UserInf queryrole(UserInf user) {
		// TODO Auto-generated method stub
		return userDao.queryrole(user);
	}

	@Override
	public UserInf queryMap(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return userDao.queryMap(map);
	}


	
}
