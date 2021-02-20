package cn.dofuntech.mina.protocol.parse;

import cn.dofuntech.mina.protocol.Constants;
import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:PackParseUtil.java 
 */
public class PackParseUtil {

    /**
     * 请求包
     * @param msgPack
     * @return
     */
    public static boolean isRequest(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.SEND && msgPack.getMsgCw() != Constants.MsgCw.AUTHENTICATION;
    }

    /**
     * 鉴权包(与请求包单独分离)
     * @param msgPack
     * @return
     */
    public static boolean isAuthRequset(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.SEND && msgPack.getMsgCw() == Constants.MsgCw.AUTHENTICATION;
    }

    /**
     * 应答包
     * @param msgPack
     * @return
     */
    public static boolean isResonpse(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.ACK;
    }

    /**
     * 信息包
     * @param msgPack
     * @return
     */
    public static boolean isInfoResponse(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.INFO;
    }

    /**
     * 异常包
     * @param msgPack
     * @return
     */
    public static boolean isErrorResonpse(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.NCK;
    }

    /**
     * 心跳包
     * @param msgPack
     * @return
     */
    public static boolean isHeartBeat(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.HEARTBEAT;
    }

    /**
     * 心跳应答包
     * @param msgPack
     * @return
     */
    public static boolean isHeartBeatResponse(MsgPack msgPack) {
        if (msgPack == null)
            return false;
        return msgPack.getMsgMethod() == Constants.MsgMethod.HEARTBEAT;
    }

}
