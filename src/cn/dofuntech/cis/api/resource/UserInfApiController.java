package cn.dofuntech.cis.api.resource;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.api.bean.ReturnMsg;

import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.UserService;

@Scope("prototype")
@Path("userInf")
@Api(value = "userInf", description = "用户信息查询")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfApiController {
	private static Logger log = LoggerFactory.getLogger(UserInfApiController.class);
	
	@Autowired
	private UserService userService;
	
	@POST
	@Path("/queryrole")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	@ApiOperation(value = "角色查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg query(UserInf user){
		ReturnMsg msg = new ReturnMsg();
		try {
			UserInf queryrole = userService.queryrole(user);
			msg.setObj(queryrole);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("数据查询异常");
		}
		
		return msg;
		
	}
	
	@GET
	@Path("/queryUserInfo/{userId}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	@ApiOperation(value = "用户信息查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg queryUserInf(@ApiParam(name = "userId", defaultValue = "1", value = "当前页") @PathParam(value = "userId") String userId){
		ReturnMsg msg = new ReturnMsg();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			UserInf queryrole = userService.queryMap(params);
			msg.setObj(queryrole);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("数据查询异常");
		}
		
		return msg;
		
	}
	
}
