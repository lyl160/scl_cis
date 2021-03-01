package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.admin.repository.domain.DynamicAttr;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.Team;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.DynamicAttrService;
import cn.dofuntech.cis.admin.service.InspectionLogsService;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.cis.admin.service.TeamService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.cis.api.util.ImageUpload;
import cn.dofuntech.cis.bean.EnvUtil;
import cn.dofuntech.core.util.DateUtils;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.DictService;

@Scope("prototype")
@Path("inspectionLogs")
@Api(value = "inspectionLogs", description = "巡查新增")
@Produces(MediaType.APPLICATION_JSON)
public class InspectionLogsApiController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(InspectionLogsApiController.class);

    @Autowired
    private InspectionLogsService inspectionLogsService;

    @Autowired
    private InspectionResultService inspectionResultService;
    @Autowired
    private DynamicAttrService dynamicAttrService;

    @Autowired
    private ClazzInfService clazzInfService;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private TeamService teamService;
    @Autowired
    private InspectionTemplateService inspectionTemplateService;
    @Autowired
    private DictService dictService;


    /**
     * 校务巡查新增
     * 相同班级，相同分类 当天重复提交数据，覆盖老数据
     *
     * @param logs
     * @return
     */
    @POST
    @Path("/add1")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "校务巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public @ResponseBody
    ReturnMsg add1(InspectionLogs logs) {
        ReturnMsg msg = new ReturnMsg();
        Timestamp date = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        logs.setCzyId((long) getUser().getId());// 巡检人id
        logs.setCzy(getUser().getUserName());// 巡检人名称
        logs.setSchoolId(getUser().getAgentId());  // 学校id
        logs.setAddTime(date); //新增时间


        if (logs.getTeamId() != null) {//按团队提交
            //先删除 当天历史数据
            inspectionLogsService.deleteTodayLogsByTeamId(logs);
            //查询该团队下的所有班级
            Map<String, Object> clazzParam = new HashMap<String, Object>();
            clazzParam.put("teamId", logs.getTeamId());
            clazzParam.put("status", "0");
            List<ClazzInf> clazzInfList = clazzInfService.query(clazzParam);
            for (ClazzInf c : clazzInfList) {
                try {
                    //图片上传
                    uploadImgs(logs);
                    if (StringUtils.isNotEmpty(logs.getTeacherStr())) {
                        logs.setTeacherId(logs.getTeacherStr().split(":")[0]);
                        logs.setTeacherName(logs.getTeacherStr().split(":")[1]);
                    }
                    // 再 数据保存到日志表
                    logs.setGrade(c.getGrade());
                    logs.setClazz(c.getClazz());
                    logs.setClazzId(c.getId());
                    logs.setId(null);
                    inspectionLogsService.insert(logs);

                    //获取动态属性成果表数据
                    String[] attrScore = logs.getStr().split("@");
                    float totalScore = 0;//总得分
                    InspectionResult result = null;
                    for (int i = 0; i < attrScore.length; i++) {
                        result = new InspectionResult();
                        result.setLogsId(logs.getId());
                        result.setItemScore(attrScore[i].split(":")[3]);//单项得分
                        result.setItemCode(attrScore[i].split(":")[4]);//项目代码
                        result.setAttrName(attrScore[i].split(":")[0]);//动态属性名称
                        result.setAttrValue(attrScore[i].split(":")[1]);//动态属性值
                        result.setAttrId(NumberUtils.toLong(attrScore[i].split(":")[2]));//动态属性id
                        result.setSchoolId(Long.parseLong(logs.getSchoolId()));
                        result.setAddTime(date);
                        inspectionResultService.insert(result);
                        totalScore += Float.parseFloat(result.getItemScore());
                    }
                    logs.setTotalScore("" + totalScore);
                    inspectionLogsService.update(logs);
                    msg.setSuccess("提交成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setFail("提交失败");
                    break;
                }

            }
        } else {
            try {
                ClazzInf clazzInf = clazzInfService.get(logs.getClazzId());
                logs.setTeamId(clazzInf.getTeamId());
                //图片上传
                uploadImgs(logs);
                //保存教师
                if (StringUtils.isNotEmpty(logs.getTeacherStr())) {
                    logs.setTeacherId(logs.getTeacherStr().split(":")[0]);
                    logs.setTeacherName(logs.getTeacherStr().split(":")[1]);
                }
                //先删除 再 数据保存到日志表
                inspectionLogsService.deleteTodayLogsByClazzId(logs);
                inspectionLogsService.insert(logs);
                //获取动态属性成果表数据
                String[] attrScore = logs.getStr().split("@");
                float totalScore = 0;//总得分
                InspectionResult result = null;
                for (int i = 0; i < attrScore.length; i++) {
                    result = new InspectionResult();
                    result.setLogsId(logs.getId());
                    result.setItemScore(attrScore[i].split(":")[3]);//单项得分
                    result.setItemCode(attrScore[i].split(":")[4]);//项目代码
                    result.setAttrName(attrScore[i].split(":")[0]);//动态属性名称
                    result.setAttrValue(attrScore[i].split(":")[1]);//动态属性值
                    result.setAttrId(NumberUtils.toLong(attrScore[i].split(":")[2]));//动态属性id
                    result.setSchoolId(Long.parseLong(logs.getSchoolId()));
                    result.setAddTime(date);
                    //数据保存到动态属性成果表
                    inspectionResultService.insert(result);
                    totalScore += Float.parseFloat(result.getItemScore());
                }
                logs.setTotalScore("" + totalScore);
                inspectionLogsService.update(logs);
                msg.setSuccess("提交成功");
            } catch (Exception e) {
                e.printStackTrace();
                msg.setFail("提交失败");
            }
        }
        return msg;
    }

    private void uploadImgs(InspectionLogs logs) throws Exception {
        if (logs.getListimgs() != null && logs.getListimgs().size() > 0) {
            List<String> list = logs.getListimgs();
            StringBuilder imgs = new StringBuilder();
            for (String uploadImg : list) {
                String fileName = ImageUpload.uploadImgByBase64(uploadImg, envUtil.getSystemFilePath());
                log.debug("图片开始上传" + fileName);
                imgs.append(fileName + ",");
            }
            imgs = imgs.deleteCharAt(imgs.length() - 1);
            logs.setImgs(imgs.toString());
        }
    }


    /**
     * 后勤巡查新增
     * 相同班级，相同分类 当天重复提交数据，覆盖老数据
     *
     * @param logs
     * @return
     */
    @POST
    @Path("/add2")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "后勤巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public @ResponseBody
    ReturnMsg add2(InspectionLogs logs) {
        ReturnMsg msg = new ReturnMsg();
        Timestamp date = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        logs.setCzyId((long) getUser().getId());// 巡检人id
        logs.setCzy(getUser().getUserName());// 巡检人名称
        logs.setSchoolId(getUser().getAgentId());  // 学校id
        logs.setAddTime(date); //新增时间

        if (logs.getTeamId() != null) {//按团队提交
            //先删除 当天历史数据
            //inspectionLogsService.deleteTodayLogsByTeamId(logs);
            //查询该团队下的所有班级
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("teamId", logs.getTeamId());
            map.put("status", "0");
            List<ClazzInf> clazzInfList = clazzInfService.query(map);
            for (ClazzInf c : clazzInfList) {
                try {
                    logs.setGrade(c.getGrade());
                    logs.setClazz(c.getClazz());
                    logs.setClazzId(c.getId());
                    logs.setId(null);
                    saveLogAndResults(logs, date);
                    msg.setSuccess("提交成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setFail("提交失败");
                    break;
                }
            }
        } else {
            try {
                ClazzInf clazzInf = clazzInfService.get(logs.getClazzId());
                logs.setTeamId(clazzInf.getTeamId());
                saveLogAndResults(logs, date);

                msg.setSuccess("提交成功");
            } catch (Exception e) {
                e.printStackTrace();
                msg.setFail("提交失败");
            }
        }
        return msg;
    }

    private void saveLogAndResults(InspectionLogs logs, Timestamp date) throws Exception {
        Map<String, Object> attrQueryParam = new HashMap<String, Object>();
        attrQueryParam.put("templateId", logs.getTemplateId());
        attrQueryParam.put("schoolId", logs.getSchoolId());
        List<DynamicAttr> dynamicAttrList = dynamicAttrService.query(attrQueryParam);//动态属性全集
        List<InspectionResult> allResult =  convertAttrToResult(dynamicAttrList);

        //图片上传
        uploadImgs(logs);

        //先删除 再 数据保存到日志表
        List<InspectionResult> oldResultList = inspectionLogsService.deleteTodayLogsByClazzId(logs);
        if (oldResultList == null || oldResultList.isEmpty()) {
            oldResultList = allResult;
        }
        inspectionLogsService.insert(logs);
        //获取动态属性成果表数据
        String[] attrScore = logs.getStr().split("@");
        float totalScore = 0;//总得分
        InspectionResult result = null;
        for (int i = 0; i < attrScore.length; i++) {
            result = new InspectionResult();
            result.setLogsId(logs.getId());
            result.setItemScore(attrScore[i].split(":")[3]);//单项得分
            result.setItemCode(attrScore[i].split(":")[4]);//项目代码
            result.setAttrName(attrScore[i].split(":")[0]);//动态属性名称
            result.setAttrValue(attrScore[i].split(":")[1]);//动态属性值
            result.setAttrId(NumberUtils.toLong(attrScore[i].split(":")[2]));//动态属性id
            result.setSchoolId(Long.parseLong(logs.getSchoolId()));
            result.setAddTime(date);
            //数据保存到动态属性成果表
            inspectionResultService.insert(result);
            totalScore += Float.parseFloat(result.getItemScore());
            //如果旧明细包含
            for (InspectionResult oldResult : oldResultList) {
                if (oldResult.getAttrId().equals(result.getAttrId())) {
                    oldResult.setItemScore("-1");
                }
            }
        }
        if (oldResultList.size() > 0) {
            for (InspectionResult oldResult : oldResultList) {
                if (oldResult.getItemScore().equals("-1")) {
                    continue;
                }
                oldResult.setLogsId(logs.getId());
                oldResult.setAddTime(date);
                //数据保存到动态属性成果表
                inspectionResultService.insert(oldResult);
                totalScore += Float.parseFloat(oldResult.getItemScore());
            }
        }
        logs.setTotalScore("" + totalScore);
        inspectionLogsService.update(logs);
    }

    private List<InspectionResult> convertAttrToResult(List<DynamicAttr> dynamicAttrList) {
        List<InspectionResult> resultList = new ArrayList<>();
        if (dynamicAttrList != null && dynamicAttrList.size() > 0) {
            for (DynamicAttr attr : dynamicAttrList) {
                InspectionResult result = new InspectionResult();
                result.setItemScore("0");//单项得分
                result.setItemCode("1");//项目代码
                result.setAttrName(attr.getName());//动态属性名称
                result.setAttrValue("");//动态属性值
                result.setAttrId(attr.getId());//动态属性id
                result.setSchoolId(attr.getSchoolId());
                //result.setAddTime(date);
                resultList.add(result);
            }

        }
        return resultList;
    }


    //一周汇总查询
    @POST
    @Path("/queryhebdomad")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "一周汇总查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public @ResponseBody
    ReturnMsg queryHebdomad(InspectionLogs log) {
        ReturnMsg msg = new ReturnMsg();
        String monday = log.getMonday();
        String sunday = log.getSunday();
        if (StringUtils.isEmpty(monday)) {
            monday = DateUtils.getMonday(new Date(), 0);//获取当前日期周一所在的日期
        }
        if (StringUtils.isEmpty(sunday)) {
            sunday = DateUtils.getMonday(new Date(), 6);//获取当前日期周日所在的日期
        }
        try {
            //查询所有年级+班级 可查的
            UserInf user = getUser();
            String schoolId = user.getAgentId();
            Map<String, Object> currentLogsParam;
            Map<String, Object> prevLogsParam;

            Map<String, Object> teamParam = new HashMap<String, Object>();
            teamParam.put("schoolId", schoolId);
            List<Team> teamList = teamService.query(teamParam);
            for (Team team : teamList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("teamId", team.getId());
                map.put("status", "0");
                map.put("schoolId", schoolId);
                List<ClazzInf> classList = clazzInfService.query(map);
                team.setClazzList(classList);

                for (ClazzInf clazz : classList) {
                    clazz.setTeamId(clazz.getTeamId());
                    InspectionLogsApiController.log.debug("开始查询，当前班级是" + clazz.getClazz() + clazz.getGrade());
                    String clazzName = clazz.getClazz();
                    String grade = clazz.getGrade();

                    Double currentScore = getClazzTotalScore(clazzName, grade, monday, sunday, log.getTemplateId(), schoolId);
                    clazz.setScoreNum(currentScore + "");
                }
                //对按分数每一个班级进行排名排序
                Collections.sort(classList, new Comparator<ClazzInf>() {
                    @Override
                    public int compare(ClazzInf o1, ClazzInf o2) {
                        if (Double.parseDouble(o1.getScoreNum()) < Double.parseDouble(o2.getScoreNum())) {
                            return 1;
                        }
                        if (o1.getScoreNum().equals(o2.getScoreNum())) {
                            return 0;
                        }
                        return -1;
                    }
                });
            }

            msg.setObj(teamList);
        } catch (Exception e) {
            msg.setFail("查询失败");
            InspectionLogsApiController.log.error("查询数据异常:{}", e.getMessage(), e);
        }
        return msg;
    }

    /**
     * 查询巡查时间段总分
     *
     * @param clazzName
     * @param grade
     * @param monday     yyyy-MM-dd 开始时间
     * @param sunday     yyyy-MM-dd 结束时间
     * @param templateId
     * @param schoolId
     * @return
     */
    Double getClazzTotalScore(String clazzName, String grade, String monday, String sunday, Long templateId, String schoolId) {
        DecimalFormat df = new DecimalFormat("0.0");
        HashMap logsParam = new HashMap<String, Object>();
        logsParam.put("clazz", clazzName);
        logsParam.put("grade", grade);
        logsParam.put("monday", monday);
        logsParam.put("sunday", sunday);
        logsParam.put("templateId", templateId);
        logsParam.put("schoolId", schoolId);

        InspectionTemplate template = inspectionTemplateService.get(templateId);
        if (template.getName().equals("校务巡查")) {
            //校务巡查时 只查学生分数
            Map dictParam = new HashMap();
            dictParam.put("dictCode", "ITEMCODE");
            dictParam.put("dictValue", "2");
            //logsParam.put("category3", dictService.get(dictParam).getDictId());//1教师 2学生 3管理
        }
        //查询出当前一周的日志表数据
        List<InspectionLogs> logsList = inspectionLogsService.querydate(logsParam);
        //定义一个总分(每一个年级+班级总得分)
        double totalScore = 0;
        if (logsList != null && logsList.size() > 0) {
            //取日志的表的总得分
            for (InspectionLogs logs : logsList) {
                totalScore += Float.parseFloat(logs.getTotalScore());
            }
            totalScore = Double.parseDouble(df.format(totalScore / logsList.size()));

        }
        return totalScore;

    }


    //数据统计详情列表
    @POST
    @Path("/criticism")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "批评表扬次数查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg criticism(Map<String, Object> params) {

        ReturnMsg msg = new ReturnMsg();

        try {
            Map<String, Object> resultParam = new HashMap<String, Object>();
            UserInf user = getUser();
            //获取当前日期周一所在的日期
            String monday = (String) params.get("monday");
            //获取当前日期周日所在的日期
            String sunday = (String) params.get("sunday");
            if (params.get("hxd") != null || params.get("hxd") != null) {
                resultParam.put("hxd", "hxd");//校务巡查
            }
            resultParam.put("clazzId", params.get("clazzId"));
            resultParam.put("monday", monday);
            resultParam.put("sunday", sunday);
            resultParam.put("schoolId", user.getAgentId());
            resultParam.put("templateName", params.get("templateName"));
            if (params.get("templateName").equals("校务巡查")) {
                //resultParam.put("itemCode", "2");//1教师 2学生 3管理
            }
            Set<Long> set = new LinkedHashSet<Long>();

            List<InspectionResult> querydate = inspectionResultService.queryNum(resultParam);
            for (InspectionResult data : querydate) {
                set.add(data.getAttrId());
            }
            List<Map<String, Object>> list = new ArrayList<>();
            for (Long long1 : set) {

                List<InspectionResult> que = new ArrayList<>();
                Map<String, Object> mm = new HashMap<>();
                mm.put("attrId", long1);
                for (InspectionResult data : querydate) {
                    if (long1.equals(data.getAttrId())) {
                        que.add(data);
                        mm.put("dictName", data.getDictName());
                        mm.put("attrName", data.getAttrName());
                        mm.put("categoryName", data.getCategoryName());
                    }
                }
                mm.put("sub", que);
                list.add(mm);
            }

            msg.setObj(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg.setFail("没有数据");
        }


        return msg;
    }


    //数据统计
    @POST
    @Path("/Allcriticism")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "数据统计", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg Allcriticism(InspectionLogs il) {
        ReturnMsg msg = new ReturnMsg();
        try {
            //用来存储每一天的数据统计
            List<InspectionLogs> list = new ArrayList<InspectionLogs>();
            Map<String, Object> m = null;
            InspectionLogs itl = null;


            //查询周一到周五的所有数据
            for (int i = 0; i <= 4; i++) {
                itl = new InspectionLogs();
                m = new HashMap<String, Object>();
                String day = DateUtils.getMonday(new Date(), i);

                m.put("day", day);
                m.put("templateId", il.getTemplateId());
                m.put("status", "0");

                //每一天的日志数据
                List<InspectionLogs> queryOnedate = inspectionLogsService.queryOnedate(m);
                if (queryOnedate == null || queryOnedate.size() == 0) {
                    itl.setCriticism("0");
                    itl.setPraise("0");
                    itl.setRemind("0");
                    itl.setDay(day);
                    list.add(itl);
                    continue;
                }

                int criticism = 0;
                int praise = 0;
                int remind = 0;

                //通过日志id查询所有的属性值
                Map<String, Object> m1 = null;
                for (InspectionLogs ilogs : queryOnedate) {
                    m1 = new HashMap<String, Object>();
                    m1.put("logsId", ilogs.getId());


                    List<InspectionResult> query = inspectionResultService.queryCPR(m1);

                    for (InspectionResult ir : query) {
                        if ("批评".equals(ir.getAttrValue())) {
                            criticism++;
                        } else if ("表扬".equals(ir.getAttrValue())) {
                            praise++;
                        } else if ("提醒".equals(ir.getAttrValue())) {
                            remind++;
                        }

                    }

                }
                itl.setCriticism("" + criticism);
                itl.setPraise("" + praise);
                itl.setRemind("" + remind);
                itl.setDay(day);

                list.add(itl);
            }

            msg.setObj(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg.setFail("数据查询异常");
        }

        return msg;


    }

}	
	
