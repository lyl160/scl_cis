package cn.dofuntech.core.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

/*********************************************************************************
//* Copyright (C) 2015 bsteel. All Rights Reserved.
//*
//* Filename:      CopyUtils.java
//* Revision:      1.0
//* Author:        lxu
//* Created On:    2015-1-30
//* Modified by:   
//* Modified On:   
//*
//* Description:   注意此类用于特殊的属性复制 （适用于PO更新场景 NULL值忽略）
/********************************************************************************/

public class CopyUtils extends BeanUtils {

    private static Logger      logger                = Logger.getLogger(CopyUtils.class);
    public static final String FULL_PATTERN_NOSECOND = "yyyy-MM-dd HH:mm";               //年月日时分秒时间格式
    public static final String FULL_PATTERN          = "yyyy-MM-dd HH:mm:ss";            //年月日时分秒时间格式
    public static final String SIMPLE_PATTERN        = "yyyy-MM-dd";                     //年月日时间格式

    static {
        ConvertUtils.register(new DateConvert(), java.sql.Date.class);
        ConvertUtils.register(new DateConvert(), java.sql.Timestamp.class);
        ConvertUtils.register(new DateConvert(), Date.class);
    }

    /**
     * 属性复制 用于PO更新
     * DTO 属性复制 到PO
     * NULL 值直接忽略
     * @param source
     * @param target
     */
    public static void copyProp4update(Object dest, Object orig) {
        if (dest == null || orig == null)
            return;
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor origDescriptors[] = propertyUtilsBean.getPropertyDescriptors(orig);
        try {
            for (int i = 0 ; i < origDescriptors.length ; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name) || !propertyUtilsBean.isReadable(orig, name) || !propertyUtilsBean.isWriteable(dest, name))
                    continue;
                Object value = propertyUtilsBean.getSimpleProperty(orig, name);
                if (value == null)
                    continue;
                copyProperty(dest, name, value);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 属性复制 继承原始的复制方法 可安全访问
     * @param source
     * @param target
     */
    public static void copyProperties(Object dest, Object orig) {
        if (dest == null || orig == null)
            return;
        try {
            BeanUtils.copyProperties(dest, orig);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 属性复制  继承原始的复制方法 可安全访问
     * @param source
     * @param target
     */
    public static <T> T copyProperties(Object orig, Class<T> clazz) {
        return copyProperties(orig, clazz, Boolean.FALSE);
    }

    /**
     * 属性复制 适用于更新
     * @param source
     * @param target
     */
    public static <T> T copyProp4update(Object orig, Class<T> clazz) {
        return copyProperties(orig, clazz, Boolean.TRUE);
    }

    /**
     * 属性复制 继承原始的复制方法 可安全访问
     * @param orig
     * @param clazz
     * @param isUpdate
     */
    public static <T> T copyProperties(Object orig, Class<T> clazz, Boolean isUpdate) {
        if (clazz == null || orig == null)
            return null;
        T dto = null;
        try {
            dto = clazz.newInstance();
            if (isUpdate) {
                copyProp4update(dto, orig);
            }
            else {
                copyProperties(dto, orig);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dto;
    }

    /**
     * PO元素集合复制为DTO元素集合
     * @param orig PO元素集合
     * @param clazz DTO类型
     * @return
     */
    public static <T, M> List<T> copyToCollection(List<M> orig, Class<T> clazz) {
        T t = null;
        List<T> list = new ArrayList<T>();
        try {
            if (orig != null)
                for (M m : orig) {
                    t = clazz.newInstance();
                    if (m instanceof Map) {
                        BeanUtils.copyProperties(t, m);
                    }
                    else {
                        copyProp4update(t, m);
                    }
                    list.add(t);
                }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static class DateConvert implements Converter {

        public Object convert(Class arg0, Object arg1) {
            if (arg1 == null) {
                return null;
            }
            if (arg1 instanceof Date) {
                return arg1;
            }
            String p = arg1.toString();
            if (p == null || p.trim().length() == 0) {
                return null;
            }
            try {
                Date date = DateUtils.parseDate(p.trim(), SIMPLE_PATTERN, FULL_PATTERN, FULL_PATTERN_NOSECOND);
                Long value = date.getTime();
                if (arg0.equals(Date.class))
                    return date;
                if (arg0.equals(java.sql.Date.class))
                    return new java.sql.Date(value);
                if (arg0.equals(java.sql.Timestamp.class))
                    return new java.sql.Timestamp(value);
                return null;
            }
            catch (Exception e) {
                logger.debug(" -- Cant't Convert to date : " + p);
                return null;
            }

        }
    }
}
