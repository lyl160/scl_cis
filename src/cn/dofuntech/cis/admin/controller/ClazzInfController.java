package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * ClazzInfController
 */

@Controller
@RequestMapping("/clazzInf")
public class ClazzInfController extends AdminController<ClazzInf> {

    @Resource
    private ClazzInfService clazzInfService;


    @RequestMapping(value = "/claDetail")
    public String schoolDetail(HttpServletRequest request, Long id, Model map) {
        ClazzInf clazz = clazzInfService.get(id);
        map.addAttribute("clazz", clazz);
        return "clazzinf/detail";
    }


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
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        Paginator paginator = new Paginator(page, rows);
        params.put("status", "0");//只查询前端显示的数据
        List<ClazzInf> list = clazzInfService.query(params, paginator);
        Map<String, Object> map = null;
        for (ClazzInf cll : list) {
            logger.info("cell数据:{}", JSON.toJSONString(cll));
            map = new HashMap<String, Object>();

            String h_id = cll.gethid();
            if (h_id != null) {

                String s[] = h_id.split(",");
                h_id = s[s.length - 1];
                map.put("id", h_id);
                ClazzInf clazzInf = clazzInfService.get(map);
                cll.setH_clazz(clazzInf.getClazz());
                cll.setH_grade(clazzInf.getGrade());
            }

        }


        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }


    @RequestMapping(value = "/save1", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> save(@ModelAttribute ClazzInf entity) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");

                entity.setAddTime(date);
                entity.setStatus("0");
                entity.setUpgrade("0");

                doProcessBeforeSave(entity);
                Map<String, Object> map = new HashMap<>();
                map.put("clazz", entity.getClazz());
                map.put("grade", entity.getGrade());
                map.put("status", entity.getStatus());
                ClazzInf clazzInf = clazzInfService.get(map);
                if (clazzInf == null) {
                    service.insert(entity);
                } else {
                    result.put(MSG, "该班级所在年级已添加");
                    return result;

                }


            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                ClazzInf entityInDB = service.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);

                entityInDB.setEditTime(date);

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

    /**
     * 升级
     */
    @RequestMapping("/classupgrade")
    public @ResponseBody
    Map classupgrade(Long id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Timestamp date1 = new Timestamp(System.currentTimeMillis());

        try {
            ClazzInf ci = clazzInfService.get(id);

            //升级先将然来数据状态改变

            ci.setStatus("1");//前端不显示
            ci.setUpgrade("1");//已升级;
            if (ci.getGrade().equals("6年级")) {
                ci.setGrade("毕业年级");
                result.put(RETURN_CODE, SUCCESS);
                clazzInfService.update(ci);
                return result;
            }


            clazzInfService.update(ci);
            ClazzInf ci1 = ci;


            ci1.setStatus("0");//前端显示
            ci1.setUpgrade("0");//未升级;

            ci1.setId(null);

            //年级增加
            String grade = (Integer.parseInt(ci.getGrade().substring(0, 1)) + 1) + "年级";
            if (grade.equals("7年级")) {
                ci1.setGrade("毕业年级");
            } else {
                ci1.setGrade(grade);
            }
            //判断历史id是否为空
            StringBuilder h_id = null;
            if (ci.gethid() == null || "".equals(ci.gethid())) {
                h_id = new StringBuilder();
                h_id.append(id);
            } else {
                h_id = new StringBuilder(ci.gethid());
                h_id.append("," + id);

            }

            ci1.sethid(h_id.toString());
            ci1.setAddTime(date1);
            //班级无须增加
            //团队升级
            ci1.setTeamId(ci.getTeamId() + 1);

            clazzInfService.insert(ci1);

            result.put(RETURN_CODE, SUCCESS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, e.getMessage());
        }


        return result;

    }

}
