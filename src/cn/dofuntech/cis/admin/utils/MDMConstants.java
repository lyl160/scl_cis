package cn.dofuntech.cis.admin.utils;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月17日)
 * @version 1.0
 * filename:MDMConstants.java 
 */
public interface MDMConstants {

    interface PushCode {

        int DEVICE_BIND   = 1; //设备绑定
        int APP           = 2; //推送APP
        int CONFIG        = 3; //推送配置
        int UNINSTALL     = 4; //APP卸载推送
        int DEVICE_REGIST = 5; //注册
        int BROAD_CAST = 6; //推送广播
    }

    interface CacheKey {

        String APP_UNINSTALL = "_app_uninstall";

    }

}
