package cn.dofuntech.cis.api.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.dfauth.bean.UserInf;

@Scope("prototype")
@Path("inspectionTemplate")
@Api(value = "inspectionTemplate", description = "模板查询")
@Produces(MediaType.APPLICATION_JSON)

public class InspectionTemplateApiController extends BaseController{
	
	
	@Autowired
	private InspectionTemplateService inspectionTemplateService;
	
	
	@GET
	@Path("/template")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "模板查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	
	public ReturnMsg template(){
		
		ReturnMsg msg = new ReturnMsg();
		
	  try {
		  UserInf user = getUser();
		  Map<String,Object> param = new HashMap<String,Object>();
		  param.put("schoolId",user.getAgentId());
		List<InspectionTemplate>  it   = inspectionTemplateService.query(param);
		msg.setObj(it);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		msg.setFail("查询失败");
	}
	  
	  return msg;
	}
	
	
	

}
