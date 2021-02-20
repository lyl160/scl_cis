package cn.dofuntech.dfauth.service;

import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.util.UAI;
/**
 * 用户登陆接口
 * @author ..
 *
 */
public interface UserLoginService {

	/**
	 * 用户登录（不校验验证码）
	 * @param user 用户信息
	 * @return
	 */
	public UAI logIn(UserInf user)throws Exception;

	public ReturnMsg saveUser(String sid) throws Exception;

}
