package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


import cn.dofuntech.cis.admin.repository.domain.TowCodeResult;
import cn.dofuntech.cis.admin.repository.domain.TwoCode;
import cn.dofuntech.cis.admin.service.TowCodeResultService;
import cn.dofuntech.cis.admin.service.TwoCodeService;
import cn.dofuntech.cis.api.bean.ReturnMsg;


@Scope("prototype")
@Path("twocoderesult")
@Api(value = "twocoderesult", description = "扫码记录表")
@Produces(MediaType.APPLICATION_JSON)
public class TwoCodeResultApiController {

	@Autowired
	private TowCodeResultService towCodeResultService;
	
	@Autowired
	private TwoCodeService twoCodeService;
	
	
	@GET
	@Path("/insertTwocode/{text}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "扫码记录生成", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg teamClazz(@ApiParam(name = "text", required = true, value = "位置") @PathParam("text")String text){
		ReturnMsg msg = new ReturnMsg();
		try {
			//查询是否可用
			Map<String,Object>  map = new HashMap<String,Object>();
			map.put("text",text);
			TwoCode twoCode = twoCodeService.get(map);
		   if(twoCode.getStatus().equals("0"))
		   {
			   TowCodeResult tr = new TowCodeResult();
			   tr.setCreateTime(new Timestamp(System.currentTimeMillis()));
			   tr.setTwoId(twoCode.getId());
			   tr.setTwoText(text);
			   towCodeResultService.insert(tr);
			   msg.setSuccess("扫码成功");
		   }
		   else
		   {
			   msg.setSuccess("二维码不可用");
			   
		   }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("操作失败");
		}
		return msg;
	}
}
