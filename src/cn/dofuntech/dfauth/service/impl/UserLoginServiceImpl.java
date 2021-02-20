package cn.dofuntech.dfauth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.dfauth.bean.OrgInf;
import cn.dofuntech.dfauth.bean.RoleInf;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.constants.Constant;
import cn.dofuntech.dfauth.controller.MenuController;
import cn.dofuntech.dfauth.repository.mapper.UserDao;
import cn.dofuntech.dfauth.service.OrgService;
import cn.dofuntech.dfauth.service.RoleService;
import cn.dofuntech.dfauth.service.UserLoginService;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.dfauth.service.exception.UserLoginException;
import cn.dofuntech.dfauth.util.APIServiceImpl;
import cn.dofuntech.dfauth.util.ShieldSyncApp;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 用户登陆接口实现类</br> 用户登陆效验。
 * 
 * @author luokai
 * 
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
	private static Logger log = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private RoleService roleService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ShieldSyncApp shieldSyncApp;


	public UAI logIn(UserInf user) throws UserLoginException, Exception {
		UserInf userInf = null;
		UAI uai = null;
		String roleId = "", roleName = "";
		userInf = this.login2(user);
		if (userInf.getUserStatus() == 1) {
			throw new UserLoginException("用户已被禁用，请联系管理员！");
		}
		List<RoleInf> list = roleService.getRoleInfByUser(userInf.getId());
		for (RoleInf ri : list) {
			roleId = roleId + ri.getRoleId() + "|";
			roleName = roleName + ri.getRoleName() + " ";
		}
		// roleName = roleName.substring(0, roleName.lastIndexOf(","));
        if (roleId.length() > 0) {
            roleId = roleId.substring(0, roleId.length()-1);
        }
		log.info("USERINF={}", userInf);
		uai = new UAI(userInf.getId(), userInf.getUserId(),
				userInf.getUserName(), userInf.getUserRandom(),
				userInf.getOrgId(), roleId, roleName, userInf.getOrgName(),
				"",userInf.getAgentId());
		uai.setSysId(userInf.getSysId());
		log.info("UAI={}", uai);
		return uai;
	}
	
	public UserInf login2(UserInf user) throws UserLoginException, Exception {
		UserInf userInf = null;
		// 用户名密码校验
		if (user.getUserId() == null || "".equals(user.getUserId())) {
			throw new UserLoginException("用户名不能为空！");
		}else if (user.getOrgId() == null || "".equals(user.getOrgId())) {
			throw new UserLoginException("机构号不能为空！");
		} else if (user.getSysId() == null || "".equals(user.getSysId())) {
			throw new UserLoginException("系统模块ID不能为空！");
		}
		try {
			userInf = userService.getEntity(new UserInf(user.getUserId(),
					user.getUserPwd(), user.getOrgId(),user.getSysId(),user.getAgentId(),0));
		} catch (Exception e) {
			throw new UserLoginException("用户登陆异常！", e);
		}
		if (userInf == null) {
			throw new UserLoginException("用户名或密码错误！");
		} else if (userInf.getUserStatus() == 1) {
			throw new UserLoginException("用户已禁用！");
		}
		String random = String
				.valueOf((int) ((Math.random() * 9 + 1) * 100000));
		int num = modifyRandom(new UserInf(userInf.getId(), random,user.getLastLoginTime(),user.getLastLoginIp()));
		if (num < 0) {
			throw new UserLoginException("用户登陆异常！");
		}
		OrgInf org = orgService.getEntity(new OrgInf(userInf.getOrgId()));
		if (org == null || org.getOrgStatus().intValue() == 1) {
			throw new UserLoginException("用户所属机构状态异常！");
		}
		userInf.setOrgName(org.getOrgName());
		return userInf;
	}
	
	private int modifyRandom(UserInf userInf) throws Exception {
		int rv = 0;
		try {
			UserInf user = userService.getEntity(new UserInf(userInf.getId()));
			userInf.setLnum(user.getLnum()+1);
			rv = userService.modifyEntity(userInf);
		} catch (Exception e) {
			log.error("更新用户随机码异常！", e);
			throw new Exception("更新用户随机码异常！");
		}
		return rv;
	}
	
	
	
	@Override
	public ReturnMsg saveUser(String sid)  throws Exception {
		ReturnMsg returnMsg = new ReturnMsg();
		APIServiceImpl apiser = new APIServiceImpl();
		//更新人员信息
		String message = shieldSyncApp.listUserByRoleInSchool(sid).toJSONString();
		String agentId = "1";
		switch (sid) {
		case Constant.SchoolId.GJ:
			agentId = "1";
			break;
			
		case Constant.SchoolId.YG:
			agentId = "2";
			break;
			
		case Constant.SchoolId.CBD:
			agentId = "3";
			break;
		default:
			break;
		}
		Map<String,Object> array = Jacksons.me().json2Map(message);	
		List<Map<String, Object>> jsonarray = (List<Map<String, Object>>)array.get("data");
		for(int i=0;i<jsonarray.size();i++){
			Map<String,Object> json = jsonarray.get(i);
			Map<String, Object> parameters = new HashMap();
			String userid = MapUtils.getString(json,"loginName");
			parameters.put("userId", userid);
			UserInf ui = userDao.queryMap(parameters);
			if(ui !=null ){
				ui.setUserPwd("111111");
				ui.setUserRandom("000000");
				ui.setUserStatus(0);
				ui.setLnum(0);
				ui.setLastLoginIp("127.0.0.1");
				ui.setLastLoginTime("00000000000000");
				ui.setAgentId(agentId);
				ui.setOrgId("000000001");
				ui.setUserId(userid);//登陆名称
				ui.setUserName(MapUtils.getString(json,"userName"));//用户名
				ui.setEmail(MapUtils.getString(json,"email")); //邮箱
				ui.setPhone(MapUtils.getString(json,"phone"));//手机号
				userDao.updateEntity(ui);
				
			}else{
				UserInf user = new UserInf();
				user.setUserPwd("111111");
				user.setUserRandom("000000");
				user.setUserStatus(0);
				user.setLnum(0);
				user.setLastLoginIp("127.0.0.1");
				user.setLastLoginTime("00000000000000");
				user.setAgentId(agentId);
				user.setOrgId("000000001");
				user.setSysId("0001");
				user.setUserId(userid);//登陆名称
				user.setUserName(MapUtils.getString(json,"userName"));//用户名
				user.setEmail(MapUtils.getString(json,"email")); //邮箱
				user.setPhone(MapUtils.getString(json,"phone"));//手机号
				userDao.insertEntity(user);
			}
			
		}
		
		returnMsg.setSuccess();
		return returnMsg;
	}

}
