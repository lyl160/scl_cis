package cn.dofuntech.cis.admin.service.impl;

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.cis.admin.repository.mapper.InspectionCategoryMapper;
import cn.dofuntech.cis.admin.service.InspectionCategoryService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.core.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <p>
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 *
 * @author lxu(@ 2016年2月2日)
 * @version 1.0
 * filename:InspectionCategoryServiceImpl.java
 */
@Service
public class InspectionCategoryServiceImpl extends DunfengServiceImpl<InspectionCategory> implements InspectionCategoryService {

    private static Logger logger = LoggerFactory.getLogger(InspectionCategoryServiceImpl.class);
    @Resource
    private InspectionCategoryMapper inspectionCategoryMapper;

    /**
     * 校内执勤 和 校外执勤 检查当前时间 是否符合 二级分类设定的通用时间 和 自定义时间
     *
     * @param category
     * @param date     当前时间
     * @return
     */
    @Override
    public boolean inTime(InspectionCategory category, Date date) {
        SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");
        boolean resultFlag = false;
        if (category != null) {
            //先查自定义
            if (StringUtils.isNotEmpty(category.getDiyTime())) {
                String[] timeArray = category.getDiyTime().split("&");
                if (timeArray.length > 0) {
                    String diyWeek;
                    String diyStartTime;
                    String diyEndTime;
                    for (String diyTime : timeArray) {
                        String[] diyTimeArray = diyTime.split(",");
                        diyWeek = diyTimeArray[0];
                        diyStartTime = diyTimeArray[1];
                        diyEndTime = diyTimeArray[2];
                        if ((DateUtils.getWeek(date) == Integer.parseInt(diyWeek))) {
                            //匹配周几
                            if ((getMinute(diyStartTime) <= getMinute(sdfHHmm.format(date)))//匹配开始时间
                                    && (getMinute(diyEndTime) >= getMinute(sdfHHmm.format(date)))//匹配结束时间
                                    ) {
                                resultFlag = true;
                                return resultFlag;
                            } else {
                                resultFlag = false;
                                return resultFlag;
                            }

                        }

                    }
                }
            }

            if (StringUtils.isNotEmpty(category.getStartTime()) && StringUtils.isNotEmpty(category.getEndTime())) {
                if ((getMinute(category.getStartTime()) <= getMinute(sdfHHmm.format(date)))//匹配开始时间
                        && (getMinute(category.getEndTime()) >= getMinute(sdfHHmm.format(date)))) {//匹配结束时间
                    resultFlag = true;
                    return resultFlag;
                }
            }
        }

        return resultFlag;

    }

    Integer getMinute(String HHmm) {
        Integer result = null;
        if (StringUtils.isNotEmpty(HHmm)) {
            String[] time = HHmm.split(":");
            if (time.length == 2) {
                return Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
            }
        }
        return result;
    }


    @Override
    public void resetStartAndEndTime(InspectionCategory category, Date date) {
        SimpleDateFormat sdfHHmm = new SimpleDateFormat("HH:mm");
        if (category != null) {
            //先查自定义
            if (StringUtils.isNotEmpty(category.getDiyTime())) {
                String[] timeArray = category.getDiyTime().split("&");
                if (timeArray.length > 0) {
                    String diyWeek;
                    String diyStartTime;
                    String diyEndTime;
                    for (String diyTime : timeArray) {
                        String[] diyTimeArray = diyTime.split(",");
                        diyWeek = diyTimeArray[0];
                        diyStartTime = diyTimeArray[1];
                        diyEndTime = diyTimeArray[2];
                        if ((DateUtils.getWeek(date) == Integer.parseInt(diyWeek))) {
                            //匹配周几
                            category.setStartTime(diyStartTime);
                            category.setEndTime(diyEndTime);
                            logger.debug("分类：{}，存在自定义时间", category.getName());
                        }

                    }
                }
            }

        }

    }

    @Override
    public List<InspectionCategory> queryByParam(Map<String, Object> map) {
        return inspectionCategoryMapper.queryByParam(map);
    }

    @Override
    public String getTemplateIdByType(String schoolId, String type) {
        if ("1".equals(schoolId)) {
            if ("4".equals(type)) {
                return "3";
            }
            if ("5".equals(type)) {
                return "4";
            }
        }
        //todo 需要补充其他学校的映射关系
        return null;
    }
}