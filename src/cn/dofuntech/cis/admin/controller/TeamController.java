package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.Team;
import cn.dofuntech.cis.admin.service.TeamService;
import cn.dofuntech.core.util.ReturnMsg;

import com.alibaba.fastjson.JSON;

/**
 * 控制器
 * TeamController
 *
 */

@Controller
@RequestMapping("/team")
public class TeamController extends AdminController<Team>{
	
	@Resource
	private TeamService teamService;

	
	

	@RequestMapping(value = "/save1", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(@ModelAttribute Team entity) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }
        Map<String,Object> map1 =null;
        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
             
                entity.setAddtime(date);
                
                doProcessBeforeSave(entity);
                
                map1 = new HashMap<>();
                map1.put("teamName",entity.getTeamName());
                map1.put("schoolId",entity.getSchoolId());
                Team team = teamService.get(map1);
                if(team != null)
                {
                	 result.put(MSG,"该学校团队名称已经添加");
                	 return result;
                }
                service.insert(entity);
                
            }
            else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                Team entityInDB = service.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
             
                 entityInDB.setEdittime(date);
                
                doProcessBeforeSave(entityInDB);
                map1 = new HashMap<>();
                map1.put("teamName",entity.getTeamName());
                map1.put("schoolId",entity.getSchoolId());
                Team team = teamService.get(map1);
                if(team != null)
                {
                	 result.put(MSG,"该学校团队名称已经添加");
                	 return result;
                }
                service.update(entityInDB);
            }
            result.put(RETURN_CODE, SUCCESS);
        }
        catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }

	
	/**
     * 团队下拉组件
     * @return
     */
    @RequestMapping(value = "/selectoption")
    @ResponseBody
    public ReturnMsg getSelectData() {
    	ReturnMsg msg = new ReturnMsg();
    	try {
    		List<Team> dataList = teamService.query();
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		HashMap<String, Object> nmap = new HashMap<String, Object>();
    		for (Team team : dataList) {
    			Map<String, Object> map_temp = new HashMap<String, Object>();
    			map_temp.put("text", team.getTeamName());
    			map_temp.put("value", team.getId());
    			list.add(map_temp);
    		}
    		nmap.put("options", list);
    		msg.setObj(JSON.toJSONString(nmap));
    		msg.setMsg("200", "查询成功！");
    	}
    	catch (Exception e) {
    		msg.setMsg("202", e.getMessage());
    	}
    	
    	return msg;
    }
    
    @RequestMapping(value = "/query1")
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
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的模板
        if(!uai.getRoleId().equals("9"))
        {
        	 params.put("schoolId",uai.getAgentId());
        }
        List<Team> list = teamService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }

}
