package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "后勤巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg add(InspectionLogs ilogs) {
        ReturnMsg msg = new ReturnMsg();
        Timestamp date = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (ilogs.getTeamId() != null) {
            //查询该团队下的所有班级
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("teamId", ilogs.getTeamId());
            map.put("status", "0");
            List<ClazzInf> query = clazzInfService.query(map);
            for (ClazzInf c : query) {
                try {
                    //图片上传
                    if (ilogs.getStr1() != null) {
                        String uploadImg = ilogs.getStr1();
                        String fileName = ImageUpload.uploadImgByBase64(uploadImg, envUtil.getSystemFilePath());
                        log.debug("图片开始上传" + fileName);
                        ilogs.setImgs(fileName);
                    }
                    //获取动态属性成果表数据
                    String[] attrScore = ilogs.getStr().split("@");
                    float total_score = 0;//总得分
                    float score2 = 0;
                    for (int i = 0; i < attrScore.length; i++) {
                        String score = attrScore[i].split(":")[3];//单项得分
                        total_score += Float.parseFloat(score);

                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String nowDay = sdf.format(date.getTime());
                    Map<String, Object> m1 = new HashMap<String, Object>();
                    m1.put("addTime", nowDay);
                    m1.put("clazzId", c.getId());
                    m1.put("templateId", ilogs.getTemplateId());
                    InspectionLogs log = inspectionLogsService.get(m1);
                    if (log == null) {//新增
                        //数据保存到日志表
                        // 巡检人id
                        long czyId = (long) getUser().getId();
                        ilogs.setCzyId(czyId);
                        // 巡检人名称
                        String czy = getUser().getUserName();
                        ilogs.setCzy(czy);

                        // 学校id
                        ilogs.setSchoolId(getUser().getAgentId());

                        //新增时间
                        ilogs.setAddTime(date);
                        ilogs.setGrade(c.getGrade());
                        ilogs.setClazz(c.getClazz());
                        ilogs.setClazzId(c.getId());
                        ilogs.setId(null);
                        ilogs.setTotalScore("" + total_score);
                        ilogs.setTeamId(c.getTeamId());
                        ilogs.setTeamName(c.getTeamName());
                        inspectionLogsService.insert(ilogs);
                        InspectionResult ir = null;

                        Map<String, Object> m2 = new HashMap<String, Object>();
                        for (int i = 0; i < attrScore.length; i++) {
                            ir = new InspectionResult();
                            ir.setLogsId(ilogs.getId());
                            String attrName = attrScore[i].split(":")[0];//动态属性名称
                            String attrValue = attrScore[i].split(":")[1];//动态属性值
                            String attrId = attrScore[i].split(":")[2];//动态属性id
                            String score = attrScore[i].split(":")[3];//单项得分
                            //单项得分
                            ir.setItemScore(score);
                            //项目代码
                            ir.setAttrName(attrName);
                            ir.setAttrValue(attrValue);
                            ir.setAttrId(NumberUtils.toLong(attrId));
                            ir.setAddTime(date);
                            //数据保存到动态属性成果表
                            m2.put("addTime", nowDay);
                            m2.put("logsId", ilogs.getId());
                            m2.put("attrId", attrId);
                            inspectionResultService.insert(ir);
                        }
                    } else {
                        InspectionResult ir = null;

                        Map<String, Object> resultParam = new HashMap<String, Object>();
                        for (int i = 0; i < attrScore.length; i++) {
                            ir = new InspectionResult();
                            ir.setLogsId(ilogs.getId());
                            String attrName = attrScore[i].split(":")[0];//动态属性名称
                            String attrValue = attrScore[i].split(":")[1];//动态属性值
                            String attrId = attrScore[i].split(":")[2];//动态属性id
                            String score = attrScore[i].split(":")[3];//单项得分
                            //单项得分
                            ir.setItemScore(score);
                            //项目代码
                            ir.setAttrName(attrName);
                            ir.setAttrValue(attrValue);
                            ir.setAttrId(NumberUtils.toLong(attrId));
                            ir.setAddTime(date);
                            //数据保存到动态属性成果表
                            resultParam.put("addTime", nowDay);
                            resultParam.put("logsId", log.getId());
                            resultParam.put("attrId", attrId);
                            InspectionResult queryir = inspectionResultService.get(resultParam);
                            if (queryir == null) {
                                ir.setLogsId(log.getId());
                                inspectionResultService.insert(ir);
                            } else {
                                ir.setId(queryir.getId());
                                ir.setLogsId(queryir.getLogsId());
                                inspectionResultService.update(ir);
                            }
                        }
                        //数据保存到日志表
                        // 巡检人id
                        long czyId = (long) getUser().getId();
                        ilogs.setCzyId(czyId);
                        // 巡检人名称
                        if (!(log.getCzy().contains(getUser().getUserName()))) {
                            String czy = log.getCzy() + "," + getUser().getUserName();
                            ilogs.setCzy(czy);
                        } else {
                            String czy = log.getCzy();
                            ilogs.setCzy(czy);
                        }
                        // 学校id
                        ilogs.setSchoolId(getUser().getAgentId());

                        //新增时间
                        ilogs.setAddTime(date);
                        ilogs.setGrade(c.getGrade());
                        ilogs.setClazz(c.getClazz());
                        ilogs.setClazzId(c.getId());
                        ilogs.setTeamId(c.getTeamId());
                        ilogs.setTeamName(c.getTeamName());
                        ilogs.setId(log.getId());
                        Map<String, Object> m3 = new HashMap<String, Object>();
                        m3.put("logsId", ilogs.getId());
                        List<InspectionResult> queryall = inspectionResultService.query(m3);
                        for (InspectionResult irlist : queryall) {
                            score2 = score2 + Float.parseFloat(irlist.getItemScore());
                        }
                        ilogs.setTotalScore("" + score2);
                        inspectionLogsService.update(ilogs);
                    }


                    msg.setSuccess("提交成功");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    msg.setFail("提交失败");
                }
            }
        } else {
            try {
                //图片上传
                if (ilogs.getStr1() != null) {
                    String uploadImg = ilogs.getStr1();
                    String fileName = ImageUpload.uploadImgByBase64(uploadImg, envUtil.getSystemFilePath());
                    log.debug("图片开始上传" + fileName);
                    ilogs.setImgs(fileName);
                }
                //获取动态属性成果表数据
                String[] s = ilogs.getStr().split("@");
                float total_score = 0;//总得分
                float score2 = 0;
                for (int i = 0; i < s.length; i++) {
                    String score = s[i].split(":")[3];//单项得分
                    total_score += Float.parseFloat(score);

                }

                SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
                String nowDay = s2.format(date.getTime());
                Map<String, Object> m1 = new HashMap<String, Object>();
                m1.put("addTime", nowDay);
                m1.put("clazzId", ilogs.getClazzId());
                m1.put("templateId", ilogs.getTemplateId());
                InspectionLogs log = inspectionLogsService.get(m1);
                if (log == null) {
                    //数据保存到日志表
                    // 巡检人id
                    long czyId = (long) getUser().getId();
                    ilogs.setCzyId(czyId);
                    // 巡检人名称
                    String czy = getUser().getUserName();
                    ilogs.setCzy(czy);

                    // 学校id
                    ilogs.setSchoolId(getUser().getAgentId());

                    //新增时间
                    ilogs.setAddTime(date);
                    ilogs.setGrade(ilogs.getGrade());
                    ilogs.setClazz(ilogs.getClazz());
                    ilogs.setClazzId(ilogs.getClazzId());
                    ilogs.setTeamId(ilogs.getTeamId());
                    ilogs.setId(null);
                    ilogs.setTotalScore("" + total_score);
                    inspectionLogsService.insert(ilogs);
                    InspectionResult ir = null;

                    Map<String, Object> m2 = new HashMap<String, Object>();
                    for (int i = 0; i < s.length; i++) {
                        ir = new InspectionResult();
                        ir.setLogsId(ilogs.getId());
                        String attrName = s[i].split(":")[0];//动态属性名称
                        String attrValue = s[i].split(":")[1];//动态属性值
                        String attrId = s[i].split(":")[2];//动态属性id
                        String score = s[i].split(":")[3];//单项得分
                        //单项得分
                        ir.setItemScore(score);
                        //项目代码
                        ir.setAttrName(attrName);
                        ir.setAttrValue(attrValue);
                        ir.setAttrId(NumberUtils.toLong(attrId));
                        ir.setAddTime(date);
                        //数据保存到动态属性成果表
                        m2.put("addTime", nowDay);
                        m2.put("logsId", ilogs.getId());
                        m2.put("attrId", attrId);
                        inspectionResultService.insert(ir);
                    }
                } else {
                    InspectionResult ir = null;

                    Map<String, Object> m2 = new HashMap<String, Object>();
                    for (int i = 0; i < s.length; i++) {
                        ir = new InspectionResult();
                        ir.setLogsId(ilogs.getId());
                        String attrName = s[i].split(":")[0];//动态属性名称
                        String attrValue = s[i].split(":")[1];//动态属性值
                        String attrId = s[i].split(":")[2];//动态属性id
                        String score = s[i].split(":")[3];//单项得分
                        //单项得分
                        ir.setItemScore(score);
                        //项目代码
                        ir.setAttrName(attrName);
                        ir.setAttrValue(attrValue);
                        ir.setAttrId(NumberUtils.toLong(attrId));
                        ir.setAddTime(date);
                        //数据保存到动态属性成果表
                        m2.put("addTime", nowDay);
                        m2.put("logsId", log.getId());
                        m2.put("attrId", attrId);
                        InspectionResult queryir = inspectionResultService.get(m2);
                        if (queryir == null) {
                            ir.setLogsId(log.getId());
                            inspectionResultService.insert(ir);
                        } else {
                            ir.setId(queryir.getId());
                            ir.setLogsId(queryir.getLogsId());
                            inspectionResultService.update(ir);
                        }
                    }
                    //数据保存到日志表
                    // 巡检人id
                    long czyId = (long) getUser().getId();
                    ilogs.setCzyId(czyId);
                    // 巡检人名称
                    if (!(log.getCzy().contains(getUser().getUserName()))) {
                        String czy = log.getCzy() + "," + getUser().getUserName();
                        ilogs.setCzy(czy);
                    } else {
                        String czy = log.getCzy();
                        ilogs.setCzy(czy);
                    }
                    // 学校id
                    ilogs.setSchoolId(getUser().getAgentId());

                    //新增时间
                    ilogs.setAddTime(date);
                    ilogs.setGrade(ilogs.getGrade());
                    ilogs.setClazz(ilogs.getClazz());
                    ilogs.setClazzId(ilogs.getClazzId());
                    ilogs.setTeamId(ilogs.getTeamId());
                    ilogs.setId(log.getId());
                    Map<String, Object> m3 = new HashMap<String, Object>();
                    m3.put("logsId", ilogs.getId());
                    List<InspectionResult> queryall = inspectionResultService.query(m3);
                    for (InspectionResult irlist : queryall) {
                        score2 = score2 + Float.parseFloat(irlist.getItemScore());
                    }
                    ilogs.setTotalScore("" + score2);
                    inspectionLogsService.update(ilogs);
                }


                msg.setSuccess("提交成功");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                msg.setFail("提交失败");
            }
        }
        return msg;
    }


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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("teamId", logs.getTeamId());
            map.put("status", "0");
            List<ClazzInf> query = clazzInfService.query(map);
            for (ClazzInf c : query) {
                try {
                    //图片上传
                    if (logs.getListimgs() != null && logs.getListimgs().size()>0) {
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
                    //获取动态属性成果表数据
                    String[] attrScore = logs.getStr().split("@");
                    float totalScore = 0;//总得分
                    for (int i = 0; i < attrScore.length; i++) {
                        String score = attrScore[i].split(":")[3];//单项得分
                        totalScore += Float.parseFloat(score);

                    }
                    if (StringUtils.isNotEmpty(logs.getTeacherStr())) {
                        logs.setTeacherId(logs.getTeacherStr().split(":")[0]);
                        logs.setTeacherName(logs.getTeacherStr().split(":")[1]);
                    }
                    logs.setTotalScore("" + totalScore);
                    // 再 数据保存到日志表
                    logs.setGrade(c.getGrade());
                    logs.setClazz(c.getClazz());
                    logs.setClazzId(c.getId());
                    logs.setId(null);

                    inspectionLogsService.insert(logs);
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
                    }
                    msg.setSuccess("提交成功");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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
                if (logs.getListimgs() != null && logs.getListimgs().size()>0) {
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
                //获取动态属性成果表数据
                String[] s = logs.getStr().split("@");
                float total_score = 0;//总得分
                for (int i = 0; i < s.length; i++) {
                    String score = s[i].split(":")[3];//单项得分
                    total_score += Float.parseFloat(score);

                }
                logs.setTotalScore("" + total_score);

                if (StringUtils.isNotEmpty(logs.getTeacherStr())) {
                    logs.setTeacherId(logs.getTeacherStr().split(":")[0]);
                    logs.setTeacherName(logs.getTeacherStr().split(":")[1]);
                }
                //先删除 再 数据保存到日志表
                inspectionLogsService.deleteTodayLogsByClazzId(logs);
                inspectionLogsService.insert(logs);
                InspectionResult result = null;
                for (int i = 0; i < s.length; i++) {
                    result = new InspectionResult();
                    result.setLogsId(logs.getId());
                    result.setItemScore(s[i].split(":")[3]);//单项得分
                    result.setItemCode(s[i].split(":")[4]);//项目代码
                    result.setAttrName(s[i].split(":")[0]);//动态属性名称
                    result.setAttrValue(s[i].split(":")[1]);//动态属性值
                    result.setAttrId(NumberUtils.toLong(s[i].split(":")[2]));//动态属性id
                    result.setSchoolId(Long.parseLong(logs.getSchoolId()));
                    result.setAddTime(date);
                    //数据保存到动态属性成果表
                    inspectionResultService.insert(result);
                }
                msg.setSuccess("提交成功");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                msg.setFail("提交失败");
                ;
            }
        }
        return msg;
    }


    @POST
    @Path("/add3")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "护校队讯巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg add3(InspectionLogs il) {
        ReturnMsg msg = new ReturnMsg();
        Timestamp date = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 巡检人id
        long czyId = (long) getUser().getId();
        il.setCzyId(czyId);
        // 巡检人名称
        String czy = getUser().getUserName();
        il.setCzy(czy);

        // 学校id
        il.setSchoolId(getUser().getAgentId());
        // 新增时间
        il.setAddTime(date);
        log.debug("开始时间为" + date);

        try {
            //获取动态属性成果表数据
            String[] s = il.getStr().split("@");
            float total_score = 0;//总得分
            for (int i = 0; i < s.length; i++) {
                String score = s[i].split(":")[3];//单项得分
                total_score += Float.parseFloat(score);

            }
            il.setTotalScore("" + total_score);

            // 数据保存到日志表
            inspectionLogsService.insert(il);

            InspectionResult ir = null;
            for (int i = 0; i < s.length; i++) {
                ir = new InspectionResult();
                ir.setLogsId(il.getId());
                String attrName = s[i].split(":")[0];// 动态属性名称
                String attrValue = s[i].split(":")[1];// 动态属性值
                String attrId = s[i].split(":")[2];// 动态属性id
                String score = s[i].split(":")[3];//单项得分
                String item_code = s[i].split(":")[4];//项目代码
                //项目得分
                ir.setItemScore(score);
                //项目代码
                ir.setItemCode(item_code);
                ir.setAttrName(attrName);
                ir.setAttrValue(attrValue);
                ir.setAttrId(NumberUtils.toLong(attrId));
                ir.setAddTime(date);
                // 数据保存到动态属性成果表
                inspectionResultService.insert(ir);
            }
            msg.setSuccess("提交成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg.setFail("提交失败");
        }

        return msg;
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
        //String monday = DateUtils.getMonday(new Date(), 0);//获取当前日期周一所在的日期
        //String sunday = DateUtils.getMonday(new Date(), 6);//获取当前日期周日所在的日期
        //String prevMonday = DateUtils.getMonday(new Date(), -7);//获取上周一所在的日期
        //String prevSunday = DateUtils.getMonday(new Date(), -1);//获取上周日期周日所在的日期
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

                    Double currentScore = getClazzTotalScore(clazzName, grade, monday, sunday, log.getTemplateId(),schoolId);
                    clazz.setScoreNum(currentScore + "");

                    //Double prevScore = getClazzTotalScore(clazzName, grade, prevMonday, prevSunday, log.getTemplateId());
                    ////1、升 2降 0不变
                    //if (prevScore > currentScore) {
                    //    clazz.setEs(2);
                    //} else if (prevScore < currentScore) {
                    //    clazz.setEs(1);
                    //} else {
                    //    clazz.setEs(0);
                    //}

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
     * @param clazzName
     * @param grade
     * @param monday yyyy-MM-dd 开始时间
     * @param sunday yyyy-MM-dd 结束时间
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


    //一周汇总查询
    @POST
    @Path("/hebdomadhxd")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "护校队一周汇总查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public @ResponseBody
    ReturnMsg hebdomadhxd(InspectionLogs itl) {
        ReturnMsg msg = new ReturnMsg();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            ;
            Map<String, Object> m = new HashMap<String, Object>();
            ;
            Map<String, Object> m1 = new HashMap<String, Object>();
            //获取当前日期周一所在的日期
            String monday = DateUtils.getMonday(new Date(), 0);
            //获取当前日期周日所在的日期
            String sunday = DateUtils.getMonday(new Date(), 6);
            //获取上周一所在的日期
            String monday1 = DateUtils.getMonday(new Date(), -7);
            //获取上周日期周日所在的日期
            String sunday1 = DateUtils.getMonday(new Date(), -1);

            m.put("monday", monday);
            m.put("sunday", sunday);
            m.put("templateId", itl.getTemplateId());
            m1.put("monday", monday1);
            m1.put("sunday", sunday1);
            m1.put("templateId", itl.getTemplateId());


            List<InspectionLogs> querydate = inspectionLogsService.querydate(m);


            if (querydate == null || querydate.size() == 0) {
                map.put("total_score", 0);
                map.put("seq", 0);
                msg.setObj(map);
                return msg;
            }


            //定义一个总分(每一个年级+班级总得分)
            float total_score = 0;

            //取日志的表的总得分
            for (InspectionLogs il : querydate) {
                total_score += Float.parseFloat(il.getTotalScore());

            }
            map.put("total_score", total_score);
            //定义一个总分(每一个年级+班级总得分)
            float total_score1 = 0;
            try {
                //查询出上周一周的日志表数据
                List<InspectionLogs> querydate1 = inspectionLogsService.querydate(m1);
                if (querydate1 == null) {
                    map.put("seq", 0);
                    msg.setObj(map);
                    return msg;
                }
                //取上周日志的表的总得分
                for (InspectionLogs il : querydate1) {
                    total_score1 += Float.parseFloat(il.getTotalScore());

                }
                total_score1 = (float) (total_score1 * 1.0 / querydate1.size());
                //总分下降
                if (total_score1 > total_score) {
                    map.put("seq", 1);
                } else if (total_score1 < total_score) {
                    map.put("seq", 2);


                } else {
                    map.put("seq", 0);

                }
            } catch (Exception e) {
                msg.setFail("查询失败");
                log.error("查询数据异常:{}", e.getMessage(), e);
            }

            msg.setObj(map);


        } catch (Exception e) {
            msg.setFail("查询失败");
            log.error("查询数据异常:{}", e.getMessage(), e);
        }


        return msg;


    }

    //批评表扬次数查询
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

	
	
	
	/*// 校务巡查新增
	@POST
	@Path("/add2")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	@ApiOperation(value = "校务巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public @ResponseBody ReturnMsg add2(InspectionLogs il) {
		ReturnMsg msg = new ReturnMsg();
		Timestamp date = new Timestamp(System.currentTimeMillis());
		log.debug("图片开始上传" + envUtil.getSystemFilePath());
		// 巡检人id

		// 巡检人名称

		// 学校id

		// 新增时间
		il.setAddTime(date);

		try {
			// 图片上传
			String[] uploadImg = il.getStr1().split(":");

			for (int i = 0; i < uploadImg.length; i++) {
				String fileName = ImageUpload.uploadImgByBase64(uploadImg[i], envUtil.getSystemFilePath());
				log.debug("图片继续开始上传" + fileName);
				String realName = FileUtils.getFullName(fileName);
				// 准备上传
				UploadManager uploadManager = new UploadManager();
				Response res = uploadManager.put(new File(envUtil.getSystemFilePath() + fileName), realName,
						Config.QINIU_UTOKEN);
				log.debug(" -- 远程上传文件返回：" + res.bodyString());
				// if (status == HttpStatus.SC_OK) {
				String realUrl = Config.QINIU_DOMAIN + "/" + realName;

				log.debug(" -- 文件URL：" + realUrl);

				if (fileName.length() > 0) { // 上传成功
					msg.setObj(realUrl);
					msg.setSuccess();
				} else {
					msg.setFail("上传失败");
				}
			}
			// 数据保存到日志表
			inspectionLogsService.insert(il);
			Map<String, Object> result1 = new HashMap<String, Object>();
			result1.put("clazz", il.getClazz());
			result1.put("grade", il.getGrade());
			result1.put("templateId", "2");
			SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result1.put("addTime", s1.format(date.getTime() + 1000));
			// 根据班级 年级 模板id 新增时间 查询唯一一条数据 id
			InspectionLogs query = inspectionLogsService.get(result1);
			if (query == null) {
				msg.setFail("提交失败");
				return msg;
			}
			// 获取动态属性成果表数据
			String[] s = il.getStr().split("@");

			InspectionResult ir = null;
			for (int i = 0; i < s.length; i++) {

				ir = new InspectionResult();
				ir.setLogsId(query.getId());
				String attrName = s[i].split(":")[0];// 动态属性名称
				String attrValue = s[i].split(":")[1];// 动态属性值
				String attrId = s[i].split(":")[2];// 动态属性id
				ir.setAttrName(attrName);
				ir.setAttrValue(attrValue);
				ir.setAttrId(NumberUtils.toLong(attrId));
				ir.setAddTime(date);
				// 数据保存到动态属性成果表
				inspectionResultService.insert(ir);
			}
			msg.setFail("提交成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("提交失败");
			;
		}

		return msg;
	}

	// 护校队巡查新增
	@POST
	@Path("/add3")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	@ApiOperation(value = "护校队巡查新增", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg add3(InspectionLogs il) {
		ReturnMsg msg = new ReturnMsg();
		Timestamp date = new Timestamp(System.currentTimeMillis());
		// 巡检人id

		// 巡检人名称

		// 学校id

		// 新增时间
		il.setAddTime(date);
		log.debug("开始时间为" + date);

		try {

			// 数据保存到日志表
			inspectionLogsService.insert(il);
			Map<String, Object> result1 = new HashMap<String, Object>();
			result1.put("clazz", il.getClazz());
			result1.put("grade", il.getGrade());
			result1.put("templateId", il.getTemplateId());
			SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result1.put("addTime", s1.format(date.getTime() + 1000));
			// 根据班级 年级 模板id 新增时间 查询唯一一条数据 id
			log.debug("最后时间为" + s1.format(date.getTime() + 1000));
			InspectionLogs query = inspectionLogsService.get(result1);
			if (query == null) {
				msg.setFail("提交失败");

				return msg;
			}
			// 获取动态属性成果表数据
			String[] s = il.getStr().split("@");

			InspectionResult ir = null;
			for (int i = 0; i < s.length; i++) {

				ir = new InspectionResult();
				ir.setLogsId(query.getId());
				String attrName = s[i].split(":")[0];// 动态属性名称
				String attrValue = s[i].split(":")[1];// 动态属性值
				String attrId = s[i].split(":")[2];// 动态属性id
				ir.setAttrName(attrName);
				ir.setAttrValue(attrValue);
				ir.setAttrId(NumberUtils.toLong(attrId));
				ir.setAddTime(date);
				// 数据保存到动态属性成果表
				inspectionResultService.insert(ir);
			}
			msg.setFail("提交成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("提交失败");
		}

		return msg;
	}*/

}	
	
