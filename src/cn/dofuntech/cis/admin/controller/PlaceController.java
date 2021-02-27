package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.service.BaseService;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;
import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.Place;
import cn.dofuntech.cis.admin.service.PlaceService;

/**
 * 控制器
 * PlaceController
 *
 */

@Controller
@RequestMapping("/place")
public class PlaceController extends AdminController<Place> {

    @Resource
    private PlaceService placeService;

    /**
     * 根据学校id查询所有地点
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
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的模板
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        params.put("userId", "1");
        Paginator paginator = new Paginator(page, rows);
        List<Place> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }

    @RequestMapping(value = "/save1", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> save(@ModelAttribute Place entity) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            Map<String, Object> map = new HashMap<String, Object>();
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {

                logger.debug("entity.id 为空，新增");
                entity.setAddTime(date);
                doProcessBeforeSave(entity);
                if (entity.getDefaultFlag().equals("0000")) {
                    map.put("userId", "1");
                    List<Place> list = placeService.query(map);
                    for (Place li : list) {
                        li.setUserId(1L);
                        li.setDefaultFlag("1");
                        li.setEditTime(date);
                        placeService.update(li);
                    }
                }
                placeService.insert(entity);
            } else {

                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                Place entityInDB = service.get(baseEntity.getId());
                entityInDB.setEditTime(date);
                entityInDB.setDefaultFlag(entity.getDefaultFlag());
                entityInDB.setPlaceName(entity.getPlaceName());
                if (entity.getDefaultFlag().equals("0000")) {
                    map.put("userId", "1");
                    List<Place> list = placeService.query(map);
                    for (Place li : list) {
                        li.setUserId(1L);
                        li.setDefaultFlag("1");
                        li.setEditTime(date);
                        placeService.update(li);
                    }
                }
                placeService.update(entityInDB);
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
