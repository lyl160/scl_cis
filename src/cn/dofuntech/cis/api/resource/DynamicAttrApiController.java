package cn.dofuntech.cis.api.resource;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.admin.repository.domain.DynamicAttr;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.repository.domain.TeacherClazzRelInf;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.DynamicAttrService;
import cn.dofuntech.cis.admin.service.InspectionLogsService;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.cis.admin.service.TeacherClazzRelInfService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.core.entity.DefaultValue;
import cn.dofuntech.core.util.ResourceUtils;
import sun.misc.BASE64Encoder;

@Scope("prototype")
@Path("dynamicAttr")
@Api(value = "dynamicAttr", description = "模板动态属性查询")
@Produces(MediaType.APPLICATION_JSON)
public class DynamicAttrApiController extends BaseController {

    @Autowired
    private DynamicAttrService dynamicAttrService;
    @Autowired
    private InspectionLogsService inspectionLogsService;
    @Autowired
    private InspectionResultService inspectionResultService;
    @Autowired
    private ClazzInfService clazzInfService;
    @Autowired
    private TeacherClazzRelInfService teacherClazzRelInfService;

    /**
     * 数据结构如下
     * obj
     *    |-  dnameList[]
     *    |      |- did 字典id 1 2 3
     *    |      |- dname 字典name 教师 学生 管理
     *    |      |- log 历史提交记录
     *    |          |- result[] 提交记录属性明细分数
     *    |              |- attr 明细对应动态属性
     *    |      |- attrs[]
     *    |-  dynamicAttrList 动态属性合计
     *    |-  teachers[] 班级关联的教师
     * @param attrParam
     * @return
     */
    @POST
    @Path("/getId")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "模板动态属性查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public @ResponseBody
    ReturnMsg getId(DynamicAttr attrParam) {
        ReturnMsg msg = new ReturnMsg();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> attrQueryParam = new HashMap<String, Object>();
        attrQueryParam.put("templateId", attrParam.getTemplateId());
        attrQueryParam.put("seq", attrParam.getSeq());//2级分类 id
        try {
            //根据模板id查询动态属性
            ArrayList<Map> dnameList = new ArrayList<>();//最终返回对象
            List<DynamicAttr> dynamicAttrList = dynamicAttrService.query(attrQueryParam);
            HashMap<Long, String> dnameMap = new HashMap<>();
            HashMap<Long, List> attrMap = new HashMap<>();
            HashMap<Long, List<InspectionLogs>> logMap = new HashMap<>();
            List<TeacherClazzRelInf> teacherList = new ArrayList<>();
            for (DynamicAttr attr : dynamicAttrList) {
                //判断每一个属于什么类型 单选和多选进行解析  属性值和分数
                if (attr.getType().equals("003") || attr.getType().equals("004")) {
                    attr.setAttrOptions(attr.getAttrOption().split("\\|"));
                    attr.setScores(attr.getScore().split("\\|"));
                }
                if (StringUtils.isNotEmpty(attr.getUids())) {//后勤巡查支持多人
                    attr.setUidArr(attr.getUids().split(","));
                }
                if (attr.getTname().equals("校务巡查") || attr.getTname().equals("教师执勤")) {
                }
                dnameMap.put(attr.getDid(), attr.getDname());//1教师 2	学生 3管理
                //将属性值按did分组存放 方便后面使用
                if (attrMap.get(attr.getDid()) == null) {
                    ArrayList<DynamicAttr> subList = new ArrayList<>();
                    subList.add(attr);
                    attrMap.put(attr.getDid(), subList);
                } else {
                    attrMap.get(attr.getDid()).add(attr);
                }
            }

            //查询当个班级当天此二级分类下 已提交数据 并按照did分组
            String clazz = attrParam.getClazz();
            if (clazz !=null && !clazz.equals(DefaultValue.EMPTY)) {
                Map<String, Object> logsParam = new HashMap<String, Object>();
                logsParam.put("templateId", attrParam.getTemplateId());
                logsParam.put("schoolId", attrParam.getSchoolId());
                logsParam.put("clazz", clazz);
                logsParam.put("category2", attrParam.getSeq());
                logsParam.put("addTime", sdf.format(new Date()));//2级分类 id
                List<InspectionLogs> logs = inspectionLogsService.query(logsParam);
                for (InspectionLogs log : logs) {
                    Map<String, Object> resultParam = new HashMap<String, Object>();
                    resultParam.put("logsId", log.getId());
                    List<InspectionResult> resultList = inspectionResultService.query(resultParam);
                    for (InspectionResult result : resultList) {
                        result.setDynamicAttr(dynamicAttrService.get(result.getAttrId()));
                    }
                    log.setList(resultList);
                    log.setListimgs(getImgsList(log));

                    //getCategory3 就是 did
                    if (logMap.get(log.getCategory3()) == null) {
                        ArrayList<InspectionLogs> subList = new ArrayList<>();
                        subList.add(log);
                        logMap.put(log.getCategory3(), subList);
                    } else {
                        logMap.get(log.getCategory3()).add(log);
                    }
                }
            }

            //查询当个班级的老师
            if (clazz != null && !clazz.equals(DefaultValue.EMPTY)) {
                Map<String, Object> teacherClazzRelParam = new HashMap<String, Object>();
                teacherClazzRelParam.put("clazz", clazz);
                teacherClazzRelParam.put("schoolId", attrParam.getSchoolId());
                teacherList = teacherClazzRelInfService.query(teacherClazzRelParam);
            } else {

            }


            for (Long did : dnameMap.keySet()) {
                HashMap temp = new HashMap();
                temp.put("did", did);
                temp.put("dname", dnameMap.get(did));
                List attrSubList = attrMap.get(did);
                temp.put("attrs", attrSubList);
                if (logMap.get(did) != null) {
                    InspectionLogs log = logMap.get(did).get(0);
                    temp.put("log", log);
                }

                dnameList.add(temp);
            }
            resultMap.put("dnameList", dnameList);
            resultMap.put("dynamicAttrList", dynamicAttrList);
            resultMap.put("teachers", teacherList);
            msg.setObj(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setFail("动态属性查询异常" + e.getMessage());
        }

        return msg;
    }

    /**
     * 取出log中图片list，并转换成base64，用于反显
     * @param log
     * @return
     */
    private List<String> getImgsList(InspectionLogs log) {
        ArrayList<String> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(log.getImgs())) {
            String[] img = log.getImgs().split(",");
            if (img != null && img.length > 0) {
                for (String imgId : img) {
                    String base64Data = getBase64DataByPic(imgId);
                    result.add(base64Data);
                }
            }
        }
        return result;
    }

    /**
     * 根据图片相对地址将图片转成base64码
     *
     * @param fid
     * @return
     */
    private String getBase64DataByPic(String fid) {
        StringBuffer result = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        String path = ResourceUtils.getString("config", "system.file.path");
        File file;
        if (null == fid || "".equals(fid)) {
            file = new File(request.getSession().getServletContext().getRealPath("/") + "static/icons/auth/root.png");
        } else {
            sb.append(path).append(fid);
            file = new File(sb.toString());
            //判断文件是否存在如果不存在就返回默认图标
            if (!(file.exists() && file.canRead())) {
                file = new File(request.getSession().getServletContext().getRealPath("/") + "static/icons/auth/root.png");
            }
        }

        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        //System.out.println("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
        result.append("data:image/jpeg;base64,").append(encoder.encode(Objects.requireNonNull(data)));
        return result.toString().replaceAll("\r|\n", "");
    }


}
