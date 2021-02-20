package cn.dofuntech.mina.protocol;

import java.util.zip.CRC32;

import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:CRC32Build.java 
 */
public class CRC32Build {

    private MsgPack msgPack;

    /**
     * @param msgPack
     */
    public CRC32Build(MsgPack msgPack) {
        super();
        this.msgPack = msgPack;
    }

    /**
     * 返回校验码
     * @return
     */
    public int authCode() {
        if (msgPack == null) {
            return 0;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(authBytes());
        return (int) crc32.getValue();
    }

    /**
     * @param authCode
     * @return
     */
    public boolean validate() {
        if (msgPack == null) {
            return false;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(authBytes());
        return (int) crc32.getValue() == msgPack.getAuthCode();
    }

    /**
     * @return
     */
    private byte[] authBytes() {
        int msgLength = msgPack.getMsgLength();
        byte[] bytes = new byte[msgLength - 3];
        //包长
        System.arraycopy(ByteConvert.getBytes(msgLength), 0, bytes, 0, 4);
        //命令字
        bytes[4] = msgPack.getMsgCw();
        //数据
        if (null != msgPack.getData()) {
            System.arraycopy(ByteConvert.getBytes(msgPack.getData()), 0, bytes, 5, msgLength - 8);
        }
        return bytes;

    }
}
