package cn.dofuntech.dfauth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.dofuntech.cis.admin.repository.domain.SchoolInf;
import cn.dofuntech.cis.admin.service.SchoolInfService;
import cn.dofuntech.cis.admin.service.TeacherClazzRelInfService;
import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.dfauth.bean.MenuInf;
import cn.dofuntech.dfauth.bean.OrgInf;
import cn.dofuntech.dfauth.bean.RoleInf;
import cn.dofuntech.dfauth.bean.Tree;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.constants.Constant;
import cn.dofuntech.dfauth.service.MenuService;
import cn.dofuntech.dfauth.service.OrgService;
import cn.dofuntech.dfauth.service.RoleService;
import cn.dofuntech.dfauth.service.UserLoginService;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.dfauth.util.ShieldSyncApp;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 
 * @author luokai
 * 
 */
@Controller
@RequestMapping("/auth/")
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	@Autowired
	OrgService orgService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	private ShieldSyncApp shieldSyncApp;
    @Autowired
    private TeacherClazzRelInfService teacherClazzRelInfService;
    @Autowired
    private SchoolInfService schoolInfService;

    @RequestMapping(value = "foradduser")
	public void AddUserss() {
		ReturnMsg rm = new ReturnMsg();
		System.out.println("sdfsdfsd");
		return;
	}

	@RequestMapping(value = "index/userPwdEditView")
	public String updatePwdView() {
		return "auth/user/userPwdEdit";
	}

	@RequestMapping(value = "userPwdEdit/edit")
	public @ResponseBody ReturnMsg updatePwd(
			@ModelAttribute("UserInf") UserInf user) {
		ReturnMsg msg = new ReturnMsg();

		try {
			if (user.getUserPwd() == null
					|| user.getUserPwd().trim().equals("")) {
				msg.setMsg("201", "原始密码不能为空！");
			} else if (user.getUserNewPwd() == null
					|| user.getUserNewPwd().trim().equals("")) {
				msg.setMsg("202", "新密码不能为空！");
			} else {
				userService.modifyUserPwd(user.getUserId(),
						user.getUserNewPwd(), user.getUserPwd());
				msg.setMsg("200", "密码修改成功,重新登录时生效！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg.setMsg("203", e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "/addView")
	public String addUserView() {
		return "auth/user/userAdd";
	}

	@RequestMapping(value = "userManage/queryUserInfView")
	public String queryUserInfView(@RequestParam("id") Integer id, ModelMap mp) {
		mp.addAttribute("id", id);
		return "auth/user/userInfo";
	}

	@RequestMapping(value = "userInfo/queryUTreeInf")
	public @ResponseBody List<Tree> queryUTreeInf(@RequestParam("id") Integer id) {
		log.debug(String.valueOf(id));
		List<Tree> tree = menuService.getMenuByUid(id);
		return tree;
	}

	/**
	 * 加载用户编辑
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "userManage/editView")
	public String editUserView() {

		return "auth/user/userEdit";
	}

	@RequestMapping(value = "userManage/queryUserById")
	@ResponseBody
	public ReturnMsg queryUserById(@ModelAttribute("UserInf") UserInf user,
			HttpServletRequest request) {

		UserInf ui = null;
		String roleId = "";
		String roleName = "";
		String init = "";
		ReturnMsg msg = new ReturnMsg();
		try {
			ui = userService.getEntity(user);
			if (ui.getOrgId().equals(UAI.TOP_ORG_ID)
					&& ui.getUserId().equals(UAI.TOP_USER_ID)) {
				init = "100";
			}
			List<RoleInf> list = roleService.getRoleInfByUser(ui.getId());
			for (RoleInf ri : list) {
				// roleId = roleId + ri.getRoleId() + "|";
				// roleName = roleName + ri.getRoleName() + " ";
				roleId = ri.getRoleId();
				roleName = ri.getRoleName();
			}
			ui.setRoleId(roleId);
			msg.setMsg("200", "成功！");
		} catch (Exception e) {
			msg.setMsg("201", "用户信息查询异常！");
			log.error("用户信息查询异常！", e);
		}
		msg.setObj(ui);

		return msg;
	}

	@RequestMapping(value = "userManage")
	public String userManage(HttpServletRequest request) {
		// 考虑如何校验权限
		Map<String, String> map = null;
		// try {
		// map = dictService.getOptionTextMapSpace("USERSTATUS");
		// log.info(map.toString());
		// } catch (Exception e) {
		// log.error(e.getMessage(), e);
		// }
		request.setAttribute("DICT_SELECT_MAP", map);
		return "auth/user/userManage";
	}

	@RequestMapping(value = "userManage/query")
	@ResponseBody
	public ReturnMsg queryUser(UserInf user, HttpSession session) {
		int total = 0;
		List<UserInf> list = null;
		try {
			if (user.getRids() != null && !user.getRids().equals("")) {

				user.setRids(user.getRids().replaceAll("\\|", ","));
			}
			UAI uai = (UAI) session.getAttribute("UID");
			Map<String, Object> parameters = user.toMap();
			parameters.put("currentOrgId", uai.getOrgId());
			parameters.put("sysId", uai.getSysId());
			parameters.put("agentId", uai.getAgentId());
			log.debug(parameters.toString());
			user.setSysId(uai.getSysId());
			user.setAgentId(uai.getAgentId());
			total = userService.countByMap(parameters);
			list = userService.getListPage(user, uai.getOrgId()); // HashMap<String,
																	// Object>
																	// result =
																	// new
																	// HashMap<String,
																	// Object>();
			// result.put("records", total);
			// result.put("rows", list);
			log.debug("总记录数:{},数据列表：{}", total, list.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ReturnMsg(total, list);
	}

	@RequestMapping(value = "orgList/queryOrgByDlg")
	public HashMap<String, Object> queryOrgByDlg(
			@ModelAttribute("OrgInf") OrgInf orgInf,
			HttpServletRequest request, HttpSession session) throws Exception {
		UAI uai = (UAI) session.getAttribute("UID");
		String operOrgId = uai.getOrgId();
		int pageNo = Integer.parseInt(request.getParameter("page"));
		int numPerPage = Integer.parseInt(request.getParameter("rows"));
		return orgService.getOrgList(orgInf, operOrgId, pageNo, numPerPage);
	}

	@RequestMapping(value = "roleList/queryRoleByDlg")
	public HashMap<String, Object> queryRoleByDlg(
			@ModelAttribute("RoleInf") RoleInf roleInf,
			HttpServletRequest request) {
		int total = 0;
		List<RoleInf> list = null;
		try {
			roleInf.setFlag("1"); // 设置不显式超级管理员角色
			// int pageNo = Integer.parseInt(request.getParameter("page"));
			// int numPerPage = Integer.parseInt(request.getParameter("rows"));
			// oracle分页
			// int start = (pageNo - 1) * numPerPage + 1;
			// mysql分页
			// int start = (pageNo - 1) * numPerPage ;
			// int end = (pageNo) * numPerPage;
			List<OrgInf> orgList = orgService.getOrgForAll(roleInf.getOrgId());
			String orgs = "";
			for (OrgInf org : orgList) {
				orgs = orgs + "'" + org.getOrgId() + "',";
			}
			orgs = orgs.substring(0, orgs.lastIndexOf(","));
			roleInf.setOrgIds(orgs);
			roleInf.setOrgId(null);
			total = roleService.getCount(roleInf);
			list = roleService.getListPage(roleInf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("records", total);
		result.put("rows", list);
		return result;
	}

	@RequestMapping(value = "{page}/queryOrgByDlgView")
	public String queryOrgByDlgView(HttpServletRequest request) {
		request.setAttribute("type", request.getParameter("type"));
		return "auth/user/orgList";
	}

	@RequestMapping(value = "{page}/queryRoleByDlgView")
	public String queryRoleByDlgView(HttpServletRequest request) {
		request.setAttribute("orgId", request.getParameter("orgId"));
		request.setAttribute("type", request.getParameter("type"));
		return "auth/user/roleList";
	}

	@RequestMapping(value = "userManage/modifyUsersStatus")
	@ResponseBody
	public ReturnMsg modifyUsersStatus(@RequestParam("userId") String ids,
			@RequestParam("status") int status) {
		ReturnMsg msg = new ReturnMsg();
		String strmsg = "";
		try {
			int num = userService.modifyUsersStatus(ids, status);
			if (num > 0) {
				if (status == 1)
					strmsg = "用户 禁用操作成功！";
				else
					strmsg = "用户启用操作成功！";
				msg.setMsg("200", strmsg);

			} else {
				if (status == 1)
					strmsg = "用户 禁用操作失败！";
				else
					strmsg = "用户启用操作失败！";

				msg.setMsg("201", strmsg);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg.setMsg("202", e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "userManage/resetUserPwd")
	@ResponseBody
	public ReturnMsg resetUserPwd(@RequestParam("userId") String ids) {
		ReturnMsg msg = new ReturnMsg();

		try {
			userService.modifyUserPwd(ids, null, null);
			msg.setMsg("200", "密码已重置为“111111”,重新登录时生效！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg.setMsg("202", e.getMessage());
		}
		return msg;
	}

	@RequestMapping(value = "userManage/delete")
	@ResponseBody
	public ReturnMsg removeUsers(@RequestParam("userId") String ids)
			throws Exception {
		ReturnMsg msg = new ReturnMsg();
		try {
			userService.removeUsers(ids);
			msg.setMsg("200", "用户删除成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg.setMsg("202", e.getMessage());
		}

		return msg;
	}

	@RequestMapping(value = "userManage/modify")
	@ResponseBody
	public ReturnMsg modifyUser(UserInf user) throws Exception {
		ReturnMsg rm = new ReturnMsg();
		int num = userService.modifyEntity(user);
		if (num == 1) {
			rm.setSuccess("成功更新用户" + user.getUserId());
		} else {
			rm.setFail("用户更新失败");
		}
		return rm;
	}

	@RequestMapping(value = "userAdd/add")
	@ResponseBody
	public ReturnMsg addUser(@ModelAttribute("UserInf") UserInf user,
			HttpSession session) {
		ReturnMsg rm = new ReturnMsg();
		int num;
		try {
			UAI uai = (UAI) session.getAttribute("UID");
			user.setOrgId(uai.getOrgId());
			user.setSysId(uai.getSysId());
			user.setAgentId(user.getAgentId());
			int count = userService.countEntity(new UserInf(user.getUserId(),
					user.getOrgId(), user.getSysId(), 0));
			if (count > 0) {
				rm.setMsg("203", "用户添加失败，用户名[" + user.getUserId() + "]已存在！");
				return rm;
			}

			num = userService.addEntity(user);
			if (num == 1)
				rm.setMsg("200", "用户添加成功！用户名:" + user.getUserId());
			else
				rm.setMsg("201", "用户添加失败！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			rm.setMsg("202", "用户添加异常！");
		}
		return rm;
	}

	@RequestMapping(value = "userEdit/edit")
	@ResponseBody
	public ReturnMsg editUser(@ModelAttribute("UserInf") UserInf user) {
		ReturnMsg rm = new ReturnMsg();
		try {

			userService.modifyUserRole(user, null, null);
			rm.setMsg("200", "用户更新成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			rm.setMsg("202", e.getMessage());
		}
		return rm;
	}

	@RequestMapping(value = "vlidateUserName")
	@ResponseBody
	public ReturnMsg vlidateUserName(@ModelAttribute("UserInf") UserInf user) {
		ReturnMsg rm = new ReturnMsg();
		try {
			int count = userService.countEntity(new UserInf(user.getUserId(),
					user.getOrgId(), null, 0));
			if (count > 0) {
				rm.setMsg("203", "用户名[" + user.getUserId() + "]已存在！");
				return rm;
			}
			rm.setMsg("200", "OK");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			rm.setMsg("202", "用户名检验失败！");
		}
		return rm;
	}

	/**
	 * 从session中获取用户信息。
	 * 
	 * @param session
	 * @return msg
	 */
	@RequestMapping(value = "uid/query")
	@ResponseBody
	public ReturnMsg getUID(HttpSession session) {
		ReturnMsg rm = new ReturnMsg();
		try {
			UAI uai = (UAI) session.getAttribute("UID");

			List<MenuInf> menus = menuService.queryAuthAccording(uai.getId(),
					uai.getSysId());
			log.debug("用户拥有的菜单树：{}", menus);
			// request.setAttribute("menus", menus);
			// 查询用户的权限
			uai.setMenuAuth(menuService.queryAuthMap(uai.getId(),
					uai.getSysId()));
			session.setAttribute("UID", uai);
			log.debug("用户信息:[{}]", uai.toString());

			rm.setMsg("200", "获取用户信息OK");
			rm.setObj(uai);
			log.info("获取用户信息OK,{}", uai.toString());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			rm.setMsg("202", "获取用户UID失败！");
		}
		return rm;
	}

	@RequestMapping(value = "adduser")
	@ResponseBody
	public ReturnMsg adduser(String sid) {
		ReturnMsg msg = new ReturnMsg();
		try {
			userLoginService.saveUser(sid);
			msg.setMsg("200", "添加成功！");
		} catch (Exception e) {
			msg.setMsg("201", "添加失败！");
			log.error(e.getMessage(), e);
		}
		return msg;

	}

    @RequestMapping(value = "findTeacherAndClazzRel")
    @ResponseBody
    public String  findTeacherAndClazzRel(HttpServletRequest request) {
        teacherClazzRelInfService.deleteBatch(teacherClazzRelInfService.query());
        List<SchoolInf> schoolInfList = schoolInfService.query();
        for (SchoolInf schoolInf : schoolInfList) {
            shieldSyncApp.findTeacherAndClazzRel(schoolInf);
        }
        //JSONObject userObj = shieldSyncApp.listUserByRoleInSchool(Constant.SchoolId.GJ);
        //JSONObject userObj = shieldSyncApp.listOrgClassByTeacher("2390000238000002736",null);
        return "success";
    }

    @RequestMapping(value = "sdktest")
    @ResponseBody
    public JSONObject  sdktest(HttpServletRequest request) {
        //JSONObject result = shieldSyncApp.getSchool(Constant.SchoolId.GJ);
        JSONObject result = shieldSyncApp.listUserByRoleInSchool(Constant.SchoolId.CBD);
        //JSONObject result = shieldSyncApp.teacherTimeTable("fbc284072a40da84adbe206f721b328575063df57018bda60534778a63c03b6cf7a1b66468194928dd05ed4d02743c788c3fcef23c1506c2","2019","1","1");
        //JSONObject result = shieldSyncApp.getCurrentTeachingCycleInSchool(Constant.SchoolId.CBD);
        //JSONObject userObj = shieldSyncApp.listOrgClassByTeacher("2390000238000002736",null);
        //JSONObject result = shieldSyncApp.teacherTimeTable("2390000159000354566");
        //JSONObject result = shieldSyncApp.sendMesasge();
        return result;
    }



}
