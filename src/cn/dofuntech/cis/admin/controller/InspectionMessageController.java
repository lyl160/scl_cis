package cn.dofuntech.cis.admin.controller;

import cn.dofuntech.cis.admin.controller.base.ExportExcelUtil;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.repository.domain.NoticeMx;
import cn.dofuntech.cis.admin.service.InspectionMessageService;
import cn.dofuntech.cis.admin.service.NoticeMxService;
import cn.dofuntech.cis.admin.service.SchoolInfService;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 * InspectionMessageController
 */

@Controller
@RequestMapping("/inspectionMessage")
public class InspectionMessageController extends AdminController<InspectionMessage> {

    @Resource
    private InspectionMessageService inspectionMessageService;
    @Resource
    private SchoolInfService schoolInfService;
    @Resource
    private NoticeMxService noticeMxService;



    @RequestMapping("/Detail")
    public String Detail(HttpServletRequest request, Long id, Model map) {
        InspectionMessage inspectionMessage = inspectionMessageService.get(id);
        String[] img = inspectionMessage.getImgs().split(",");
        List<String> listimgs = new ArrayList<String>();
        for (int i = 0; i < img.length; i++) {
            listimgs.add(img[i]);
        }
        inspectionMessage.setListimgs(listimgs);
        map.addAttribute("entity", inspectionMessage);
        return "inspectionmessage/detail";


    }

    /**
     * 一日综述数据列表
     *
     * @param params
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/queryAll")
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
        //大校长为9可以查看所有的一日综述
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        params.put("notin45", "true");
        List<InspectionMessage> list = inspectionMessageService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);
        return result;
    }

    /**
     * 校内执勤明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport4Js")
    public void msgExport4Js(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("校内执勤明细导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            List<InspectionMessage> msgMappings = inspectionMessageService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("内容描述");
            headers.add("提交人");

            headers.add("提交日期");
            headers.add("地点");
            headers.add("学校");
            headers.add("图片");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (InspectionMessage bm : msgMappings) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                xdata_temp.add(bm.getRemark());
                xdata_temp.add(bm.getUserName());

                xdata_temp.add(bm.getFmtDate());
                xdata_temp.add(bm.getPlace());
                xdata_temp.add(bm.getSchoolName());
                if (StringUtils.isNotEmpty(bm.getImgs())) {
                    String[] imgArray = bm.getImgs().split(",");
                    StringBuilder imgsNew = new StringBuilder();
                    for (String img : imgArray) {
                        //imgsNew.append(ROOTPAHH).append(img).append(",");
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
            String title = "校内执勤明细";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("校内执勤明细导出成功=====");
        } catch (Exception ex) {
            logger.error("校内执勤明细数据导出异常：{}", ex.getMessage(), ex);
        }
    }

    /**
     * 校外执勤明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport4zsxx")
    public void msgExport4zsxx(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("综述消息导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            params.put("notin45", "true");
            List<InspectionMessage> msgMappings = inspectionMessageService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("内容类型");
            headers.add("消息类型");
            headers.add("内容描述");
            headers.add("提交人");
            headers.add("添加时间");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (InspectionMessage bm : msgMappings) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                Long type = bm.getType();
                String typeDesc = "";
                if (type == 1) {
                    typeDesc = "校务巡查反馈";
                } else if (type == 2) {
                    typeDesc = "一周综述";
                } else if (type == 3) {
                    typeDesc = "校园大事记";
                } else if (type == 6) {
                    typeDesc = "后勤巡查反馈";
                }
                String title = null;
                if (type == 6) {
                    title = "后勤巡查反馈";
                } else {
                    String[] titleArray = bm.getTitle().split("-");
                    if (titleArray.length >= 2) {
                        title = titleArray[1].replace("校务巡查反馈", "").replace("校务巡查", "");
                    }
                }
                xdata_temp.add(typeDesc);
                xdata_temp.add(title);
                xdata_temp.add(bm.getRemark());
                xdata_temp.add(bm.getUserName());
                xdata_temp.add(bm.getFmtDate());
                index++;
                xdataMain.add(xdata_temp);
            }
            //订单主表信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            OutputStream out = response.getOutputStream();
            String title = "综述消息";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("综述消息导出成功=====");
        } catch (Exception ex) {
            logger.error("综述消息数据导出异常：{}", ex.getMessage(), ex);
        }
    }

    /**
     * 校外执勤明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport4Hxd")
    public void msgExport4Hxd(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("校外执勤明细导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            List<InspectionMessage> msgMappings = inspectionMessageService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("内容描述");
            headers.add("提交人");
            headers.add("提交日期");
            headers.add("地点");
            headers.add("学校");
            headers.add("图片");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (InspectionMessage bm : msgMappings) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                xdata_temp.add(bm.getRemark());
                xdata_temp.add(bm.getUserName());

                xdata_temp.add(bm.getFmtDate());
                xdata_temp.add(bm.getPlace());
                xdata_temp.add(bm.getSchoolName());
                if (StringUtils.isNotEmpty(bm.getImgs())) {
                    String[] imgArray = bm.getImgs().split(",");
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
            String title = "校外执勤明细";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("校外执勤明细导出成功=====");
        } catch (Exception ex) {
            logger.error("校外执勤明细数据导出异常：{}", ex.getMessage(), ex);
        }
    }

    /**
     * 通告明细导出
     *
     * @return
     */
    @RequestMapping(value = "/msgExport")
    public void msgExport(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("通告明细导出======start=======参数:{}", JSON.toJSONString(params));
        try {
            UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
            //大校长为9可以查看所有的模板
            if (!uai.getRoleId().equals("9")) {
                params.put("schoolId", uai.getAgentId());
            }
            List<NoticeMx> msgMappings = noticeMxService.query(params);
            //构造标题
            List<String> headers = new ArrayList<String>();
            headers.add("序号");
            headers.add("姓名");
            headers.add("值班日期");
            headers.add("开始时间");
            headers.add("结束时间");
            headers.add("地点");
            //构造行数据
            List<List<String>> xdataMain = new ArrayList<List<String>>();
            int index = 1;
            for (NoticeMx bm : msgMappings) {
                List<String> xdata_temp = new ArrayList<String>();
                xdata_temp.add(String.valueOf(index));
                xdata_temp.add(bm.getUserName());
                xdata_temp.add(bm.getDutyDate());
                xdata_temp.add(bm.getStartTime());
                xdata_temp.add(bm.getEndTime());
                xdata_temp.add(bm.getPlace());
                index++;
                xdataMain.add(xdata_temp);
            }
            //订单主表信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            OutputStream out = response.getOutputStream();
            String title = params.get("templateName")+"-通告明细";
            ExportExcelUtil<InspectionMessage> excelUtil = new ExportExcelUtil<InspectionMessage>();
            String pattern = "yyyy-MM-dd HH:mm:dd";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xls").getBytes(), "iso-8859-1"));
            excelUtil.exportoExcelSorce(title, headers, null, xdataMain, out, pattern);
            logger.info("通告明细导出成功=====");
        } catch (Exception ex) {
            logger.error("通告明细数据导出异常：{}", ex.getMessage(), ex);
        }
    }
}
