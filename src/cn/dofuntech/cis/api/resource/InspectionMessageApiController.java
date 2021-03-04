package cn.dofuntech.cis.api.resource;

import cn.dofuntech.cis.admin.repository.domain.ImReadLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.service.*;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.cis.api.util.ImageUpload;
import cn.dofuntech.cis.bean.EnvUtil;
import cn.dofuntech.dfauth.bean.Dict;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.bean.UserRoleRelInf;
import cn.dofuntech.dfauth.service.DictService;
import cn.dofuntech.dfauth.service.RoleService;
import cn.dofuntech.dfauth.service.UserRoleRelService;
import cn.dofuntech.dfauth.service.UserService;
import com.wordnik.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Scope("prototype")
@Path("inspectionMessage")
@Api(value = "inspectionMessage", description = "一日综述")
@Produces(MediaType.APPLICATION_JSON)
public class InspectionMessageApiController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(InspectionMessageApiController.class);

    @Autowired
    private InspectionLogsService inspectionLogsService;

    @Autowired
    private InspectionResultService inspectionResultService;

    @Autowired
    private ClazzInfService clazzInfService;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private InspectionMessageService inspectionMessageService;

    @Autowired
    private ImReadLogsService imReadLogsService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserRoleRelService userRoleRelService;
	
	/*@GET
	@Path("/readLog/{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "阅读日志", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg readLog(@ApiParam(name = "id", required = true, value = "一日综述ID") @PathParam("id") Long id){
		ReturnMsg msg = new ReturnMsg();
		Long userId = (long)getUser().getId();
		try{
			if(userId==null||userId<=0){
				msg.setFail("用户id不能为空");
				return msg;
			}
			log.info("阅读日志开始...");
			InspectionMessage message = inspectionMessageService.get(id);
			msg.setObj(message);
		}catch(Exception e){
			msg.setFail("阅读日志，异常:" + e.getMessage());
			log.error("阅读日志，{}", e.getMessage(), e);
		}
		return msg;
	}*/

    /**
     * 校务巡查-3种综述
     * 校内执勤
     * 护校队巡查
     * 3个入口的统一新增方法
     * @param entity
     * @return
     */
    @POST
    @Path("/addMessage")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "新增综述", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg addMessage(InspectionMessage entity) {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        String schoolId = getUser().getAgentId();
        try {
            if (userId == null || userId <= 0) {
                msg.setFail("用户id不能为空");
                return msg;
            }
            log.info("新增综述开始...");
            //	String[] uploadImg = entity.getImgs().split(",");
            Map<String, Object> map2 = new HashMap<String, Object>();
            if (schoolId.equals("1")) {
                map2.put("dictName", "国际人员");
            } else if (schoolId.equals("2")) {
                map2.put("dictName", "阳光人员");
            } else if (schoolId.equals("3")) {
                map2.put("dictName", "CBD人员");
            }

            //查询
            Dict dict = dictService.get(map2);
            UserInf user = new UserInf();
            user.setUserName(dict.getDictValue());
            user = userService.getEntity(user);
            List<String> list = entity.getListimgs();
            StringBuilder imgs = new StringBuilder();
            for (String uploadImg : list) {

                String fileName = ImageUpload.uploadImgByBase64(uploadImg, envUtil.getSystemFilePath());
                log.debug("图片开始上传" + fileName);
                imgs.append(fileName + ",");
            }
            imgs = imgs.deleteCharAt(imgs.length() - 1);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String[] date = df.format(new Date()).split("-");

            StringBuilder date1 = new StringBuilder();
            for (int i = 0; i < date.length; i++) {
                date1.append(date[i]);
            }

            entity.setSchoolId(Long.parseLong(getUser().getAgentId()));
            entity.setFmtDate(date1.toString());
            entity.setUserName(getUser().getUserName());
            entity.setImgs(imgs.toString());
            entity.setStatus("0");
            if (entity.getTitle().contains("校内执勤") || entity.getTitle().contains("校务巡查") || entity.getTitle().contains("护校队巡查")) {
                entity.setReceiver(1L);
            } else {
                entity.setReceiver(Long.parseLong(user.getId().toString()));
            }
            entity.setUserId(userId);
            entity.setAddTime(new Timestamp(System.currentTimeMillis()));
            inspectionMessageService.insert(entity);
            if (entity.getTitle().contains("校务巡查")
                    || entity.getTitle().contains("校务巡查反馈")
                    || entity.getTitle().contains("后勤巡查反馈")) {
                //一日综述、一周综述、校园大事记、 校务巡查反馈、后勤巡查反馈 自动发布
                pushMessage(entity.getId());
            } else if (entity.getTitle().contains("校内执勤")
                    || entity.getTitle().contains("护校队巡查")) {
                //教师执勤、护校队执勤 生成一条已读记录
                pushMessageSelf(entity.getId());
            }
            msg.setSuccess("提交成功");
            //im.setUserName(userName);
        } catch (Exception e) {
            msg.setFail("阅读日志，异常:" + e.getMessage());
            log.error("阅读日志，{}", e.getMessage(), e);
        }
        return msg;
    }

    public ReturnMsg pushMessageSelf(Long message_id) {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        InspectionMessage inspectionMessage = inspectionMessageService.get(message_id);
        try {
            if (userId == null || userId <= 0) {
                msg.setFail("用户id不能为空");
                return msg;
            }
            ImReadLogs entity = new ImReadLogs();
            entity.setMessageId(message_id);
            entity.setIsread("0");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            entity.setAddtime(inspectionMessage.getAddTime());
            entity.setEdittime(inspectionMessage.getAddTime());
            Map<String, Object> map = new HashMap<>();
            map.put("agentId", getUser().getAgentId());//学校id
            entity.setType(inspectionMessage.getType());
            entity.setUserId((long) getUser().getId());
            entity.setUserName(getUser().getUserName());
            imReadLogsService.insert(entity);
            //修改推送状态
            inspectionMessage.setStatus("1");
            inspectionMessageService.update(inspectionMessage);
        } catch (Exception e) {
            msg.setFail("推送自己打卡消息，异常:" + e.getMessage());
            log.error("推送自己打卡消息，{}", e.getMessage(), e);
        }
        pushMessageManager(message_id);
        return msg;
    }

    @POST
    @Path("/changeIsread")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "读取状态变更", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg changeIsread(ImReadLogs entity) {
        //读取状态变更 messageid+userid
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        InspectionMessage inspectionMessage = inspectionMessageService.get(entity.getMessageId());

        String imgs[] = inspectionMessage.getImgs().split(",");
        List<String> listings = new ArrayList<String>();
        for (int i = 0; i < imgs.length; i++) {
            listings.add(imgs[i]);
        }
        inspectionMessage.setListimgs(listings);

        String title = inspectionMessage.getTitle();
        if (title.contains("护校队巡查")) {
            inspectionMessage.setTitleDiy(title.substring(title.length() - 5) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 5)));
        } else {
            inspectionMessage.setTitleDiy(title.substring(title.length() - 4) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 4)));
        }

        try {
            log.info("综述已读，变更开始...");
            entity.setUserId(userId);
            entity.setIsread("1");

            entity.setReadtime(new Timestamp(System.currentTimeMillis()));
            imReadLogsService.updateOne(entity);
            msg.setSuccess("已读取");
            msg.setObj(inspectionMessage);
        } catch (Exception e) {
            msg.setFail("读取日志已读更新，异常:" + e.getMessage());
            log.error("读取日志已读更新，{}", e.getMessage(), e);
        }
        return msg;
    }

    /*@GET
    @Path("/readCount")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
    @ApiOperation(value = "阅读人数统计", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
    public ReturnMsg readCount() {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long)getUser().getId();
        try {
            if(userId==null||userId<=0){
                msg.setFail("用户id不能为空");
                return msg;
            }
            log.info("未读综述统计,开始...");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            param.put("isread", "0");
            List<ImReadLogs> list = imReadLogsService.query(param);
            int count = list==null?0:list.size();
            msg.setObj(count);
            msg.setSuccess("未读综述统计成功");
        } catch (Exception e) {
            msg.setFail("未读综述统计，异常:" + e.getMessage());
            log.error("未读通知统计异常，{}", e.getMessage(), e);
        }
        return msg;
    }*/
    @GET
    @Path("/pushMessage/{message_id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "推送一日综述", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg pushMessage(@ApiParam(name = "message_id", required = true, value = "一日综述ID") @PathParam("message_id") Long message_id) {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        InspectionMessage inspectionMessage = inspectionMessageService.get(message_id);
        try {
            if (userId == null || userId <= 0) {
                msg.setFail("用户id不能为空");
                return msg;
            }
            log.info("推送综述,开始...");

            ImReadLogs entity = new ImReadLogs();
            entity.setMessageId(message_id);
            entity.setIsread("0");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            entity.setAddtime(inspectionMessage.getAddTime());
            entity.setEdittime(inspectionMessage.getAddTime());
            entity.setType(inspectionMessage.getType());
            Map<String, Object> map = new HashMap<>();
            map.put("agentId", getUser().getAgentId());//学校id
            //教师角色为7
            List<UserInf> list = userService.queryAll(map);

            for (UserInf u : list) {
                logger.debug("推送综述,开始...id是" + u.getId());
                entity.setUserId((long) u.getId());
                entity.setUserName(u.getUserName());
                // 老师entity.setId(id);
                imReadLogsService.insert(entity);
            }
            log.info("所有老师推送完毕...");
            msg.setSuccess("所有老师推送综述完毕");

            //修改推送状态
            inspectionMessage.setStatus("1");
            inspectionMessageService.update(inspectionMessage);

        } catch (Exception e) {
            msg.setFail("推送综述，异常:" + e.getMessage());
            log.error("推送综述，{}", e.getMessage(), e);
        }
        return msg;
    }

    public ReturnMsg pushMessageManager(@ApiParam(name = "message_id", required = true, value = "一日综述ID") @PathParam("message_id") Long message_id) {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        InspectionMessage inspectionMessage = inspectionMessageService.get(message_id);
        try {
            if (userId == null || userId <= 0) {
                msg.setFail("用户id不能为空");
                return msg;
            }
            log.info("推送管理人员,开始...");

            ImReadLogs entity = new ImReadLogs();
            entity.setMessageId(message_id);
            entity.setIsread("0");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            entity.setAddtime(inspectionMessage.getAddTime());
            entity.setEdittime(inspectionMessage.getAddTime());
            entity.setType(inspectionMessage.getType());
            Map<String, Object> map = new HashMap<>();
            map.put("agentId", getUser().getAgentId());//学校id
            //管理员、校务、校长
            List<UserRoleRelInf> list = userRoleRelService.queryManager(map);
            for (UserRoleRelInf u : list) {
                logger.debug("推送管理人员,开始...id是" + u.getUserId());
                entity.setUserId((long) u.getUserId());
                UserInf userInfParam = new UserInf();
                userInfParam.setId(u.getUserId());
                UserInf userInf = userService.getEntity(userInfParam);
                entity.setUserName(userInf.getUserName());
                // 老师entity.setId(id);
                imReadLogsService.insert(entity);
            }
            log.info("推送管理人员推送完毕...");
            msg.setSuccess("推送管理人员推送综述完毕");

            //修改推送状态
            inspectionMessage.setStatus("1");
            inspectionMessageService.update(inspectionMessage);

        } catch (Exception e) {
            msg.setFail("推送管理人员，异常:" + e.getMessage());
            log.error("推送管理人员，{}", e.getMessage(), e);
        }
        return msg;
    }

    @POST
    @Path("/readCountDetail")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "阅读日志统计明细", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg readCountDetail(ImReadLogs ir) {
        ReturnMsg msg = new ReturnMsg();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("messageId", ir.getMessageId());
            //查询一日详情
            InspectionMessage inspectionMessage = inspectionMessageService.get(ir.getMessageId());
            String imgs[] = inspectionMessage.getImgs().split(",");
            List<String> listings = new ArrayList<String>();
            for (int i = 0; i < imgs.length; i++) {
                listings.add(imgs[i]);
            }
            inspectionMessage.setListimgs(listings);

            String title = inspectionMessage.getTitle();
            if (title.contains("护校队巡查")) {
                inspectionMessage.setTitleDiy(title.substring(title.length() - 5) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 5)));
            } else {
                inspectionMessage.setTitleDiy(title.substring(title.length() - 4) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 4)));
            }

            //查询所有（阅读人数统计）  用messageid
            List<ImReadLogs> alllogs = imReadLogsService.query(map);
            //已查看
            map.put("isread", "1");
            List<ImReadLogs> unRead = imReadLogsService.query(map);
            map.put("allcount", alllogs.size());
            map.put("unRead", unRead.size());
            map.put("inspectionMessage", inspectionMessage);
            msg.setObj(map);
        } catch (Exception e) {
            msg.setFail("查询阅读日志统计明细，异常:" + e.getMessage());
            log.error("查询阅读日志统计明细，{}", e.getMessage(), e);
        }
        return msg;
    }

    @GET
    @Path("/list")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "管理员右上角巡查消息列表", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg list() {
        ReturnMsg msg = new ReturnMsg();
        String schoolId = getUser().getAgentId();
        try {
            Map<String, Object> messageParam = new HashMap<String, Object>();
            Timestamp date = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
            messageParam.put("addTime", s1.format(date));
            messageParam.put("schoolId", getUser().getAgentId());
            //查询今日所有
            List<InspectionMessage> alllogs = inspectionMessageService.queryAll(messageParam);
            String title;
            for (InspectionMessage message : alllogs) {
                title = message.getTitle();
                if (title.contains("后勤巡查反馈")) {
                    message.setTitleDiy(title);
                } else if (title.contains("护校队巡查")) {
                    message.setTitleDiy(title.substring(title.length() - 5) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 5)));
                } else if (title.contains("校务巡查反馈") || title.contains("后勤巡查反馈")) {
                    message.setTitleDiy(title.substring(title.length() - 6) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 6)));
                } else {
                    message.setTitleDiy(title.substring(title.length() - 4) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 4)));
                }
            }

            msg.setObj(alllogs);
        } catch (Exception e) {
            msg.setFail("查询阅读日志统计明细，异常:" + e.getMessage());
            log.error("查询阅读日志统计明细，{}", e.getMessage(), e);
        }
        return msg;
    }

    public static void main(String[] args) {
        String title = "12345";
        System.out.println(title.substring(title.length() - 4));
    }

    @GET
    @Path("/list1")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "老师一日综述消息查看", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg list1() {
        ReturnMsg msg = new ReturnMsg();
        Long userId = (long) getUser().getId();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Timestamp date = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
            map.put("addtime", s1.format(date));

            map.put("userId", userId);
            //查询今日所有
            List<ImReadLogs> alllogs = imReadLogsService.queryAll(map);
            for (ImReadLogs readLog : alllogs) {
                InspectionMessage message = inspectionMessageService.get(readLog.getMessageId());
                String title = message.getTitle();
                if (title.contains("后勤巡查反馈")) {
                    message.setTitleDiy(title);
                } else if (title.contains("护校队巡查")) {
                    message.setTitleDiy(title.substring(title.length() - 5) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 5)));
                } else if (title.contains("校务巡查反馈") || title.contains("后勤巡查反馈")) {
                    message.setTitleDiy(title.substring(title.length() - 6) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 6)));
                } else {
                    message.setTitleDiy(title.substring(title.length() - 4) + "-" + (title.split("-")[1].substring(0, title.split("-")[1].length() - 4)));
                }
                readLog.setInspectionMessage(message);
            }
            map.put("allcount", alllogs);
            msg.setObj(map);
        } catch (Exception e) {
            msg.setFail("查询阅读日志统计明细，异常:" + e.getMessage());
            log.error("查询阅读日志统计明细，{}", e.getMessage(), e);
        }
        return msg;
    }

    @GET
    @Path("/getOne")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "查询指定通告人", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg getOne() {
        ReturnMsg msg = new ReturnMsg();
        String schoolId = getUser().getAgentId();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if (schoolId.equals("1")) {
                map.put("dictName", "国际人员");
            } else if (schoolId.equals("2")) {
                map.put("dictName", "阳光人员");
            } else if (schoolId.equals("3")) {
                map.put("dictName", "CBD人员");
            }
            //查询
            Dict dict = dictService.get(map);
            UserInf user = new UserInf();
            user.setUserName(dict.getDictValue());
            user = userService.getEntity(user);
            msg.setObj(user.getId());
        } catch (Exception e) {
            msg.setFail("查询指定通告人，异常:" + e.getMessage());
            log.error("查询指定通告人，{}", e.getMessage(), e);
        }
        return msg;
    }

    @POST
    @Path("/editReceiver")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "上报校长", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg editReceiver(InspectionMessage in) {
        ReturnMsg msg = new ReturnMsg();
        try {
            //查询一日详情
            InspectionMessage inspectionMessage = inspectionMessageService.get(in.getId());
            InspectionMessage message = new InspectionMessage();
            message.setAddTime(new Timestamp(System.currentTimeMillis()));
            message.setType(inspectionMessage.getType());
            message.setRemark(inspectionMessage.getRemark());
            message.setUserName(inspectionMessage.getUserName());
            message.setUserId(inspectionMessage.getUserId());
            message.setImgs(inspectionMessage.getImgs());
            message.setFmtDate(inspectionMessage.getFmtDate());
            message.setStatus("0");
            message.setTitle(inspectionMessage.getTitle());
            message.setSchoolId(inspectionMessage.getSchoolId());
            message.setPlace(inspectionMessage.getPlace());
            message.setReceiver(1L);
            inspectionMessageService.insert(message);
            msg.setSuccess("上报校长成功");
        } catch (Exception e) {
            msg.setFail("上报校长，异常:" + e.getMessage());
            log.error("上报校长，{}", e.getMessage(), e);
        }
        return msg;
    }
}
