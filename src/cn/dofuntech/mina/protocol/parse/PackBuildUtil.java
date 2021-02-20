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
 * filename:PackBuildUtil.java 
 */
public class PackBuildUtil {

    /**
     * 创建心跳包
     * @param data
     * @return
     */
    public static MsgPack buildHeartBeatPack(String data) {
        return new MsgPack(Constants.MsgMethod.HEARTBEAT, Constants.MsgCw.UPDATE_TOKEN, data);
    }

    /**
     * 创建APP信息包
     * @param data
     * @return
     */
    public static MsgPack buildAppInfoPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.APP_INFO, data);
    }
    /**
     * 创建查询APPinfo信息包
     * @param data
     * @return
     */
    public static MsgPack buildAppInfoQueryPack(String data) {
    	return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.QUERY_APP_INFO, data);
    }

    /**
     * 创建推送信息包
     * @param data
     * @return
     */
    public static MsgPack buildPushInfoPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.PUSH_CONFIG, data);
    }

    /**
     * 创建推送信息包
     * @param data
     * @return
     */
    public static MsgPack buildPushAppPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.PUSH_APP, data);
    }

    /**
     * 创建查询APP请求包
     * @param data
     * @return
     */
    public static MsgPack buildListAppSendPack(String data) {
        return new MsgPack(Constants.MsgMethod.SEND, Constants.MsgCw.APP_SETUP, data);
    }

    /**
     * 创建绑定设备信息包
     * @param data
     * @return
     */
    public static MsgPack buildBindDeviceInfoPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.DEVICE_BIND, data);
    }

    /**
     * 创建绑定设备信息包
     * @param data
     * @return
     */
    public static MsgPack buildBindDeviceRequestPack(String data) {
        return new MsgPack(Constants.MsgMethod.SEND, Constants.MsgCw.DEVICE_BIND_REQUEST, data);
    }

    /**
     * 创建卸载APP信息包
     * @param data
     * @return
     */
    public static MsgPack buildUninstallDeviceAppPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.DEVICE_APP_UNIINSTALL, data);
    }
    
    /**
     * 创建广播信息包
     * @param data
     * @return
     */
    public static MsgPack buildBoradcastPack(String data) {
        return new MsgPack(Constants.MsgMethod.INFO, Constants.MsgCw.PUSH_BROADCASE, data);
    }

    /**
     * 正常应答包
     * @param data
     * @return
     */
    public static MsgPack buildACKPack(byte msgCw) {
        return new MsgPack(Constants.MsgMethod.ACK, msgCw, null);
    }

    /**
     * 正常应答包
     * @param data
     * @return
     */
    public static MsgPack buildACKPack(byte msgCw, String data) {
        return new MsgPack(Constants.MsgMethod.ACK, msgCw, data);
    }

    /**
     * 异常应答包
     * @param data
     * @return
     */
    public static MsgPack buildNCKPack(byte msgCw) {
        return new MsgPack(Constants.MsgMethod.NCK, msgCw, null);
    }

    /**
     * 异常应答包
     * @param data
     * @return
     */
    public static MsgPack buildNCKPack(byte msgCw, String data) {
        return new MsgPack(Constants.MsgMethod.NCK, msgCw, data);
    }
}
