package cn.dofuntech.mina.process;

import org.apache.mina.core.session.IoSession;

import cn.dofuntech.mina.process.domain.HandleResult;
import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:DefaultHandle.java 
 */
public class DefaultHandle extends AbstractHandle {

    /* (non-Javadoc)
     * @see cn.dofuntech.mina.process.AbstractHandle#canDo(cn.dofuntech.mina.protocol.domain.MsgPack)
     */
    @Override
    public boolean canDo(MsgPack msgPack) {
        return false;
    }

    /* (non-Javadoc)
     * @see cn.dofuntech.mina.process.AbstractHandle#handle(cn.dofuntech.mina.protocol.domain.MsgPack)
     */
    @Override
    protected HandleResult handle(MsgPack msgPack, IoSession session) {
        throw new RuntimeException("未找到可以处理的实例");
    }

}
