package cn.dofuntech.cis.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.bean.SmsMode;
import cn.dofuntech.tools.mq.ActivemqService;

@Scope("prototype")
@Path("user")
@Api(value = "user", description = "用户注册")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterApiController {

	@Autowired
    private ActivemqService          mqService;
	
	@GET
	@Path("/register/{phone}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "短信验证码", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg teamClazz1(@ApiParam(name = "phone", required = true, value = "通知手机号") @PathParam("phone")String phone){
		ReturnMsg msg = new ReturnMsg();
		try {
			String vcode = RandomStringUtils.randomNumeric(6);
			 SmsMode smode = new SmsMode(vcode,phone);
             if (mqService.sendMessage(smode.formatData())) {
            
               msg.setSuccess("验证码已发送至您的手机");
            }
             else {
                
                msg.setFail("消息服务器异常");
             }
		} catch (Exception e) {
			msg.setFail("发送异常:" + e.getMessage());
			
		}
		return msg;
	}
	

}
