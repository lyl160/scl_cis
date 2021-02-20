package cn.dofuntech.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * api 标准返回对象
 * </p>
 * <font size=0.25>Copyright (C) 2018 dofuntech. All Rights Reserved.</font>
 * @author lxu(2018年4月19日)
 * @version 1.0
 * filename:ApiResult.java 
 */
public class ApiResult {

    public static final String      SUCESS  = "200";
    public static final String      FAILURE = "500";

    private String                  code;
    private String                  msg;
    private Object                  data;
    private HashMap<String, Object> extra   = new HashMap<String, Object>();

    public ApiResult() {
    }

    public ApiResult(Object data) {
        this.data = data;
    }

    public ApiResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void put(String key, Object value) {
        this.extra.put(key, value);
    }

    public HashMap<String, Object> getMap() {
        return this.extra;
    }

    public void putAll(Map<String, Object> map) {
        this.extra.putAll(map);
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg == null ? "ok" : this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ApiResult setFail() {
        setCode(FAILURE);
        return this;
    }

    public ApiResult setFail(String msg) {
        setCode(FAILURE);
        setMsg(msg);
        return this;
    }

    public ApiResult setFail(String msg, Map<String, Object> data) {
        setCode(FAILURE);
        putAll(data);
        return this;
    }

    public ApiResult setSuccess() {
        setCode(SUCESS);
        return this;
    }

    public ApiResult setSuccess(String msg) {
        setCode(SUCESS);
        setMsg(msg);
        return this;
    }

    public ApiResult setSuccessWithData(Object data) {
        setCode(SUCESS);
        setData(data);
        return this;
    }

    public ApiResult setSuccess(String msg, Map<String, Object> data) {
        setCode(SUCESS);
        putAll(data);
        return this;
    }

    public void setMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}