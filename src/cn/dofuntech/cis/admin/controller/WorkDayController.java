package cn.dofuntech.cis.admin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.cis.admin.repository.domain.WorkDay;
import cn.dofuntech.cis.admin.service.WorkDayService;
import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.web.WebUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 控制器
 * WorkDayController
 */

@Controller
@RequestMapping("/workDay")
public class WorkDayController extends AdminController<WorkDay> {

    @Resource
    private WorkDayService workDayService;

    /**
     * 保存或更新排班
     */
    @RequestMapping(value = "/saveorupdate", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> saveorupdate(@ModelAttribute WorkDay entity) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }
        try {
            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
                Map workParam = new HashMap();
                workParam.put("year", entity.getYear());
                workParam.put("month", entity.getMonth());
                WorkDay oldWorkday = workDayService.get(workParam);
                if (oldWorkday != null) {
                    throw new RuntimeException("已存在该年月的记录");
                }
                Timestamp time = new Timestamp(System.currentTimeMillis());
                entity.setAddtime(time);
                entity.setEdittime(time);
                workDayService.insert(entity);
            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                WorkDay entityInDB = workDayService.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
                entity.setEdittime(new Timestamp(System.currentTimeMillis()));
                workDayService.update(entityInDB);
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
     * 排班列表查询扩展方法
     */
    @RequestMapping(value = "/query_extra")
    public @ResponseBody
    Map<String, Object> queryExtra(@RequestParam Map<String, Object> params, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) {
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
        //大校长为9可以查看所有的排班列表
        if (!uai.getRoleId().equals("9")) {
            params.put("schoolId", uai.getAgentId());
        }
        List<WorkDay> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        result.put("total", totalCount);
        result.put("rows", list);
        result.put("page", page);
        result.put("totalPage", totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1);

        return result;
    }

    /**
     * 老师列表页面
     */
    @RequestMapping(value = "optuser/view")
    public String optserviceView(ModelMap map, String schoolId) {
        map.addAttribute("schoolId", schoolId);
        return "workday/optusermanage";
    }

    public static void main(String[] args) {
        Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
        int nowWeekDay = cd.get(Calendar.DAY_OF_WEEK);
        System.out.println(nowWeekDay - 1);
    }

    /**
     * 排班日期
     * 从当前时间开始算起
     */
    @RequestMapping(value = "get/date/json")
    @ResponseBody
    public Map<String, Object> getJsonDate(@RequestParam Map<String, Object> map) {
        String year = (String) map.get("year");
        String month = (String) map.get("month");
        Map<String, Object> result = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        Date startDate = getFirstDayOfMonth(calendar, Integer.parseInt(year), Integer.parseInt(month));
        Date endDate = getLastDayOfMonth(calendar, Integer.parseInt(year), Integer.parseInt(month));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE", Locale.CHINA);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); //保存日期集合
        try {
            Date date = startDate;
            Calendar cd = Calendar.getInstance();//用Calendar 进行日期比较判断
            cd.setTime(startDate);
            int nowWeekDay = cd.get(Calendar.DAY_OF_WEEK) - 1;
            while (date.getTime() <= endDate.getTime()) {
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("date_str", sdf.format(date));
                temp.put("week_str", dateFm.format(date));
                list.add(temp);
                cd.setTime(date);
                cd.add(Calendar.DATE, 1);//增加一天 放入集合
                date = cd.getTime();
            }
            result.put(DATA, list);
            result.put("nowWeekDay", nowWeekDay);
            result.put(RETURN_CODE, SUCCESS);
            result.put(MSG, "获取日期成功");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }

    /**
     * 获取当前月第一天
     *
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Calendar calendar, int year, int month) {

        calendar.set(Calendar.YEAR, year);
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Calendar calendar, int year, int month) {
        calendar.set(Calendar.YEAR, year);
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (month == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        return calendar.getTime();
    }
}
