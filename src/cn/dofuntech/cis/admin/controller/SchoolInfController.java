package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.SchoolInf;
import cn.dofuntech.cis.admin.service.SchoolInfService;

/**
 * 控制器
 * SchoolInfController
 *
 */

@Controller
@RequestMapping("/schoolInf")
public class SchoolInfController extends AdminController<SchoolInf>{
	
	@Resource
	private SchoolInfService schoolInfService;
	
	@RequestMapping( value ="/schDetail")
	public String schoolDetail(HttpServletRequest request,Long id,Model map){
		 SchoolInf school = schoolInfService.get(id);
		 map.addAttribute("school", school);
		return "schoolinf/detail";
	}
	
	@RequestMapping(value = "/save1", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(@ModelAttribute SchoolInf entity) {
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
                
                doProcessBeforeSave(entity);
                service.insert(entity);
                
            }
            else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                SchoolInf entityInDB = service.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
             
                 entityInDB.setEditTime(date);
                
                doProcessBeforeSave(entityInDB);
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
     * 学校下拉组件
     * @return
     */
    @RequestMapping(value = "/selectoption")
    @ResponseBody
    public ReturnMsg getSelectData() {
    	ReturnMsg msg = new ReturnMsg();
    	try {
    		UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		HashMap<String, Object> nmap = new HashMap<String, Object>();
    		//总校长查询所有学校信息
    		if(uai.getRoleId().equals("9"))
    		{
    			List<SchoolInf> query = schoolInfService.query();
    			for (SchoolInf template :query) {
        			Map<String, Object> map_temp = new HashMap<String, Object>();
        			map_temp.put("text", template.getSchoolName());
        			map_temp.put("value", template.getId());
        			list.add(map_temp);
        		}
    		}
    		//只查自己所在学校
    		else
    		{
    			SchoolInf schoolInf = schoolInfService.get(Long.parseLong(uai.getAgentId()));
    			Map<String, Object> map_temp = new HashMap<String, Object>();
    			map_temp.put("text", schoolInf.getSchoolName());
    			map_temp.put("value", schoolInf.getId());
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
	
}
