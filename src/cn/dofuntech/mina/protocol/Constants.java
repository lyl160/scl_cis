package cn.dofuntech.mina.protocol;

import java.nio.charset.Charset;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:Constants.java 
 */
public interface Constants {

    Charset              charset    = Charset.forName("UTF-8");

    public static double TRADE_RATE = 0.02;

    //包类型
    interface MsgMethod {

        byte HEARTBEAT = 0x1; //心跳包
        byte SEND      = 0x2; //请求包
        byte ACK       = 0x3; //正常应答包
        byte NCK       = 0x4; //异常应答包
        byte INFO      = 0x5; //信息包
    }

    //命令字
    interface MsgCw {

        byte UPDATE_TOKEN          = 0x01; //更新令牌
        byte AUTHENTICATION        = 0x20; //鉴权
        byte QUERY_APP             = 0x21; //查询APP
        byte DEVICE_BIND           = 0x51; //设备绑定
        byte DEVICE_APP_UNIINSTALL = 0x57; //卸载APP
        byte PUSH_APP              = 0x52; //推送软件
        byte PUSH_WIFI             = 0x53; //推送热点
        byte PUSH_SCREEN           = 0x54; //推送屏幕亮度
        byte PUSH_CONFIG           = 0x55; //推送配置
        byte PUSH_BROADCASE        = 0x56; //推送广播
        byte DEVICE_BIND_REQUEST   = 0x58; //设备绑定

        byte APP_SETUP             = 0x60; //app安装情况
        byte APP_INFO              = 0x61; //app信息包
        byte QUERY_APP_INFO        = 0x62; //查询AppInfo
    }

}
