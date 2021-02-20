package cn.dofuntech.cis.admin.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * InspectionTemplateController
 *
 */

@Controller
@RequestMapping("/inspectionTemplate")
public class InspectionTemplateController extends AdminController<InspectionTemplate>{
	
	@Resource
	private InspectionTemplateService inspectionTemplateService;
	@Autowired
	private UserService userService;
	

	    @RequestMapping(value = "/save1", method = RequestMethod.POST)
	    public @ResponseBody Map<String, Object> save(@ModelAttribute InspectionTemplate entity) {
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
	             
	                entity.setAddTime(date);
	                
	                doProcessBeforeSave(entity);
	                map1 = new HashMap<>();
	                map1.put("name",entity.getName());
	                map1.put("schoolId",entity.getSchoolId());
	                InspectionTemplate inspectionTemplate = inspectionTemplateService.get(map1);
	                if(inspectionTemplate != null)
	                {
	                	 result.put(MSG,"该学校模板已经添加");
	                	 return result;
	                }
	                
	                service.insert(entity);
	                
	            }
	            else {
	                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
	                InspectionTemplate entityInDB = service.get(baseEntity.getId());
	                HttpServletRequest request = getRequest();
	                Map<String, Object> map = WebUtil.getParameterMap(request);
	                CopyUtils.copyProperties(entityInDB, map);
	             
	                 entityInDB.setEditTime(date);
	                
	                doProcessBeforeSave(entityInDB);
	                map1 = new HashMap<>();
	                map1.put("name",entity.getName());
	                map1.put("schoolId",entity.getSchoolId());
	                InspectionTemplate inspectionTemplate = inspectionTemplateService.get(map1);
	                if(inspectionTemplate != null)
	                {
	                	 result.put(MSG,"该学校模板已经添加");
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
	     * 模板下拉组件
	     * @return
	     */
	    @RequestMapping(value = "/selectoption")
	    @ResponseBody
	    public ReturnMsg getSelectData(HttpSession session) {
	    	ReturnMsg msg = new ReturnMsg();
            UAI uai = (UAI) session.getAttribute("UID");
            String schoolId = uai.getAgentId();
            try {
                Map templateParam = new HashMap();
                templateParam.put("schoolId", schoolId);
	    		List<InspectionTemplate> dataList = inspectionTemplateService.query(templateParam);
	    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    		HashMap<String, Object> nmap = new HashMap<String, Object>();
	    		for (InspectionTemplate template : dataList) {
	    			Map<String, Object> map_temp = new HashMap<String, Object>();
	    			map_temp.put("text", template.getName());
	    			map_temp.put("value", template.getId());
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
	    /**
	     * 根据学校id查询所有模板
	     */
	    
	    @RequestMapping(value = "/queryAll")
	    public @ResponseBody Map<String, Object> query(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
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
	        if(!uai.getRoleId().equals("9"))
	        {
	        	 params.put("schoolId",uai.getAgentId());
	        }
	        Paginator paginator = new Paginator(page, rows);
	        List<InspectionTemplate> list = service.query(params, paginator);
	        long totalCount = paginator.getTotalCount();
	        result.put("total", totalCount);
	        result.put("rows", list);
	        result.put("page", page);
	        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

	        return result;
	    }
	    
	    /**
	     * 根据学校id查询所有模板
	     */
	    
	    @RequestMapping(value = "/querySid")
	    public @ResponseBody Map<String, Object> querySid(@RequestParam Map<String, Object> params) {
	        Map<String, Object> result = new HashMap<String, Object>();
	      
	        try {
				List<InspectionTemplate> list = service.query(params);
      
				result.put("rows", list);
				result.put("msg",200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("msg",202);
			}
	    
	        return result;
	    }
	   
	  //根据权限ID查询所有后勤人员 (分类查询)
	  		@RequestMapping("/queryUser")
	  		public @ResponseBody Map<String,Object> queryUser(){
	  		 Map<String,Object> map = new HashMap<String,Object>();
	  		 UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
	  		 map.put("agentId",uai.getAgentId());
	  	     try { 
	  			List<UserInf> ic1 =  userService.queryAll(map);
	  			 
	  			 map.put("list",ic1);
	  		} catch (Exception e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  		 
	  		 
	  		  return map;
	  		}
}
