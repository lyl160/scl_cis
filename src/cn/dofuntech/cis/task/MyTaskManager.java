package cn.dofuntech.cis.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.admin.repository.domain.DynamicAttr;
import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.cis.admin.repository.domain.InspectionLogs;
import cn.dofuntech.cis.admin.repository.domain.InspectionMessage;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.repository.domain.InspectionTemplate;
import cn.dofuntech.cis.admin.repository.domain.Notice;
import cn.dofuntech.cis.admin.repository.domain.NoticeMx;
import cn.dofuntech.cis.admin.repository.domain.Place;
import cn.dofuntech.cis.admin.repository.domain.Schedule;
import cn.dofuntech.cis.admin.repository.domain.WorkDay;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.DynamicAttrService;
import cn.dofuntech.cis.admin.service.InspectionCategoryService;
import cn.dofuntech.cis.admin.service.InspectionLogsService;
import cn.dofuntech.cis.admin.service.InspectionMessageService;
import cn.dofuntech.cis.admin.service.InspectionResultService;
import cn.dofuntech.cis.admin.service.InspectionTemplateService;
import cn.dofuntech.cis.admin.service.NoticeMxService;
import cn.dofuntech.cis.admin.service.NoticeService;
import cn.dofuntech.cis.admin.service.PlaceService;
import cn.dofuntech.cis.admin.service.ScheduleService;
import cn.dofuntech.cis.admin.service.WorkDayService;
import cn.dofuntech.dfauth.bean.Dict;
import cn.dofuntech.dfauth.bean.UserInf;
import cn.dofuntech.dfauth.service.DictService;
import cn.dofuntech.dfauth.service.UserService;


@Component
public class MyTaskManager {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private InspectionCategoryService inspectionCategoryService;
    @Autowired
    private ClazzInfService clazzInfService;
    @Autowired
    private InspectionLogsService inspectionLogsService;
    @Autowired
    private DynamicAttrService dynamicAttrService;
    @Autowired
    private InspectionResultService inspectionResultService;
    @Autowired
    private InspectionMessageService inspectionMessageService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private NoticeMxService noticeMxService;
    @Autowired
    private DictService dictService;
    @Autowired
    private WorkDayService workDayService;
    @Autowired
    private InspectionTemplateService inspectionTemplateService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");

    private static Long DICT_ITEMCODE_TEACHER = 10000008L;
    private static Long DICT_ITEMCODE_STUDENT = 10000009L;

    /**
     * 判断某校今天是不是休息日
     * @param schoolId
     * @return
     */
    private boolean todayIsHoliday(Long schoolId) {
        Calendar cal = Calendar.getInstance();
        String nowDataStr = sdf.format(System.currentTimeMillis());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        Map workParam = new HashMap();
        workParam.put("year", year);
        workParam.put("month", month);
        workParam.put("schoolId", schoolId);
        WorkDay workDay = workDayService.get(workParam);
        if (workDay != null && StringUtils.isNotEmpty(workDay.getHoliday())) {
            String holiday = workDay.getHoliday();
            if (holiday.indexOf(nowDataStr) >= 0) {
                logger.debug("今天是休息日，不需要推送消息");
                return true;
            }
        }
        return false;
    }


    /**
     * 每天早上6点钟执行 自动推送值班消息
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void morningNotice() throws Exception {
        logger.info("每天六点，自动推送值班消息...");
        //根据校历  如果当天不开学，则不需要 通知


        //查询排班 得到今天所有要推送的消息
        Map<String, Object> scheduleParam = new HashMap<String, Object>();
        scheduleParam.put("dutyDate", sdf.format(System.currentTimeMillis()));
        List<Schedule> query = scheduleService.query(scheduleParam);

        if (query != null && query.size() > 0) {
            for (Schedule schedule : query) {
                String userString = schedule.getUsers();
                if (todayIsHoliday(schedule.getSchoolId())) {
                    continue;
                }
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String[] userArr = userString.split(",");
                String title = "值班通知";
                for (String userId : userArr) {
                    UserInf userInfo = new UserInf();
                    userInfo.setId(Integer.parseInt(userId));
                    userInfo = userService.getEntity(userInfo);
                    StringBuffer noticeContent = new StringBuffer();
                    noticeContent.append(userInfo.getUserName())
                            .append("老师，")
                            .append(schedule.getDutyDate())
                            .append("（").append(schedule.getWeek()).append("）")
                            .append("您值班：")
                            .append(schedule.getTemplateName());

                    Notice notice = new Notice();
                    notice.setTitle(title);
                    notice.setContent(noticeContent.toString());
                    notice.setType("1");//值班提醒
                    notice.setUserId(Long.parseLong(userId));
                    notice.setIsread("0");
                    notice.setAddtime(time);
                    notice.setEdittime(time);
                    notice.setDutyDate(schedule.getDutyDate());
                    notice.setSchoolId(schedule.getSchoolId());
                    noticeService.insert(notice);
                }

            }
        } else {
            logger.debug("没有排班数据，无法推送通知提");
        }
    }

    /**
     * 每天6点到18点每5分钟执行一次值班通知(教师执勤)
     * 目的 提前5分钟通知教师去执勤
     */
    @Scheduled(cron = "0 0/5 6-18 * * ? ")
    public void jiaoshiNotice() throws Exception {
        logger.info("教师执勤，值班通知");
        logger.info("每天6点到18点每隔5分钟，自动推送值班消息...");


        Timestamp nowDate = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        Date after5minuteDate = new Date(nowDate.getTime() + 300000);//300秒（5分钟）之后的日期
        Map<String, Object> scheduleParam = new HashMap<String, Object>();
        Map<String, Object> categoryParam = new HashMap<String, Object>();
        scheduleParam.put("dutyDate", sdf.format(System.currentTimeMillis()));
        scheduleParam.put("templateName", "教师执勤");
        //得到今天所有要推送的消息
        List<Schedule> scheduleList = scheduleService.query(scheduleParam);
        if (scheduleList != null && scheduleList.size() > 0) {
            for (Schedule schedule : scheduleList) {
                //根据校历  如果当天不开学，则不需要 通知
                if (todayIsHoliday(schedule.getSchoolId())) {
                    continue;
                }
                //要值班的用户
                List<UserInf> userList = scheduleService.getTeachersBySchedule(schedule);

                categoryParam.put("templateName", "教师执勤");
                categoryParam.put("schoolId", schedule.getSchoolId());
                //获取全部 教师执勤 巡检项目
                List<InspectionCategory> categoryAllList = inspectionCategoryService.query(categoryParam);
                for (InspectionCategory category : categoryAllList) {
                    String content = "";
                    //找出5分钟之后符合时间范围的项目
                    if (inspectionCategoryService.inTime(category, after5minuteDate)) {
                        //将自定义时间重新设置项目开始和结束时间，如果无自定义时间则无操作
                        inspectionCategoryService.resetStartAndEndTime(category, new Date());

                        //针对该项目检查每个用户是否已提交执勤信息
                        for (UserInf userInfo : userList) {
                            //查询今天该用户提交的执勤消息
                            Map<String, Object> messageParam = new HashMap<String, Object>();
                            messageParam.put("title", "教师执勤");
                            messageParam.put("startHours", sdf.format(nowDate) + " " + category.getStartTime() + ":00");//今天+开始时间
                            messageParam.put("endHours", sdf.format(nowDate) + " " + category.getEndTime() + ":00");//今天+结束时间
                            messageParam.put("userId", userInfo.getId());
                            messageParam.put("schoolId", schedule.getSchoolId());
                            List<InspectionMessage> messageList = inspectionMessageService.query(messageParam);
                            if (messageList != null && messageList.size() > 0) {
                                //查询存在执勤消息，如果已经执勤了，就不再推通知消息了
                                logger.debug("用户：{},已经值班，无需发送消息", userInfo.getUserName());
                                continue;
                            } else {
                                //如果还没有执勤消息，则发值班通知
                                content = "(" + category.getStartTime() + "至" + category.getEndTime() + category.getValue() + ")";
                                StringBuffer noticeContent = new StringBuffer();
                                noticeContent.append(userInfo.getUserName())
                                        .append("老师，")
                                        .append("5分钟后您有值班任务：")
                                        .append(schedule.getTemplateName())
                                        .append(content)
                                        .append("，请及时到岗执勤。");
                                Notice notice = new Notice();
                                notice.setTitle("值班通知");
                                notice.setContent(noticeContent.toString());
                                notice.setType("1");//值班提醒
                                notice.setUserId(userInfo.getId() + 0L);
                                notice.setIsread("0");//未读
                                notice.setAddtime(new Timestamp(System.currentTimeMillis()));
                                notice.setEdittime(new Timestamp(System.currentTimeMillis()));
                                notice.setDutyDate(schedule.getDutyDate());
                                notice.setSchoolId(schedule.getSchoolId());
                                noticeService.insert(notice);
                                logger.debug("给用户：{}，发送值班通知消息", userInfo.getUserName());
                            }
                        }

                    }
                }
            }
        } else {
            logger.debug("没有排班数据，无法推送通知提");
        }
    }

    /**
     * 每天6点到18点每5分钟执行一次值班通知(护校队巡查)
     * 目的 提前5分钟通知护校队去巡查
     */
    @Scheduled(cron = "10 0/5 6-18 * * ? ")
    public void huxiaoduiNotice() throws Exception {
        logger.info("护校队巡查，值班通知...");
        logger.info("每天6点到18点，每隔5分钟，自动推送值班消息...");

        Timestamp nowDate = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        Date after5minuteDate = new Date(nowDate.getTime() + 300000);//300秒（5分钟）之后的日期
        Map<String, Object> scheduleParam = new HashMap<String, Object>();
        Map<String, Object> categoryParam = new HashMap<String, Object>();
        scheduleParam.put("dutyDate", sdf.format(System.currentTimeMillis()));
        scheduleParam.put("templateName", "护校队巡查");
        //得到今天所有要推送的消息
        List<Schedule> scheduleList = scheduleService.query(scheduleParam);
        if (scheduleList != null && scheduleList.size() > 0) {
            for (Schedule schedule : scheduleList) {
                //根据校历  如果当天不开学，则不需要 通知
                if (todayIsHoliday(schedule.getSchoolId())) {
                    continue;
                }
                //要值班的用户
                List<UserInf> userList = scheduleService.getTeachersBySchedule(schedule);

                categoryParam.put("templateName", "护校队巡查");
                categoryParam.put("schoolId", schedule.getSchoolId());
                //获取全部 教师执勤 巡检项目
                List<InspectionCategory> categoryAllList = inspectionCategoryService.query(categoryParam);
                for (InspectionCategory category : categoryAllList) {
                    //找出5分钟之后符合时间范围的项目
                    if (inspectionCategoryService.inTime(category, after5minuteDate)) {
                        //将自定义时间重新设置项目开始和结束时间，如果无自定义时间则无操作
                        inspectionCategoryService.resetStartAndEndTime(category, new Date());

                        //针对该项目检查每个用户是否已提交执勤信息
                        for (UserInf userInfo : userList) {
                            //查询今天该用户提交的执勤消息
                            Map<String, Object> messageParam = new HashMap<String, Object>();
                            messageParam.put("title", "护校队巡查");
                            messageParam.put("startHours", sdf.format(nowDate) + " " + category.getStartTime() + ":00");//今天+开始时间
                            messageParam.put("endHours", sdf.format(nowDate) + " " + category.getEndTime() + ":00");//今天+结束时间
                            messageParam.put("userId", userInfo.getId());
                            messageParam.put("schoolId", schedule.getSchoolId());
                            List<InspectionMessage> messageList = inspectionMessageService.query(messageParam);
                            if (messageList != null && messageList.size() > 0) {
                                //查询存在执勤消息，如果已经执勤了，就不再推通知消息了
                                logger.debug("用户：{},已经值班，无需发送消息", userInfo.getUserName());
                                continue;
                            } else {
                                //如果还没有执勤消息，则发值班通知
                                String content = "(" + category.getStartTime() + "至" + category.getEndTime() + category.getValue() + ")";
                                StringBuffer noticeContent = new StringBuffer();
                                noticeContent.append(userInfo.getUserName())
                                        .append("老师，")
                                        .append("5分钟后您有值班任务：")
                                        .append(schedule.getTemplateName())
                                        .append(content)
                                        .append("，请及时到岗执勤。");
                                Notice notice = new Notice();
                                notice.setTitle("值班通知");
                                notice.setContent(noticeContent.toString());
                                notice.setType("1");//值班提醒
                                notice.setUserId(userInfo.getId() + 0L);
                                notice.setIsread("0");//未读
                                notice.setAddtime(new Timestamp(System.currentTimeMillis()));
                                notice.setEdittime(new Timestamp(System.currentTimeMillis()));
                                notice.setDutyDate(schedule.getDutyDate());
                                notice.setSchoolId(schedule.getSchoolId());
                                noticeService.insert(notice);
                                logger.debug("给用户：{}，发送值班通知消息", userInfo.getUserName());
                            }
                        }

                    }
                }
            }
        } else {
            logger.debug("没有排班数据，无法推送通知提");
        }
    }

    /**
     * 每天晚上18点00秒，自动生成当天后勤巡查未巡查各班级分数
     */
    @Scheduled(cron = "0 0 18 * * ? ")
    public void houQinAutoScore() {
        logger.info("后勤巡查默认打分开始执行...");
        logger.info("每天晚上18点0分，自动生成当天后勤未巡查各班级分数...");

        Timestamp nowTime = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        String nowDate = sdf.format(nowTime.getTime());


        //循环学校
        for (long schoolId = 1; schoolId <= 3; schoolId++) {
            if (todayIsHoliday(schoolId)) {
                continue;
            }
            //查询该校模板
            Map templateParam = new HashMap();
            templateParam.put("name", "后勤巡查");
            templateParam.put("schoolId", schoolId);
            InspectionTemplate template = inspectionTemplateService.get(templateParam);
            if (template == null) {
                logger.error("schoolId:" + schoolId + ",找不到后勤巡查模板信息...");
                continue;
            }

            //查询该校班级
            Map<String, Object> clazzParam = new HashMap<String, Object>();
            clazzParam.put("status", "0");
            clazzParam.put("schoolId", schoolId);
            List<ClazzInf> clazzList = clazzInfService.query(clazzParam);

            //查询该校2级分类 后勤巡查不存在2级分类 故不使用
            Map<String, Object> categoryParam = new HashMap<String, Object>();
            categoryParam.put("templateId", template.getId());
            categoryParam.put("schoolId", schoolId);
            categoryParam.put("ilevel", "2");
            List<InspectionCategory> categoryList = inspectionCategoryService.query(categoryParam);

            //查询该校 后勤巡查 的所有属性
            Map<String, Object> attrParam = new HashMap<String, Object>();
            attrParam.put("templateId", template.getId());
            attrParam.put("schoolId", schoolId);
            List<DynamicAttr> attrList = dynamicAttrService.query(attrParam);


            //查询该校今日的所有后勤巡查记录
            Map<String, Object> logsParam = new HashMap<String, Object>();
            logsParam.put("addTime", nowDate);
            logsParam.put("templateId", template.getId());
            logsParam.put("schoolId", schoolId);
            List<InspectionLogs> logsList = inspectionLogsService.query(logsParam);

            Map<Long, List<InspectionResult>> clazzResultMap = new HashMap<>();
            if (logsList != null && logsList.size() > 0) {
                for (InspectionLogs logs : logsList) {
                    List<InspectionResult> resultList = clazzResultMap.get(logs.getClazzId());
                    if (resultList == null) {
                        resultList = new ArrayList<InspectionResult>();
                    }
                    Map<String, Object> resultParam = new HashMap<>();
                    resultParam.put("logsId", logs.getId());
                    List<InspectionResult> queryResult = inspectionResultService.query(resultParam);
                    resultList.addAll(queryResult);
                    clazzResultMap.put(logs.getClazzId(), resultList);
                }
            }


            //循环班级
            for (ClazzInf clazzInf : clazzList) {
                InspectionLogs clazzExistLogs = null;
                //查找该班级的巡查记录
                for (InspectionLogs logs : logsList) {
                    if (logs.getClazzId().equals(clazzInf.getId())) {
                        clazzExistLogs = logs;
                        break;
                    }
                }

                //当日没巡查则新增巡查
                if (clazzExistLogs == null) {
                    float totalScore = 0;

                    if (attrList != null && attrList.size() > 0) {
                        InspectionLogs newLog = new InspectionLogs();
                        newLog.setCategory3(DICT_ITEMCODE_TEACHER);
                        newLog.setClazzId(clazzInf.getId());
                        newLog.setClazz(clazzInf.getClazz());
                        newLog.setGrade(clazzInf.getGrade());
                        newLog.setTemplateId(template.getId());
                        newLog.setCzyId(null);
                        newLog.setCzy("系统打分");
                        newLog.setSchoolId(clazzInf.getSchoolId().toString());
                        newLog.setAddTime(nowTime);
                        newLog.setTeamId(clazzInf.getTeamId());
                        newLog.setTeamName(clazzInf.getTeamName());
                        //newLog.setTotalScore(totalScore + "");
                        inspectionLogsService.insert(newLog);


                        //为每个属性生成result明细
                        for (DynamicAttr attr : attrList) {
                            try {
                                String[] scoreArray = attr.getScore().split("\\|");
                                String[] optionArray = attr.getAttrOption().split("\\|");
                                float middleScore = Float.parseFloat(scoreArray[1]);
                                String attrOption = optionArray[1];
                                totalScore = totalScore + middleScore;

                                InspectionResult newResult = new InspectionResult();
                                newResult.setLogsId(newLog.getId());
                                newResult.setAttrId(attr.getId());
                                newResult.setAttrName(attr.getName());
                                newResult.setAttrValue(attrOption);
                                newResult.setAddTime(nowTime);
                                newResult.setItemCode(attr.getItemCode());
                                newResult.setItemScore(middleScore + "");
                                newResult.setSchoolId(attr.getSchoolId());
                                inspectionResultService.insert(newResult);
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                        }
                        newLog.setTotalScore(String.valueOf(totalScore));
                        inspectionLogsService.update(newLog);
                    }
                } else {
                    //如果当日已经巡查，检查当日是否有未巡查的属性，如果有则补充result
                    logger.debug("今日" + clazzInf.getClazz() + "后勤巡查已巡查...");
                    float totalScore = Float.parseFloat(clazzExistLogs.getTotalScore());
                    //该班级下今天的巡查result明细
                    List<InspectionResult> resultList = clazzResultMap.get(clazzInf.getId());
                    for (DynamicAttr attr : attrList) {
                        try {
                            //查询是否已经存在该属性的result
                            boolean existResultFlag = false;
                            for (InspectionResult result : resultList) {
                                if (attr.getId().equals(result.getAttrId())) {
                                    existResultFlag = true;
                                }
                            }
                            //存在该属性的result，则不生成result
                            if (existResultFlag) {
                                continue;
                            }

                            String[] scoreArray = attr.getScore().split("\\|");
                            String[] optionArray = attr.getAttrOption().split("\\|");
                            float middleScore = Float.parseFloat(scoreArray[1]);
                            String attrOption = optionArray[1];
                            totalScore = totalScore + middleScore;

                            InspectionResult newResult = new InspectionResult();
                            newResult.setLogsId(clazzExistLogs.getId());
                            newResult.setAttrId(attr.getId());
                            newResult.setAttrName(attr.getName());
                            newResult.setAttrValue(attrOption);
                            newResult.setAddTime(nowTime);
                            newResult.setItemCode(attr.getItemCode());
                            newResult.setItemScore(middleScore + "");
                            newResult.setSchoolId(attr.getSchoolId());
                            inspectionResultService.insert(newResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    clazzExistLogs.setTotalScore(String.valueOf(totalScore));
                    inspectionLogsService.update(clazzExistLogs);
                }

            }
        }

    }


    /**
     * 每天晚上18点30分，自动生成当天校务巡查未巡查各班级分数
     */
    @Scheduled(cron = "0 30 18 * * ? ")
    public void xiaoWuAutoScore() {
        logger.info("校务巡查，默认打分开始执行...");
        logger.info("每天晚上18点30分，自动生成当天校务巡查未巡查各班级分数...");

        Timestamp nowTime = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        String nowDate = sdf.format(nowTime.getTime());


        //循环学校
        for (long schoolId = 1; schoolId <= 3; schoolId++) {
            if (todayIsHoliday(schoolId)) {
                continue;
            }
            //查询该校模板
            Map templateParam = new HashMap();
            templateParam.put("name", "校务巡查");
            templateParam.put("schoolId", schoolId);
            InspectionTemplate template = inspectionTemplateService.get(templateParam);
            if (template == null) {
                logger.error("schoolId:" + schoolId + ",找不到校务巡查模板信息...");
                continue;
            }

            //查询该校班级
            Map<String, Object> clazzParam = new HashMap<String, Object>();
            clazzParam.put("status", "0");
            clazzParam.put("schoolId", schoolId);
            List<ClazzInf> clazzList = clazzInfService.query(clazzParam);

            //查询该校2级分类
            Map<String, Object> categoryParam = new HashMap<String, Object>();
            categoryParam.put("templateId", template.getId());
            categoryParam.put("schoolId", schoolId);
            categoryParam.put("ilevel", "2");
            List<InspectionCategory> categoryList = inspectionCategoryService.query(categoryParam);

            //查询该校今日的所有后勤巡查记录
            Map<String, Object> logsParam = new HashMap<String, Object>();
            logsParam.put("addTime", nowDate);
            logsParam.put("templateId", template.getId());
            logsParam.put("schoolId", schoolId);
            List<InspectionLogs> logsList = inspectionLogsService.query(logsParam);


            //循环班级
            for (ClazzInf clazzInf : clazzList) {
                //循环分类 每个分类下保证2条logs 教师和学生
                for (InspectionCategory category : categoryList) {
                    InspectionLogs clazzExistTeacherLogs = null;
                    InspectionLogs clazzExistStudentLogs = null;
                    //查找该班级的巡查记录
                    for (InspectionLogs logs : logsList) {
                        if (logs.getClazzId().equals(clazzInf.getId()) && category.getId().equals(logs.getCategory2())) {
                            if (logs.getCategory3().equals(DICT_ITEMCODE_TEACHER)) {
                                clazzExistTeacherLogs = logs;
                            }
                            if (logs.getCategory3().equals(DICT_ITEMCODE_STUDENT)) {
                                clazzExistStudentLogs = logs;
                            }
                        }
                    }

                    //当日没巡查则新增巡查
                    if (clazzExistTeacherLogs == null) {
                        saveXiaoWuLogs(nowTime, schoolId, template, clazzInf, category, DICT_ITEMCODE_TEACHER);
                    } else {
                        //如果当日已经巡查，则不需要自动打分
                        logger.debug("今日" + clazzInf.getClazz() + "的校务巡查（" + category.getValue() + "）教师项目已巡查...");
                    }

                    //当日没巡查则新增巡查
                    if (clazzExistStudentLogs == null) {
                        saveXiaoWuLogs(nowTime, schoolId, template, clazzInf, category, DICT_ITEMCODE_STUDENT);
                    } else {
                        //如果当日已经巡查，则不需要自动打分
                        logger.debug("今日" + clazzInf.getClazz() + "的校务巡查（" + category.getValue() + "）学生项目已巡查...");
                    }
                }
            }
        }
    }

    /**
     * 校务巡查-自动打分-保存巡查记录
     *
     * @param nowTime
     * @param schoolId
     * @param template
     * @param clazzInf
     * @param category
     * @param category3
     */
    private void saveXiaoWuLogs(Timestamp nowTime, long schoolId, InspectionTemplate template, ClazzInf clazzInf, InspectionCategory category, Long category3) {
        float totalScore = 0;
        Map<String, Object> attrParam = new HashMap<String, Object>();
        attrParam.put("templateId", template.getId());
        attrParam.put("schoolId", schoolId);
        attrParam.put("seq", category.getId());//二级分类
        if (category3.equals(DICT_ITEMCODE_TEACHER)) {
            attrParam.put("itemCode", "1");
        } else if(category3.equals(DICT_ITEMCODE_STUDENT)) {
            attrParam.put("itemCode", "2");
        }
        List<DynamicAttr> attrList = dynamicAttrService.query(attrParam);

        if (attrList != null && attrList.size() > 0) {
            InspectionLogs newLog = new InspectionLogs();
            newLog.setCategory2(category.getId());
            newLog.setCategory3(category3);
            newLog.setClazzId(clazzInf.getId());
            newLog.setClazz(clazzInf.getClazz());
            newLog.setGrade(clazzInf.getGrade());
            newLog.setTemplateId(template.getId());
            newLog.setCzyId(null);
            newLog.setCzy("系统打分");
            newLog.setSchoolId(clazzInf.getSchoolId().toString());
            newLog.setAddTime(nowTime);
            newLog.setTeamId(clazzInf.getTeamId());
            newLog.setTeamName(clazzInf.getTeamName());
            //newLog.setTotalScore(totalScore + "");
            inspectionLogsService.insert(newLog);

            for (DynamicAttr attr : attrList) {
                try {
                    String[] scoreArray = attr.getScore().split("\\|");
                    String[] optionArray = attr.getAttrOption().split("\\|");
                    float middleScore = Float.parseFloat(scoreArray[1]);
                    String attrOption = optionArray[1];
                    totalScore = totalScore + middleScore;

                    InspectionResult newResult = new InspectionResult();
                    newResult.setLogsId(newLog.getId());
                    newResult.setAttrId(attr.getId());
                    newResult.setAttrName(attr.getName());
                    newResult.setAttrValue(attrOption);
                    newResult.setAddTime(nowTime);
                    newResult.setItemCode(attr.getItemCode());
                    newResult.setItemScore(middleScore + "");
                    newResult.setSchoolId(attr.getSchoolId());
                    inspectionResultService.insert(newResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            newLog.setTotalScore(String.valueOf(totalScore));
            inspectionLogsService.update(newLog);
        }
    }

    /**
     * 每天6点到18点 偏移2分钟 每5分钟执行一次,查看是否巡查超时并通报(教师执勤)
     * 任务开始后，2分钟未打卡的，系统自动通报（通报消息集中一条发出） 通报给当日值班校务巡查的老师
     */
    @Scheduled(cron = "0 2/5 6-18 * * ? ")
    public void jiaoshiNoticeMx() throws Exception {
        logger.info("教师执勤未到岗通报...");
        logger.info("每天6点到18点每隔5分钟，自动通报...");

        Timestamp nowDate = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        Date before2minuteDate = new Date(nowDate.getTime() - 120000);//120秒（2分钟）之前的日期
        Map<String, Object> scheduleParam = new HashMap<String, Object>();
        Map<String, Object> categoryParam = new HashMap<String, Object>();
        scheduleParam.put("dutyDate", sdf.format(System.currentTimeMillis()));
        scheduleParam.put("templateName", "教师执勤");
        //得到今天所有要推送的消息
        List<Schedule> scheduleList = scheduleService.query(scheduleParam);
        if (scheduleList != null && scheduleList.size() > 0) {
            for (Schedule schedule : scheduleList) {
                //根据校历  如果当天不开学，则不需要 通知
                if (todayIsHoliday(schedule.getSchoolId())) {
                    continue;
                }
                //要值班的用户
                List<UserInf> userList = scheduleService.getTeachersBySchedule(schedule);

                categoryParam.put("templateName", "教师执勤");
                categoryParam.put("schoolId", schedule.getSchoolId());
                //获取全部 教师执勤 巡检项目
                List<InspectionCategory> categoryAllList = inspectionCategoryService.query(categoryParam);
                for (InspectionCategory category : categoryAllList) {
                    StringBuffer timeContent = new StringBuffer();
                    StringBuffer teacherName = new StringBuffer();
                    //找出2分钟之前符合时间范围的项目
                    if (inspectionCategoryService.inTime(category, before2minuteDate)) {
                        //将自定义时间重新设置项目开始和结束时间，如果无自定义时间则无操作
                        inspectionCategoryService.resetStartAndEndTime(category, new Date());
                        timeContent.append(category.getValue()).append(category.getStartTime()).append("——").append(category.getEndTime()).append("期间,");
                        //针对该项目检查每个用户是否已提交执勤信息
                        for (UserInf userInfo : userList) {
                            //查询今天该用户提交的执勤消息
                            Map<String, Object> messageParam = new HashMap<String, Object>();
                            messageParam.put("title", "教师执勤");
                            messageParam.put("startHours", sdf.format(nowDate) + " " + category.getStartTime() + ":00");//今天+开始时间
                            messageParam.put("endHours", sdf.format(nowDate) + " " + category.getEndTime() + ":00");//今天+结束时间
                            messageParam.put("userId", userInfo.getId());
                            messageParam.put("schoolId", schedule.getSchoolId());
                            List<InspectionMessage> messageList = inspectionMessageService.query(messageParam);
                            if (messageList != null && messageList.size() > 0) {
                                //查询存在执勤消息，如果已经执勤了，就不再推通知消息了
                                logger.debug("用户：{},已经执勤，无需通报", userInfo.getUserName());
                                continue;
                            } else {
                                //如果还没有执勤消息
                                //检查是否已经通报过
                                //如果已经通报过，则不再通报
                                Map noticeMxParam = new HashMap();
                                noticeMxParam.put("startTime", category.getStartTime());
                                noticeMxParam.put("endTime", category.getEndTime());
                                noticeMxParam.put("userName", userInfo.getUserName());
                                noticeMxParam.put("dutyDate", schedule.getDutyDate());
                                noticeMxParam.put("schoolId", schedule.getSchoolId());
                                noticeMxParam.put("templateName", "教师执勤");
                                List noticeMxList = noticeMxService.query(noticeMxParam);
                                if (noticeMxList != null && noticeMxList.size() > 0) {
                                    logger.debug("用户：{}没有执勤消息,已经通报过，无需再次通报", userInfo.getUserName());
                                    continue;
                                }
                                //如果还没通报过，则加入通报名单
                                Map placeParam = new HashMap();
                                placeParam.put("userId", userInfo.getId());
                                Place place = placeService.get(placeParam);
                                //加入到通报明细
                                NoticeMx noticeMx = new NoticeMx();
                                noticeMx.setUserName(userInfo.getUserName());
                                noticeMx.setDutyDate(schedule.getDutyDate());
                                noticeMx.setStartTime(category.getStartTime());
                                noticeMx.setEndTime(category.getEndTime());
                                noticeMx.setTemplateName("教师执勤");
                                noticeMx.setSchoolId(schedule.getSchoolId() + "");
                                if (place != null) {
                                    teacherName.append(place.getPlaceName()).append("(值班老师：" + userInfo.getUserName() + ")、");
                                    noticeMx.setPlace(place.getPlaceName());
                                } else {
                                    teacherName.append("(值班老师：" + userInfo.getUserName() + ")、");
                                }
                                noticeMxService.insert(noticeMx);
                            }

                        }
                        //存在通报消息
                        if (StringUtils.isNotEmpty(teacherName.toString())) {
                            StringBuffer noticeContent = new StringBuffer();
                            noticeContent.append("在今日")
                                    .append(timeContent.toString())
                                    .append(teacherName.toString().substring(0, teacherName.length() - 1))
                                    .append("未按照规定时间到岗，予以通报");
                            //将本分类的未到岗教师名单 通报给今天进行校务巡查的排班人员
                            Map<String, Object> XWscheduleParam = new HashMap();
                            XWscheduleParam.put("dutyDate", sdf.format(nowDate));
                            XWscheduleParam.put("templateName", "校务巡查");
                            XWscheduleParam.put("schoolId", schedule.getSchoolId());
                            List<Schedule> XWscheduleList = scheduleService.query(XWscheduleParam);
                            for (Schedule XWschedule : XWscheduleList) {
                                List<UserInf> XWuserInfoList = scheduleService.getTeachersBySchedule(XWschedule);
                                for (UserInf XWUserinfo : XWuserInfoList) {
                                    Notice notice = new Notice();
                                    notice.setTitle("通报");
                                    notice.setContent(noticeContent.toString());
                                    notice.setType("1");//值班提醒
                                    notice.setUserId(XWUserinfo.getId() + 0L);
                                    notice.setIsread("0");//未读
                                    notice.setAddtime(new Timestamp(System.currentTimeMillis()));
                                    notice.setEdittime(new Timestamp(System.currentTimeMillis()));
                                    notice.setDutyDate(schedule.getDutyDate());
                                    notice.setSchoolId(schedule.getSchoolId());
                                    noticeService.insert(notice);
                                    logger.debug("给用户：{}，发送通报消息{}", XWUserinfo.getUserName(), noticeContent.toString());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            logger.debug("没有排班数据，无法推送通知提");
        }

    }


    /**
     * 每天6点到18点 偏移2分钟5秒  每5分钟执行一次,查看是否巡查超时并通报(护校队巡查)
     * 通报给指定用户
     */
    @Scheduled(cron = "5 2/5 6-18 * * ? ")
    public void huxiaoduiNoticeMx() throws Exception {
        logger.info("护校队执勤未到岗通报...");
        logger.info("每天6点到18点，每隔5分钟，自动通报...");

        Timestamp nowDate = new Timestamp((System.currentTimeMillis() / 1000) * 1000);
        Date before2minuteDate = new Date(nowDate.getTime() - 120000);//120秒（2分钟）之前的日期
        Map<String, Object> scheduleParam = new HashMap<String, Object>();
        Map<String, Object> categoryParam = new HashMap<String, Object>();
        scheduleParam.put("dutyDate", sdf.format(System.currentTimeMillis()));
        scheduleParam.put("templateName", "护校队巡查");
        //得到今天所有要推送的消息
        List<Schedule> scheduleList = scheduleService.query(scheduleParam);
        if (scheduleList != null && scheduleList.size() > 0) {
            for (Schedule schedule : scheduleList) {
                //根据校历  如果当天不开学，则不需要 通知
                if (todayIsHoliday(schedule.getSchoolId())) {
                    continue;
                }
                //要值班的用户
                List<UserInf> userList = scheduleService.getTeachersBySchedule(schedule);

                //通报给该用户
                Map dictParam = new HashMap();
                if (schedule.getSchoolId() == 1) {
                    dictParam.put("dictName", "国际人员");
                } else if (schedule.getSchoolId() == 2) {
                    dictParam.put("dictName", "阳光人员");
                } else if (schedule.getSchoolId() == 3) {
                    dictParam.put("dictName", "CBD人员");
                }
                Dict dict = dictService.get(dictParam);
                UserInf noticeToUser = new UserInf();
                noticeToUser.setUserName(dict.getDictValue());
                noticeToUser = userService.getEntity(noticeToUser);

                categoryParam.put("templateName", "护校队巡查");
                categoryParam.put("schoolId", schedule.getSchoolId());
                //获取全部 教师执勤 巡检项目
                List<InspectionCategory> categoryAllList = inspectionCategoryService.query(categoryParam);
                for (InspectionCategory category : categoryAllList) {
                    StringBuffer timeContent = new StringBuffer();
                    StringBuffer teacherName = new StringBuffer();
                    //找出2分钟之前符合时间范围的项目
                    if (inspectionCategoryService.inTime(category, before2minuteDate)) {
                        //将自定义时间重新设置项目开始和结束时间，如果无自定义时间则无操作
                        inspectionCategoryService.resetStartAndEndTime(category, new Date());
                        timeContent.append(category.getValue()).append(category.getStartTime()).append("——").append(category.getEndTime()).append("期间,");
                        //针对该项目检查每个用户是否已提交执勤信息
                        for (UserInf userInfo : userList) {
                            //查询今天该用户提交的执勤消息
                            Map<String, Object> messageParam = new HashMap<String, Object>();
                            messageParam.put("title", "护校队巡查");
                            messageParam.put("startHours", sdf.format(nowDate) + " " + category.getStartTime() + ":00");//今天+开始时间
                            messageParam.put("endHours", sdf.format(nowDate) + " " + category.getEndTime() + ":00");//今天+结束时间
                            messageParam.put("userId", userInfo.getId());
                            messageParam.put("schoolId", schedule.getSchoolId());
                            List<InspectionMessage> messageList = inspectionMessageService.query(messageParam);
                            if (messageList != null && messageList.size() > 0) {
                                //查询存在执勤消息，如果已经执勤了，则不再通报
                                logger.debug("用户：{},已经执勤，无需通报", userInfo.getUserName());
                                continue;
                            } else {
                                //如果还没有执勤消息
                                //检查是否已经通报过
                                //如果已经通报过，则不再通报
                                Map noticeMxParam = new HashMap();
                                noticeMxParam.put("startTime", category.getStartTime());
                                noticeMxParam.put("endTime", category.getEndTime());
                                noticeMxParam.put("userName", userInfo.getUserName());
                                noticeMxParam.put("dutyDate", schedule.getDutyDate());
                                noticeMxParam.put("schoolId", schedule.getSchoolId());
                                noticeMxParam.put("templateName", "护校队巡查");
                                List noticeMxList = noticeMxService.query(noticeMxParam);
                                if (noticeMxList != null && noticeMxList.size() > 0) {
                                    logger.debug("用户：{}没有执勤消息,已经通报过，无需再次通报", userInfo.getUserName());
                                    continue;
                                }

                                //如果还没通报过，则加入通报名单
                                Map placeParam = new HashMap();
                                placeParam.put("userId", userInfo.getId());
                                Place place = placeService.get(placeParam);
                                //加入到通报明细
                                NoticeMx noticeMx = new NoticeMx();
                                noticeMx.setUserName(userInfo.getUserName());
                                noticeMx.setDutyDate(schedule.getDutyDate());
                                noticeMx.setStartTime(category.getStartTime());
                                noticeMx.setEndTime(category.getEndTime());
                                noticeMx.setTemplateName("护校队巡查");
                                noticeMx.setSchoolId(schedule.getSchoolId() + "");
                                if (place != null) {
                                    teacherName.append(place.getPlaceName()).append("(值班老师：" + userInfo.getUserName() + ")、");
                                    noticeMx.setPlace(place.getPlaceName());
                                } else {
                                    teacherName.append("(值班老师：" + userInfo.getUserName() + ")、");
                                }
                                noticeMxService.insert(noticeMx);
                            }
                        }
                        //存在通报消息
                        if (StringUtils.isNotEmpty(teacherName.toString())) {
                            StringBuffer noticeContent = new StringBuffer();
                            noticeContent.append("在今日")
                                    .append(timeContent.toString())
                                    .append(teacherName.toString().substring(0, teacherName.length() - 1))
                                    .append("未按照规定时间到岗，予以通报");
                            //将本分类的未到岗教师名单 通报给字典表指定用户
                            Notice notice = new Notice();
                            notice.setTitle("通报");
                            notice.setContent(noticeContent.toString());
                            notice.setType("1");//值班提醒
                            notice.setUserId(noticeToUser.getId() + 0L);
                            notice.setIsread("0");//未读
                            notice.setAddtime(new Timestamp(System.currentTimeMillis()));
                            notice.setEdittime(new Timestamp(System.currentTimeMillis()));
                            notice.setDutyDate(schedule.getDutyDate());
                            notice.setSchoolId(schedule.getSchoolId());
                            noticeService.insert(notice);
                            logger.debug("给用户：{}，发送通报消息{}", noticeToUser.getUserName(), noticeContent.toString());
                        }
                    }
                }
            }
        } else {
            logger.debug("没有排班数据，无法推送通知提");
        }
    }
}