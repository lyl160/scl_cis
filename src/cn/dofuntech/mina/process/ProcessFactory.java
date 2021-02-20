package cn.dofuntech.mina.process;

import java.util.HashSet;
import java.util.Set;

import cn.dofuntech.mina.process.handle.ResponseHandle;
import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:ProcessFactory.java 
 */
public class ProcessFactory {

    public static Set<AbstractHandle> handles;

    static {
        regist(new ResponseHandle());
    }

    /**
     * @param msgPack
     * @return
     */
    public static AbstractHandle getHandle(MsgPack msgPack) {
        AbstractHandle rval = null;
        for (AbstractHandle handle : handles) {
            if (handle.canDo(msgPack)) {
                rval = handle;
                break;
            }
        }
        if (rval == null) {
            rval = new DefaultHandle();
        }
        return rval;
    }

    /**
     * @param msgPack
     * @return
     */
    public static AbstractHandle getAuthHandle() {
        return handles.iterator().next();
    }

    /**
     * 注册处理器
     * @param handle
     */
    public static void regist(AbstractHandle handle) {
        if (handles == null) {
            handles = new HashSet<AbstractHandle>();
        }
        handles.add(handle);
    }
}
