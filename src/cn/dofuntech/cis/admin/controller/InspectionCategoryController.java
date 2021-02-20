package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.cis.admin.service.InspectionCategoryService;
import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * InspectionCategoryController
 */

@Controller
@RequestMapping("/inspectionCategory")
public class InspectionCategoryController extends AdminController<InspectionCategory> {

    @Resource
    private InspectionCategoryService inspectionCategoryService;

    @RequestMapping(value = "/queryList")
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
        //大校长为9可以查看所有的
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }

        List<InspectionCategory> list = inspectionCategoryService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }

    @RequestMapping(value = "/save1", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> save(@ModelAttribute InspectionCategory entity,
                             @RequestParam(value = "diyWeek[]", required = false) String diyWeek,
                             @RequestParam(value = "diyStartTime[]", required = false) String diyStartTime,
                             @RequestParam(value = "diyEndTime[]", required = false) String diyEndTime) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
                entity.setIlevel("1");
                entity.setTemplateId(entity.getTemplateId());
                entity.setAddTime(date);
                doProcessBeforeSave(entity);
                inspectionCategoryService.insert(entity);

            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                InspectionCategory entityInDB = service.get(baseEntity.getId());
                entityInDB.setEditTime(date);
                entityInDB.setValue(entity.getValue());
                entityInDB.setStartTime(entity.getStartTime());
                entityInDB.setEndTime(entity.getEndTime());
                entityInDB.setUsers(entity.getUsers());
                entityInDB.setUsersName(entity.getUsersName());
                StringBuilder diyTimeStr = new StringBuilder();
                if (StringUtils.isNotEmpty(diyWeek) && StringUtils.isNotEmpty(diyStartTime) && StringUtils.isNotEmpty(diyEndTime)) {
                    String[] week = diyWeek.split(",");
                    String[] startTime = diyStartTime.split(",");
                    String[] endTime = diyEndTime.split(",");
                    for (int i = 0; i < week.length; i++) {
                        diyTimeStr.append(week[i]);
                        diyTimeStr.append(",");
                        diyTimeStr.append(startTime[i]);
                        diyTimeStr.append(",");
                        diyTimeStr.append(endTime[i]);
                        if (i < (week.length - 1)) {
                            diyTimeStr.append("&");
                        }
                    }
                }
                entityInDB.setDiyTime(diyTimeStr.toString());
                service.update(entityInDB);
            }
            result.put(RETURN_CODE, SUCCESS);
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }


    @RequestMapping("addlv2/init")
    @ResponseBody
    public Map<String, Object> addDcitInit(Long id) {

        List list = new ArrayList();
        InspectionCategory di = inspectionCategoryService.get(id);
        for (int i = 0; i < 10; i++) {
            InspectionCategory jmi = new InspectionCategory();
            jmi.setValue(di.getValue());
            jmi.setId(Long.valueOf(i + 1));
            list.add(jmi);
        }
        Map result = new HashMap();
        result.put("rows", list);
        return result;
    }


    /**
     * 保存二级分类
     *
     * @return
     */
    @RequestMapping(value = "/addlv2/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addbutton(@RequestBody List<InspectionCategory> jmiData) {
        Map<String, Object> result = new HashMap<String, Object>();
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        try {
            logger.info("{[]}", jmiData);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String startTime;
            String endTime;
            InspectionCategory inspectionCategory = inspectionCategoryService.get(jmiData.get(0).getPid1());
            System.out.println("id是----------" + jmiData.get(0).getPid1());
            for (InspectionCategory inspectioncategory : jmiData) {
                startTime = inspectioncategory.getStartTime();
                endTime = inspectioncategory.getEndTime();
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("value", inspectioncategory.getValue());
                param.put("ilevel", "2");
                param.put("templateId", inspectionCategory.getTemplateId());
                param.put("id", inspectionCategory.getId());
                InspectionCategory jki = inspectionCategoryService.get(param);
                if (jki != null) {
                    continue;
                }
                inspectioncategory.setSchoolId(Long.parseLong(uai.getAgentId()));
                inspectioncategory.setIlevel("2");
                inspectioncategory.setAddTime(time);
                inspectioncategory.setEditTime(time);
                inspectioncategory.setTemplateId(inspectionCategory.getTemplateId());
                inspectioncategory.setStartTime(startTime);
                inspectioncategory.setEndTime(endTime);
                inspectionCategoryService.save(inspectioncategory);
            }
            result.put(RETURN_CODE, SUCCESS);
        } catch (Exception e) {
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, e.getMessage());
        }
        return result;
    }


    //@RequestMapping(value = "/editlv2/view")
    //public String editlv2View(@ModelAttribute InspectionCategory categoryParam, ModelMap map) {
    //    AutoIdEntity baseEntity = (AutoIdEntity) categoryParam;
    //    InspectionCategory category = service.get(baseEntity.getId());
    //    if (StringUtils.isNotEmpty(category.getUsers())) {//后勤巡查支持多人
    //        category.setUserIdArr(category.getUsers().split(","));
    //    }
    //    map.put("entity", category);
    //    return "inspectioncategory/edit";
    //}


    @RequestMapping(value = "/edit2lv2/view")
    public String editlv2View2(@ModelAttribute InspectionCategory entity, ModelMap map) {
        AutoIdEntity baseEntity = (AutoIdEntity) entity;
        InspectionCategory entityInDB = service.get(baseEntity.getId());
        map.put("entity", entityInDB);
        return "inspectioncategory/edit2";
    }


    //校务巡查,教室巡查类别查询
    @RequestMapping("/category")

    public @ResponseBody
    Map<String, Object> Category(InspectionCategory isy) {
        //查询一级分类加模板id

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ilevel", "1");
        m.put("templateId", isy.getTemplateId());
        List<InspectionCategory> ic = inspectionCategoryService.query(m);

        //根据一级pid1查询二级分类 
        Map<String, Object> m1 = new HashMap<String, Object>();

        for (InspectionCategory isc : ic) {
            m1.put("pid1", isc.getId());
            List<InspectionCategory> ic1 = inspectionCategoryService.query(m1);
            isc.setList(ic1);

        }
        m.put("list", ic);
        return m;
    }


    //根据模板id和二级分类查询所有的信息 (分类查询)
    @RequestMapping("/classify")
    public @ResponseBody
    Map<String, Object> classify(InspectionCategory isy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("templateId", isy.getTemplateId());
        map.put("ilevel", "2");

        try {
            List<InspectionCategory> ic1 = inspectionCategoryService.query(map);

            map.put("list", ic1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return map;
    }

    /**
     * 模板下拉组件
     *
     * @return
     */
    @RequestMapping(value = "/selectoption")
    @ResponseBody
    public ReturnMsg getSelectData() {
        ReturnMsg msg = new ReturnMsg();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ilevel", "2");
            map.put("templateId", "3");
            List<InspectionCategory> dataList = inspectionCategoryService.query(map);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            HashMap<String, Object> nmap = new HashMap<String, Object>();
            for (InspectionCategory category : dataList) {
                Map<String, Object> map_temp = new HashMap<String, Object>();
                map_temp.put("text", category.getValue());
                map_temp.put("value", category.getId());
                list.add(map_temp);
            }
            nmap.put("options", list);
            msg.setObj(JSON.toJSONString(nmap));
            msg.setMsg("200", "查询成功！");
        } catch (Exception e) {
            msg.setMsg("202", e.getMessage());
        }

        return msg;
    }

    //获取教师巡检对应二级类别的巡检时间
    @RequestMapping(value = "/getData")
    @ResponseBody
    public ReturnMsg getData(@RequestParam(value = "cid", required = false) Integer cid) {
        ReturnMsg msg = new ReturnMsg();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cid);
            InspectionCategory dataList = inspectionCategoryService.get(map);
            String time = dataList.getStartTime() + " 至 " + dataList.getEndTime();
            msg.setObj(time);
            msg.setMsg("200", "查询成功！");
        } catch (Exception e) {
            msg.setMsg("202", e.getMessage());
        }

        return msg;
    }
}
