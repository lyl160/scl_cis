package cn.dofuntech.cis.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.cis.admin.repository.domain.DynamicAttr;
import cn.dofuntech.cis.admin.repository.domain.vo.DynamicAttrVo;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.DynamicAttrService;
import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;

/**
 * 控制器
 * DynamicAttrController
 */

@Controller
@RequestMapping("/dynamicAttr")
public class DynamicAttrController extends AdminController<DynamicAttr> {

    @Resource
    private DynamicAttrService dynamicAttrService;

    @Resource
    private ClazzInfService clazzInfService;


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> query(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
        Map<String, Object> result = new HashMap<String, Object>();
        //  params.put("s_orgid",getUserInSession().getOrgId());
        if (page == null) {
            page = Paginator.DEFAULT_CURRENT_PAGE;
        }
        if (rows == null) {
            rows = Paginator.DEFAULT_PAGE_SIZE;
        }
        Paginator paginator = new Paginator(page, rows);
        List<DynamicAttr> list = service.query(params, paginator);
        List<DynamicAttrVo> volist = new ArrayList<>(list.size());
        for (DynamicAttr dynamicAttr : list) {
            DynamicAttrVo vo = new DynamicAttrVo();
            CopyUtils.copyProperties(vo, dynamicAttr);
            vo.setUidsShow(dynamicAttr.getUids());
            volist.add(vo);
        }
        //转化一下，增加uidsShow字段
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", volist);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }


    @RequestMapping(value = "/manageAttr")
    public String manage(HttpServletRequest request, ModelMap map) {
        map.put("templateId", request.getParameter("templateId"));
        map.put("schoolId", request.getParameter("schoolId"));
        map.put("tname", request.getParameter("tname"));
        if (request.getParameter("tname").equals("校务巡查") || request.getParameter("tname").equals("教师执勤")) {
            return "inspectiontemplate/template_add";
        } else if (request.getParameter("tname").equals("后勤巡查")) {
            return "inspectiontemplate/template_add2";
        } else {
            return "inspectiontemplate/template_add1";
        }

    }

    @RequestMapping(value = "/getAttrId")
    public @ResponseBody
    Long getAttrId(Long id) {
        DynamicAttr entity = new DynamicAttr();
        entity.setTemplateId(id);
        dynamicAttrService.save(entity);
        return entity.getId();
    }

    @RequestMapping("/saveAttr")
    public @ResponseBody
    Map saveAttr(@RequestBody List<DynamicAttr> entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (DynamicAttr di : entity) {

            if (di.getId() != null) {
                dynamicAttrService.update(di);
                map.put("data", "1");
            }
        }
        return map;
    }



    @RequestMapping(value = "/saveOrUpdate")
    public @ResponseBody
    Map<String, Object> save(DynamicAttr dynamicAttr,@RequestParam(value="uidsShow[]",required = false) String uidsShow) {

        Map<String, Object> result = new HashMap<String, Object>();
        if (!(dynamicAttr instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {
            AutoIdEntity baseEntity = (AutoIdEntity) dynamicAttr;
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
                doProcessBeforeSave(dynamicAttr);
                if (StringUtils.isNotEmpty(uidsShow)) {
                    dynamicAttr.setUids(uidsShow);
                }
                service.insert(dynamicAttr);

            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                DynamicAttr entityInDB = service.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
                if (StringUtils.isNotEmpty(uidsShow)) {
                    entityInDB.setUids(uidsShow);
                }
                doProcessBeforeSave(entityInDB);
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



}
