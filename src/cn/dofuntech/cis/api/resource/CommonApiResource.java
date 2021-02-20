package cn.dofuntech.cis.api.resource;

import java.util.Map;

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

import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.dfauth.service.UserService;

import com.alibaba.fastjson.JSON;
import com.iflytek.edu.ew.util.AssertionHolder;
import com.iflytek.edu.ew.validation.Assertion;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Scope("prototype")
@Path("sso/user/")
@Api(value = "project", description = "通用接口")
@Produces(MediaType.APPLICATION_JSON)
public class CommonApiResource {
	private static Logger log = LoggerFactory.getLogger(CommonApiResource.class);
	@Autowired
	private  UserService userService;
	 
    @GET
    @Path("login")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    @ApiOperation(value = "单点登录测试", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
    public ReturnMsg queryList() {
        ReturnMsg msg = new ReturnMsg();
        try {
        	Assertion assertion = AssertionHolder.getAssertion();
        	String openId = assertion.getPrincipal().getName();
        	Map<String, Object> attributes = assertion.getPrincipal().getAttributes();
        	if (attributes != null) {
        	    String userId = attributes.get("userId").toString();
        	    String loginName = attributes.get("loginName").toString();
        	}
        	log.info("单点登录返回参数：{}",JSON.toJSONString(attributes));
        	msg.setObj(attributes);
        	msg.setSuccess();
        }catch (Exception e) {
            msg.setFail("单点登录，异常:"+e.getMessage());
            log.error("单点登录异常，{}",e.getMessage(),e);
        }
        return msg;
    }
    
    /*@GET
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    @ApiOperation(value = "查询工程详情", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
    public ReturnMsg getUserOrders(@ApiParam(name = "id", required = true, value = "工程ID") @PathParam("id") Long id) {
        ReturnMsg msg = new ReturnMsg();
        Project project = projectService.get(id);
        msg.setObj(project);
        msg.setSuccess();
        return msg;
    }*/
		
}
