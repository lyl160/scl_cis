package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import cn.dofuntech.cis.admin.repository.domain.Place;
import cn.dofuntech.cis.admin.service.PlaceService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.dfauth.bean.UserInf;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Scope("prototype")
@Path("place")
@Api(value = "place", description = "常用地址查询")
@Produces(MediaType.APPLICATION_JSON)
public class PlaceApiController  extends BaseController{
	private static Logger log = LoggerFactory.getLogger(PlaceApiController.class);
	@Autowired
	private PlaceService placeService;
	
	@GET
	@Path("/query")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "常用地址查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg Category() {
		ReturnMsg msg = new ReturnMsg();
		log.info("分类查询===开始==");
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			UserInf user = getUser();
			m.put("schoolId",user.getAgentId());
			m.put("userId", "1");
			List<Place> ic = placeService.query(m);
			msg.setObj(ic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("常用地址查询异常:" + e.getMessage());
		}
		return msg;
		
		
	}
	
	@GET
	@Path("/queryPlace")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "教师默认地址查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg queryPlace() {
		ReturnMsg msg = new ReturnMsg();
		log.info("分类查询===开始==");
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			UserInf user = getUser();
			m.put("userId",user.getId());
			Place ic = placeService.get(m);
			msg.setObj(ic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("教师默认地址查询异常:" + e.getMessage());
		}
		return msg;
		
		
	}
	
	@POST
	@Path("/editPlace")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "教师默认地址更改", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg editPlace(Place place) {
		ReturnMsg msg = new ReturnMsg();
		log.info("分类查询===开始==");
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			UserInf user = getUser();
			m.put("userId",user.getId());
			Place ic = placeService.get(m);
			if(ic == null){
			Place pl = new Place();
			pl.setPlaceName(place.getPlaceName());
			pl.setDefaultFlag("0000");
			pl.setAddTime(new Timestamp(System.currentTimeMillis()));
			pl.setSchoolId(Long.parseLong(user.getAgentId()));
			pl.setUserId(Long.parseLong(user.getId().toString()));
			placeService.insert(pl);
			}else{
				ic.setPlaceName(place.getPlaceName());
				ic.setDefaultFlag("0000");
				ic.setAddTime(ic.getAddTime());
				ic.setEditTime(new Timestamp(System.currentTimeMillis()));
				ic.setSchoolId(Long.parseLong(user.getAgentId()));
				ic.setUserId(Long.parseLong(user.getId().toString()));
				placeService.update(ic);
			}
			msg.setSuccess("修改成功");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("教师默认地址修改异常:" + e.getMessage());
		}
		return msg;
		
		
	}
}
