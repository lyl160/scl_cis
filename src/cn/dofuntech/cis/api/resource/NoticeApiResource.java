package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import cn.dofuntech.cis.admin.repository.domain.Notice;
import cn.dofuntech.cis.admin.service.NoticeService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.dfauth.bean.UserInf;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Scope("prototype")
@Path("notice/")
@Api(value = "notice", description = "系统通知")
@Produces(MediaType.APPLICATION_JSON)
public class NoticeApiResource extends BaseController{
	private static Logger log = LoggerFactory
			.getLogger(NoticeApiResource.class);
	@Autowired
	private NoticeService noticeService;

	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "查询用户消息列表", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg getNoticeList(Map<String, Object> params) {
		ReturnMsg msg = new ReturnMsg();
		Map<String, Object> result = new HashMap<String, Object>();
		Long userId = (long)getUser().getId();
	
		try {
			if(userId==null||userId<=0){
				msg.setFail("用户id不能为空");
				return msg;
			}
			log.info("查询用户消息列表开始...");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			Integer page = MapUtils.getInteger(params, "page");
        	Integer rows = MapUtils.getInteger(params, "rows");
            if (page == null) {
                page = Paginator.DEFAULT_CURRENT_PAGE;
            }
            if (rows == null) {
                rows = Paginator.DEFAULT_PAGE_SIZE;
            }
            Paginator paginator = new Paginator(page, rows);
			List<Notice> list = noticeService.query(param, paginator);
		
			
			result.put("rows", list);
            result.put("page", page);
            result.put("total", paginator.getTotalCount());
            result.put("totalPage", paginator.getTotalPage());
			msg.setObj(result);
			msg.setSuccess("查询用户消息列表成功");
		} catch (Exception e) {
			msg.setFail("查询用户消息列表，异常:" + e.getMessage());
			log.error("查询用户消息列表，{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("upSingleStatus/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "单条通知已读更新", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg upSingleStatus(@ApiParam(name = "id", required = true, value = "通知ID") @PathParam("id") Long id) {
		ReturnMsg msg = new ReturnMsg();
		try {
			log.info("单条通知已读，更新开始...");
			Notice notice = noticeService.get(id);
			notice.setIsread("1");
			notice.setReadtime(new Timestamp(System.currentTimeMillis()));
			noticeService.update(notice);
			msg.setSuccess("单条通知已读更新成功");
		} catch (Exception e) {
			msg.setFail("单条通知已读更新，异常:" + e.getMessage());
			log.error("单条通知已读更新，{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("upAllStatus")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "所有通知已读更新", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg upAllStatus() {
		ReturnMsg msg = new ReturnMsg();
		Long userId = (long)getUser().getId();
		try {
			if(userId==null||userId<=0){
				msg.setFail("用户id不能为空");
				return msg;
			}
			log.info("所有通知已读更新，开始...");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			noticeService.upAllStatus(param);
			msg.setSuccess("所有通知已读更新成功");
		} catch (Exception e) {
			msg.setFail("所有通知已读更新，异常:" + e.getMessage());
			log.error("所有通知已读更新，{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("noReadStat")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "未读通知统计", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg noReadStat() {
		ReturnMsg msg = new ReturnMsg();
		Long userId = (long)getUser().getId();
		
		try {
			if(userId==null||userId<=0){
				msg.setFail("用户id不能为空");
				return msg;
			}
			log.info("未读通知统计,开始...");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("isread", "0");
			List<Notice> list = noticeService.query(param);
			int count = list==null?0:list.size();
			msg.setObj(count);
			msg.setSuccess("未读通知统计成功");
		} catch (Exception e) {
			msg.setFail("未读通知统计，异常:" + e.getMessage());
			log.error("未读通知统计异常，{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("delete/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "删除单条通知", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg deleteById(@ApiParam(name = "id", required = true, value = "通知ID") @PathParam("id") Long id) {
		ReturnMsg msg = new ReturnMsg();
		try {
			log.info("删除单条通知,开始...");
			Notice entity = new Notice();
			entity.setId(id);
			noticeService.delete(entity);
			msg.setSuccess("删除单条通知成功");
		} catch (Exception e) {
			msg.setFail("删除单条通知，异常：" + e.getMessage());
			log.error("删除单条通知，异常：{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("deleteAll")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "删除所有通知", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg deleteAll() {
		ReturnMsg msg = new ReturnMsg();
		Long userId = (long)getUser().getId();
		try {
			log.info("删除所有通知,开始...");
			if(userId==null||userId<=0){
				msg.setFail("用户id不能为空");
				return msg;
			}
			Notice entity = new Notice();
			entity.setUserId(userId);
			noticeService.delete(entity);
			msg.setSuccess("删除所有通知成功");
		} catch (Exception e) {
			msg.setFail("删除所有通知，异常：" + e.getMessage());
			log.error("删除所有通知，异常：{}", e.getMessage(), e);
		}
		return msg;
	}
	
	@GET
	@Path("detail/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "查看通知明细", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg getDetail(@ApiParam(name = "id", required = true, value = "通知ID") @PathParam("id") Long id) {
		ReturnMsg msg = new ReturnMsg();
		try {
			log.info("查看通知明细,开始...");
			Notice entity = noticeService.get(id);
			msg.setObj(entity);
			msg.setSuccess("查看通知明细成功");
		} catch (Exception e) {
			msg.setFail("查看通知明细，异常：" + e.getMessage());
			log.error("查看通知明细，异常：{}", e.getMessage(), e);
		}
		return msg;
	}
}
