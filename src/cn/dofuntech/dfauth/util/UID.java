package cn.dofuntech.dfauth.util;

import javax.servlet.http.HttpSession;

/**
 * 获取用户信息
 * @author luokai
 *
 */
public class UID {
	/**
	 * 获取用户UID
	 * @param session
	 * @return
	 * @throws UIDException
	 */
	public static String get(HttpSession session){
		UAI uai = null;
		try{
			 uai = (UAI) session.getAttribute("UID");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(uai == null || uai.getId() == null){
		}
		return uai.getId().toString();
	}
	/**
	 * 获取UAI
	 * @param session
	 * @return
	 * @throws UIDException
	 */
	public static UAI getUAI(HttpSession session){
		UAI uai = null;
		try{
			 uai = (UAI) session.getAttribute("UID");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(uai == null || uai.getId() == null){
		}
		return uai;
	}
	

}
