package cn.dofuntech.mina.protocol.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import cn.dofuntech.mina.protocol.CRC32Build;
import cn.dofuntech.mina.protocol.Constants;

/**
 * <p>
 * 自定义数据包
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月14日)
 * @version 1.0
 * filename:MsgPack.java 
 */
public class MsgPack implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte              msgHead          = 0x7e; //包头
    private int               msgLength;              //包长度
    private short             version          = 10;  //协议版本
    private byte              msgMethod;              //包类型
    private byte              msgCw;                  //命令字
    private String            data;                   //数据
    private int               authCode         = 0x6e; //校验码

    public MsgPack() {
        super();
    }

    /**
     * @param msgLength
     * @param version
     * @param msgMethod
     * @param data
     * @param authCode
     */
    public MsgPack(byte msgMethod, byte msgCw, String data) {
        super();
        this.msgMethod = msgMethod;
        this.msgCw = msgCw;
        this.data = data;
        this.msgLength = 8;
        if (this.data != null) {
            this.msgLength += this.data.getBytes(Constants.charset).length;
        }
        this.authCode = new CRC32Build(this).authCode();
    }

    public String debugInfo() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }

    /**
     * @return the msgHead
     */
    public byte getMsgHead() {
        return msgHead;
    }

    /**
     * @param msgHead the msgHead to set
     */
    public void setMsgHead(byte msgHead) {
        this.msgHead = msgHead;
    }

    /**
     * @return the msgLength
     */
    public int getMsgLength() {
        return msgLength;
    }

    /**
     * @param msgLength the msgLength to set
     */
    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    /**
     * @return the version
     */
    public short getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(short version) {
        this.version = version;
    }

    /**
     * @return the msgMethod
     */
    public byte getMsgMethod() {
        return msgMethod;
    }

    /**
     * @param msgMethod the msgMethod to set
     */
    public void setMsgMethod(byte msgMethod) {
        this.msgMethod = msgMethod;
    }

    /**
     * @return the msgCw
     */
    public byte getMsgCw() {
        return msgCw;
    }

    /**
     * @param msgCw the msgCw to set
     */
    public void setMsgCw(byte msgCw) {
        this.msgCw = msgCw;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.setData(data, false);
    }

    /**
     * @param data the data to set
     */
    public void setData(String data, boolean isOri) {
        this.data = data;
        if (!isOri) {
            if (data != null) {
                this.msgLength = 8 + this.data.getBytes(Constants.charset).length;
            }
            this.authCode = new CRC32Build(this).authCode();
        }
    }

    /**
     * @return the authCode
     */
    public int getAuthCode() {
        return authCode;
    }

    /**
     * @param authCode the authCode to set
     */
    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }

}
