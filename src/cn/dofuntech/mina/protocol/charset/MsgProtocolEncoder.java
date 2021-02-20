package cn.dofuntech.mina.protocol.charset;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月14日)
 * @version 1.0
 * filename:MsgProtocolEncoder.java 
 */
public class MsgProtocolEncoder extends ProtocolEncoderAdapter {

    private static Logger Log     = LoggerFactory.getLogger(MsgProtocolEncoder.class);

    private Charset       charset = null;

    public MsgProtocolEncoder(Charset charset) {
        this.charset = charset;
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if (message instanceof MsgPack) {
            MsgPack mp = (MsgPack) message;
            Log.debug("编码包内容：{}", mp.debugInfo());
            IoBuffer buf = IoBuffer.allocate(mp.getMsgLength());
            buf.order(ByteOrder.LITTLE_ENDIAN);
            buf.setAutoExpand(true);
            //设置包头
            buf.put(mp.getMsgHead());
            //设置包长
            buf.putInt(mp.getMsgLength());
            //设置协议版本
            buf.putShort(mp.getVersion());
            //设置包类型
            buf.put(mp.getMsgMethod());
            //设置命令字
            buf.put(mp.getMsgCw());
            //设置数据
            if (null != mp.getData()) {
                buf.put(mp.getData().getBytes(charset));
            }
            //设置校验码
            buf.putInt(mp.getAuthCode());
            buf.flip();
            out.write(buf);
            out.flush();
            buf.free();
        }
    }

    public void dispose() throws Exception {
    }

}
