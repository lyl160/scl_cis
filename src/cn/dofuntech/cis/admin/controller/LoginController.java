package cn.dofuntech.cis.admin.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.iflytek.edu.ew.util.AssertionHolder;
import com.iflytek.edu.ew.validation.Assertion;

import cn.dofuntech.dfauth.bean.MenuInf;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.constants.Constant;
import cn.dofuntech.dfauth.service.MenuService;
import cn.dofuntech.dfauth.service.UserLoginService;
import cn.dofuntech.dfauth.service.exception.UserLoginException;
import cn.dofuntech.dfauth.util.DfBasicFunctions;
import cn.dofuntech.dfauth.util.UAI;
import cn.dofuntech.dfauth.util.UserAppSession;

/**
 * 控制器
 * AppController
 *
 */

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
	private UserLoginService userLoginService;
	@Autowired
	private MenuService menuService;

    @RequestMapping("/logout")
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
    	session.removeAttribute("UID");//清空session信息  
    	session.removeAttribute("adminid");//清空session信息  
    	session.invalidate();//清除 session 中的所有信息  
    }
    
	/**
	 * 单点登录成功后访问首页
	 * @param request
	 * @return index.jsp
	 */
	@RequestMapping(value = "/mainPanel")
	public String mainPanel(HttpSession session, HttpServletRequest request) {
		UAI uai = null;
		try {
//			LOGGER.info("单点登录成功后，获取用户信息");
//			Assertion assertion = AssertionHolder.getAssertion();
//			String openId = assertion.getPrincipal().getName();
//			LOGGER.info("openid：{}", openId);
//			String userId = "";// 讯飞系统的用户id，暂时没用到
//			String loginName = "";
//			Map<String, Object> attributes = assertion.getPrincipal()
//					.getAttributes();
//			if (attributes != null) {
//				userId = attributes.get("userId").toString();
//				loginName = attributes.get("loginName").toString();
//			}
//			LOGGER.info("单点登录返回参数：{}", JSON.toJSONString(attributes));

			// 机构号固定000000001
			UserInf user = new UserInf();
			//user.setUserId("liyanmeiwhshljxx");//总校长
			user.setUserId("gjxgy");
			//user.setUserId("cbdxgy");
			//user.setUserId("ygxgy");//阳光校管
			user.setOrgId(UAI.TOP_ORG_ID);

			user.setSysId("0001");// 指定假模块id
			user.setLastLoginIp(request.getRemoteHost());
			user.setLastLoginTime(DfBasicFunctions.GETDATETIME());
//			if (user.getAgentId() == null || user.getAgentId().equals("")) {
//				user.setAgentId(Constant.SYS_AGENT_ID);
//			}
			uai = userLoginService.logIn(user);
		} catch (UserLoginException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		uai.setOrgId(UAI.TOP_ORG_ID);
		session.setAttribute("UID", uai);
		session.setAttribute("adminid", uai.getId());// 配合登录拦截
		// 保存session到application
		UserAppSession.setUserSession(uai.getId(), session);
		// 查询菜单
		List<MenuInf> menus = menuService.queryAuthAccording(uai.getId(),
				uai.getSysId());
		LOGGER.debug("用户拥有的菜单树：{}", menus);
		// request.setAttribute("menus", menus);
		// 查询用户的权限
		uai.setMenuAuth(menuService.queryAuthMap(uai.getId(), uai.getSysId()));
		session.setAttribute("UID", uai);
		LOGGER.debug("用户信息:[{}]", uai.toString());
		return "index";
	}
	
	/**
	 *app跳板页面
	 */
	@RequestMapping(value = "/app/index")
	public String appIndex(HttpSession session, HttpServletRequest request) {
		UAI uai = null;
		try {
			LOGGER.info("单点登录成功后，获取用户信息");
			Assertion assertion = AssertionHolder.getAssertion();
			String openId = assertion.getPrincipal().getName();
			LOGGER.info("openid：{}", openId);
			String userId = "";// 讯飞系统的用户id，暂时没用到
			String loginName = "";
			Map<String, Object> attributes = assertion.getPrincipal()
					.getAttributes();
			if (attributes != null) {
				userId = attributes.get("userId").toString();
				loginName = attributes.get("loginName").toString();
			}
			LOGGER.info("单点登录返回参数：{}", JSON.toJSONString(attributes));

			// 机构号固定000000001
			UserInf user = new UserInf();
			user.setUserId(loginName);
			user.setOrgId(UAI.TOP_ORG_ID);

			user.setSysId("0001");// 指定假模块id
			user.setLastLoginIp(request.getRemoteHost());
			user.setLastLoginTime(DfBasicFunctions.GETDATETIME());
			if (user.getAgentId() == null || user.getAgentId().equals("")) {
				user.setAgentId(Constant.SYS_AGENT_ID);
			}
			uai = userLoginService.logIn(user);
			request.setAttribute("appUser", uai);
			session.setAttribute("AppUID", uai);
		} catch (UserLoginException e) {
				LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		uai.setOrgId(UAI.TOP_ORG_ID);
		return "appindex/manage";
	}
	

}
