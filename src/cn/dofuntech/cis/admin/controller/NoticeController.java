package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.Notice;
import cn.dofuntech.cis.admin.repository.domain.Schedule;
import cn.dofuntech.cis.admin.service.NoticeService;
import cn.dofuntech.cis.admin.service.ScheduleService;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.UserService;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * NoticeController
 *
 */

@Controller
@RequestMapping("/notice")
public class NoticeController extends AdminController<Notice>{
	
	@Resource
	private NoticeService noticeService;
	@Resource
	private ScheduleService scheduleService;
	@Resource
	private UserService userService;
	
	 /**
     * 保存推送通知（排班通知）
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> save(Long sid) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
        	Schedule sch = scheduleService.get(sid);
        	String users = sch.getUsers();
        	if(!StringUtils.isEmpty(users)){
        		 Timestamp time = new Timestamp(System.currentTimeMillis());
        		 String[] userArr = users.split(",");
        		 String title = "值班通知";
        		 for (String userid : userArr) {
        			 UserInf ui = new UserInf();
        			 ui.setId(Integer.parseInt(userid));
        			 ui = userService.getEntity(ui);
        			 StringBuffer content = new StringBuffer();
            		 content.append(ui.getUserName())
    	        		   .append("老师，")
    	        		   .append(sch.getDutyDate())
    	        		   .append("（").append(sch.getWeek()).append("）")
    	        		   .append("您值班：")
    	        		   .append(sch.getTemplateName());
            		 
        			 Notice not = new Notice();
        			 not.setTitle(title);
        			 not.setContent(content.toString());
        			 not.setType("1");//值班提醒
        			 not.setUserId(Long.parseLong(userid));
        			 not.setIsread("0");
        			 not.setAddtime(time);
        			 not.setEdittime(time);
        			 not.setDutyDate(sch.getDutyDate());
        			 not.setSchoolId(sch.getSchoolId());
        			 noticeService.insert(not);
				}
                 result.put(RETURN_CODE, SUCCESS);
        	}else{
        		logger.debug("没有排班数据，无法推送通知提");
                result.put(RETURN_CODE, ERROR_NO_RECORD);
                result.put(MSG, "没有排班数据，无法推送通知提醒");
        	}
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }
    
    /**
     * 
     * @param params
     * @param page
     * @param rows
     * @return
     */
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
        UAI uai = ((UAI) getRequest().getSession().getAttribute("UID"));
        //大校长为9可以查看所有的模板
        if(!uai.getRoleId().equals("9"))
        {
        	 params.put("schoolId",uai.getAgentId());
        }
        List<Notice> list = noticeService.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }
    
    
}
