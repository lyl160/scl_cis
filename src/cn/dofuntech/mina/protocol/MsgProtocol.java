package cn.dofuntech.mina.protocol;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import cn.dofuntech.mina.protocol.charset.MsgProtocolDecoder;
import cn.dofuntech.mina.protocol.charset.MsgProtocolEncoder;

/**
 * <p>
 * 自定义协议
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月14日)
 * @version 1.0
 * filename:MsgProtocol.java 
 */
public class MsgProtocol implements ProtocolCodecFactory {

    private static final Charset charset = Constants.charset;

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new MsgProtocolDecoder(charset);
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new MsgProtocolEncoder(charset);
    }
}
