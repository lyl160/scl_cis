package cn.dofuntech.mina.protocol.charset;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.mina.protocol.ByteConvert;
import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 协议解码
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月14日)
 * @version 1.0
 * filename:MsgProtocolDecoder.java 
 */
public class MsgProtocolDecoder extends CumulativeProtocolDecoder {

    private static Logger Log     = LoggerFactory.getLogger(MsgProtocolDecoder.class);

    private Charset       charset = null;

    public MsgProtocolDecoder() {
        this(Charset.defaultCharset());
    }

    public MsgProtocolDecoder(Charset charset) {
        this.charset = charset;
    }

    public void dispose(IoSession arg0) throws Exception {

    }

    public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {

    }

    protected boolean doDecode(IoSession session, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
        ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
        if (ioBuffer.remaining() <= 5) {
            ioBuffer.reset();
            return false; //继续接收数据，以待数据完整
        }

        ioBuffer.mark();// 标记当前位置，以便reset
        byte[] headBytes = new byte[1];
        ioBuffer.get(headBytes);
        byte head = headBytes[0];// 读取1字节获取包头
        byte[] packLenthBytes = new byte[4];
        ioBuffer.get(packLenthBytes);
        int packLenth = ByteConvert.getInt(packLenthBytes); //获取包长度
        if (ioBuffer.remaining() < packLenth) {// 如果消息内容不够，则重置
            ioBuffer.reset();
            return false;// 接收新数据，以拼凑成完整数据
        }
        else {
            decodePacket(session, ioBuffer, out, head, packLenth);
        }

        if (ioBuffer.remaining() > 0) {
            ioBuffer.mark();
            return true;// 粘包处理
        }
        else {
            return false;
        }
    }

    private void decodePacket(IoSession session, IoBuffer in, ProtocolDecoderOutput out, byte head, int size) {
        try {
            MsgPack msgPack = new MsgPack();
            msgPack.setMsgHead(head);
            byte[] sizeBytes = new byte[size];
            in.get(sizeBytes);

            byte[] versionBytes = new byte[2];
            System.arraycopy(sizeBytes, 0, versionBytes, 0, 2);
            msgPack.setVersion(ByteConvert.getShort(versionBytes));

            byte[] methodBytes = new byte[1];
            System.arraycopy(sizeBytes, 2, methodBytes, 0, methodBytes.length);
            msgPack.setMsgMethod(methodBytes[0]);

            byte[] cwBytes = new byte[1];
            System.arraycopy(sizeBytes, 3, cwBytes, 0, cwBytes.length);
            msgPack.setMsgCw(cwBytes[0]);

            byte[] dataBytes = new byte[size - 8];
            System.arraycopy(sizeBytes, 4, dataBytes, 0, dataBytes.length);
            msgPack.setData(ByteConvert.getString(dataBytes), true);

            byte[] authCodeBytes = new byte[4];
            System.arraycopy(sizeBytes, size - 4, authCodeBytes, 0, authCodeBytes.length);
            msgPack.setAuthCode(ByteConvert.getInt(authCodeBytes));

            msgPack.setMsgLength(size);

            Log.debug("解码获取包内容：{}", msgPack.debugInfo());

            out.write(msgPack);

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.error(e.getMessage(), e);
        }
    }
}
