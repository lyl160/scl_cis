package cn.dofuntech.cis.admin.controller;


import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.cis.admin.controller.base.ExportExcelUtil;
import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.admin.repository.domain.DynamicAttr;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.SchoolInf;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.DynamicAttrService;
import cn.dofuntech.cis.admin.service.InspectionCategoryService;
import cn.dofuntech.cis.admin.service.InspectionLogsService;
import cn.dofuntech.cis.admin.service.InspectionMessageService;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.cis.admin.service.SchoolInfService;
import cn.dofuntech.cis.bean.EnvUtil;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.DateUtils;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * InspectionLogsController
 */

@Controller
@RequestMapping("/inspectionLogs")
public class InspectionLogsController extends AdminController<InspectionLogs> {

    @Resource
    private InspectionLogsService inspectionLogsService;


    @Resource
    private InspectionResultService inspectionResultService;


    @Resource
    private InspectionCategoryService inspectionCategoryService;


    @Resource
    private ClazzInfService clazzInfService;


    @Resource
    private EnvUtil envUtil;

    @Resource
    private InspectionMessageService inspectionMessageService;

    @Resource
    private DynamicAttrService dynamicAttrService;
    @Resource
    private SchoolInfService schoolInfService;
    @Resource
    private InspectionTemplateService inspectionTemplateService;

    /**
     * 巡检管理查询
     * 校务巡查 后勤巡查调用
     *
     * @param params
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/query1")
    public @ResponseBody
    Map<String, Object> query(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        Map<String, Object> result = new HashMap<String, Object>();
        //  params.put("s_orgid",getUserInSession().getOrgId());


        if (page == null) {
            page = Paginator.DEFAULT_CURRENT_PAGE;
        }
        if (rows == null) {
            rows = Paginator.DEFAULT_PAGE_SIZE;
        }
        Paginator paginator = new Paginator(page, rows);

        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的模板
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        List<InspectionLogs> list = inspectionLogsService.query(params, paginator);

        for (InspectionLogs il : list) {

            il.setValue((String) params.get("name"));
        }

        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }


    //后勤管理详情
    @RequestMapping("/detail")
    public String detail(@RequestParam("id") Long id, @RequestParam("value") String value, HttpServletRequest request) {

        Map<String, Object> m = new HashMap<String, Object>();
        InspectionLogs ilog = inspectionLogsService.get(id);
        List<String> listimgs = new ArrayList<String>();
        if (StringUtils.isNotEmpty(ilog.getImgs())) {
            String[] img = ilog.getImgs().split(",");
            for (int i = 0; i < img.length; i++) {
                listimgs.add(img[i]);
            }
        }

        logger.debug("id" + ilog.getId());
        ilog.setValue(value);
        //根据巡查日志表logs_id得到所有动态属性
        m.put("logsId", id);
        try {
            List<InspectionResult> l = inspectionResultService.query(m);
            ilog.setList(l);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ilog.setListimgs(listimgs);
        request.setAttribute("entity", ilog);
        request.setAttribute("ilog", ilog);
        return "inspectionlogs/detail";
    }


    //批评表扬次数查询
    @RequestMapping("/criticism")
    public String criticism(InspectionLogs il, HttpServletRequest request) {
        Map<String, Object> m = new HashMap<String, Object>();

        //获取当前日期周一所在的日期
        String monday = DateUtils.getMonday(new Date(), 0);
        //获取当前日期周日所在的日期
        String sunday = DateUtils.getMonday(new Date(), 6);

        m.put("clazz", il.getClazz());
        m.put("grade", il.getGrade());
        m.put("monday", monday);
        m.put("sunday", sunday);
        m.put("templateId", il.getTeamId());

        List<InspectionLogs> querydate = inspectionLogsService.querydate(m);


        int criticism = 0;
        int praise = 0;
        int remind = 0;

        //通过日志id查询所有的属性值
        Map<String, Object> m1 = null;
        for (InspectionLogs ilogs : querydate) {
            m1 = new HashMap<String, Object>();
            m1.put("logsId ", ilogs.getId());

            List<InspectionResult> query = inspectionResultService.query(m1);

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

        request.setAttribute("criticism", criticism);
        request.setAttribute("praise", praise);
        request.setAttribute("remind", remind);


        return null;
    }

    //一日数据统计
    @RequestMapping("/oneDay")
    @ResponseBody
    public Map Allcriticism() {
        Map<String, Object> msg = new HashMap<String, Object>();
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
                //每一天的日志数据
                List<InspectionLogs> queryOnedate = inspectionLogsService.queryOnedateAll(m);
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
                    m1.put("logsId ", ilogs.getId());


                    List<InspectionResult> query = inspectionResultService.query(m1);

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

            msg.put("list", list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return msg;


    }

    /**
     * 校内执勤  校外执勤调用
     * @param params
     * @param page
     * @param rows
     * @return
     */

    @RequestMapping(value = "/query2", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> query2(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        Map<String, Object> result = new HashMap<String, Object>();
        //  params.put("s_orgid",getUserInSession().getOrgId());
        if (page == null) {
            page = Paginator.DEFAULT_CURRENT_PAGE;
        }
        if (rows == null) {
            rows = Paginator.DEFAULT_PAGE_SIZE;
        }
        Paginator paginator = new Paginator(page, rows);

        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的一日综述
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        params.put("title", params.get("name"));
        List<InspectionMessage> list = inspectionMessageService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);
        return result;
    }

    @RequestMapping(value = "logs/manage1", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> manage1(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (page == null) {
            page = Paginator.DEFAULT_CURRENT_PAGE;
        }
        if (rows == null) {
            rows = Paginator.DEFAULT_PAGE_SIZE;
        }
        Paginator paginator = new Paginator(page, rows);

        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的一日综述
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        params.put("title", params.get("title"));
        List<InspectionMessage> list = inspectionMessageService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);
        return result;
    }

    @RequestMapping(value = "/one")
    public String one(HttpServletRequest request) {
        String schoolId = null;
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        if (!uai.getRoleId().equals("9")) {
            schoolId = uai.getAgentId();
        }

        Map schoolInfParam = new HashMap();
        schoolInfParam.put("id", schoolId);
        request.setAttribute("schoolList", schoolInfService.query(schoolInfParam));
        return "inspectionlogs/oneDay";
    }


    @RequestMapping(value = "logs/oneday", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object>
    oneday(@RequestParam Map<String, Object> params) {
        String schoolId = (String) params.get("schoolId");;
        Map<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long currentTimeMillis = System.currentTimeMillis();
        //大校长查看全部，其余查看自己学校
        //UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //if (!uai.getRoleId().equals("9")) {
        //    schoolId = uai.getAgentId();
        //}
        List<DynamicAttr> attrList = new ArrayList<>();
        Map clazzParam = new HashMap();
        clazzParam.put("schoolId", schoolId);
        clazzParam.put("status", "0");
        List<ClazzInf> clazzList = clazzInfService.query(clazzParam);
        List<SchoolInf> schoolList = schoolInfService.query();

        InspectionResult singleResult = null;
        List<InspectionResult> list = new ArrayList<InspectionResult>();
        Map<String, Object> resultParam = new HashMap<String, Object>();


        if ("校务巡查".equals(params.get("templateName"))) {
            resultParam.put("itemCode", "2");//1教师 2	学生 3管理
        }

        Map templateParam = new HashMap();
        templateParam.put("name", params.get("templateName"));
        templateParam.put("schoolId", schoolId);
        InspectionTemplate template = inspectionTemplateService.get(templateParam);
        resultParam.put("templateId", template.getId());

        if (params.get("ksTime") != null && params.get("ksTime") != "" && params.get("jsTime") != null && params.get("jsTime") != "") {
            resultParam.put("ksTime", params.get("ksTime"));
            resultParam.put("jsTime", params.get("jsTime"));
        } else {
            resultParam.put("ksTime", sdf.format(currentTimeMillis));
            resultParam.put("jsTime", sdf.format(currentTimeMillis));
        }
        resultParam.put("schoolId", schoolId);
        List<InspectionResult> resultList = inspectionResultService.querySum(resultParam);
        if (resultList != null && resultList.size() > 0) {
            Map<Long,String> attrMap = new LinkedHashMap();
            for (ClazzInf clazz : clazzList) {
                List<InspectionResult> singleList = new ArrayList<InspectionResult>();
                float totalScore = 0;
                for (InspectionResult inspectionResult : resultList) {
                    //按照class id分组
                    if (clazz.getId().equals(inspectionResult.getClazzId())) {
                        singleList.add(inspectionResult);
                        totalScore += inspectionResult.getSum();
                        //收集attrId
                        attrMap.put(inspectionResult.getAttrId(), inspectionResult.getAttrName());
                    }
                }

                if (singleList.size() > 0) {
                    singleResult = new InspectionResult();
                    singleResult.setClazz(clazz.getClazz());
                    singleResult.setList(singleList);
                    singleResult.setItemScore(totalScore + "");
                    for (SchoolInf schoolInf : schoolList) {
                        if (schoolInf.getId().equals(clazz.getSchoolId())) {
                            singleResult.setSchoolName(schoolInf.getSchoolName());
                        }
                    }
                    list.add(singleResult);
                }
            }

            for (Long attrId : attrMap.keySet()) {
                DynamicAttr dynamicAttr = dynamicAttrService.get(attrId);
                if (dynamicAttr != null) {
                    attrList.add(dynamicAttr);
                } else {
                    dynamicAttr = new DynamicAttr();
                    dynamicAttr.setId(attrId);
                    dynamicAttr.setName(attrMap.get(attrId));
                    dynamicAttr.setScore("10|10|10");
                    attrList.add(dynamicAttr);

                }
            }
        }
        result.put("list", list);//结果全集
        result.put("attr", attrList);//属性全集
        return result;
    }

    /**
     * 后勤巡查明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport4Hq")
    public void msgExport4Hq(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("后勤巡查明细导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            String tpl = request.getParameter("tpl");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            List<InspectionLogs> logsList = inspectionLogsService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("班级");
            headers.add("年级");
            headers.add("提交人");
            headers.add("总得分");
            headers.add("评价");

            headers.add("提交日期");
            headers.add("团队");
            headers.add("学校");
            headers.add("图片");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (InspectionLogs logs : logsList) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                xdata_temp.add(logs.getClazz());
                xdata_temp.add(logs.getGrade());
                xdata_temp.add(logs.getCzy());
                xdata_temp.add(logs.getTotalScore());
                xdata_temp.add(logs.getDesc1());

                if (logs.getAddTime() != null) {
                    xdata_temp.add(sdf.format(logs.getAddTime()));
                } else {
                    xdata_temp.add("");
                }
                xdata_temp.add(logs.getTeamName());
                xdata_temp.add(logs.getSchoolName());
                if (StringUtils.isNotEmpty(logs.getImgs())) {
                    String[] imgArray = logs.getImgs().split(",");
                    StringBuilder imgsNew = new StringBuilder();
                    for (String img : imgArray) {
                        //imgsNew.append(ROOTPAHH).append(img).append(",");
                        xdata_temp.add(ROOTPAHH + img);
                    }
                }else {
                    xdata_temp.add("");
                }
                index++;
                xdataMain.add(xdata_temp);
            }
            //订单主表信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            OutputStream out = response.getOutputStream();
            String title = "后勤巡查明细";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("后勤巡查明细导出成功=====");
        } catch (Exception ex) {
            logger.error("后勤巡查明细数据导出异常：{}", ex.getMessage(), ex);
        }
    }


    /**
     * 校务巡查明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport4Xw")
    public void msgExport4Xw(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("校务巡查明细导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            String tpl = request.getParameter("tpl");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            List<InspectionLogs> logsList = inspectionLogsService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("班级");
            headers.add("年级");
            headers.add("提交人");
            headers.add("总得分");
            headers.add("评价");
            headers.add("提交日期");
            headers.add("团队");
            headers.add("学校");
            headers.add("图片");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (InspectionLogs logs : logsList) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                xdata_temp.add(logs.getClazz());
                xdata_temp.add(logs.getGrade());
                xdata_temp.add(logs.getCzy());
                xdata_temp.add(logs.getTotalScore());
                xdata_temp.add(logs.getDesc1());

                if (logs.getAddTime() != null) {
                    xdata_temp.add(sdf.format(logs.getAddTime()));
                } else {
                    xdata_temp.add("");
                }
                xdata_temp.add(logs.getTeamName());
                xdata_temp.add(logs.getSchoolName());
                if (StringUtils.isNotEmpty(logs.getImgs())) {
                    String[] imgArray = logs.getImgs().split(",");
                    StringBuilder imgsNew = new StringBuilder();
                    for (String img : imgArray) {
                        //imgsNew.append(ROOTPAHH).append(img).append("\r\n");
                        xdata_temp.add(ROOTPAHH+img);
                    }

                }else {
                    xdata_temp.add("");
                }
                index++;
                xdataMain.add(xdata_temp);
            }
            //订单主表信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            OutputStream out = response.getOutputStream();
            String title = "校务巡查明细";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("校务巡查明细导出成功=====");
        } catch (Exception ex) {
            logger.error("校务巡查明细数据导出异常：{}", ex.getMessage(), ex);
        }
    }

}
