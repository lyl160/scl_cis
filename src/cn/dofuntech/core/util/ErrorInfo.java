package cn.dofuntech.core.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 通用返回结果包装
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author Lei.XU (Create on:2014年11月15日)
 * @version 1.0
 * @fileName ErrorInfo.java
 */
public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = -3640003041881125462L;
    private int               status           = -1;
    private Object            object;
    private String            errorinfo        = StringUtils.EMPTY;

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the errorinfo
     */
    public String getErrorinfo() {
        return errorinfo;
    }

    /**
     * @param errorinfo the errorinfo to set
     */
    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * @param key
     * @param value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void put(String key, Object value) {
        if (object == null) {
            object = new HashMap<String, Object>();
        }
        if (object instanceof Map) {
            ((Map) object).put(key, value);
        }
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> tranMap() {
        Map<String, Object> rval = Collections.emptyMap();
        if (this.object instanceof Map) {
            rval = (Map<String, Object>) object;
        }
        return rval;
    }

}
