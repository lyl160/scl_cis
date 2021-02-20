package cn.dofuntech.mina.process.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:HandleResult.java 
 */
public class HandleResult implements Serializable {

    /**
     * 
     */
    private static final long   serialVersionUID = 1L;

    private List<MsgPack>       msgPackList;

    private int                 code             = 1;

    private Map<String, Object> extras           = new HashMap<String, Object>(); //额外的信息存储

    /**
     * @param msgPack
     */
    public HandleResult(MsgPack... msgPack) {
        super();
        this.msgPackList = Arrays.asList(msgPack);
    }

    /**
     * @return the msgPack
     */
    public List<MsgPack> getMsgPack() {
        return msgPackList;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Object put(String key, Object value) {
        return extras.put(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Object get(String key) {
        return extras.get(key);
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

}
