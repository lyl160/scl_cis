package cn.dofuntech.mina.process.handle;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.mina.core.session.IoSession;

import cn.dofuntech.core.cache.CacheManager;
import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.core.util.spring.SpringContextUtil;
import cn.dofuntech.mina.process.AbstractHandle;
import cn.dofuntech.mina.process.domain.HandleResult;
import cn.dofuntech.mina.protocol.Constants;
import cn.dofuntech.mina.protocol.domain.MsgPack;
import cn.dofuntech.mina.protocol.parse.PackParseUtil;
import cn.dofuntech.mina.server.MyHandler;

/**
 * <p>
 * 应答包处理
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:ResponseHandle.java 
 */
public class ResponseHandle extends AbstractHandle {

    @Override
    public boolean canDo(MsgPack msgPack) {
        return PackParseUtil.isResonpse(msgPack);
    }

    @Override
    public HandleResult handle(MsgPack msgPack, IoSession session) {
        byte msg = msgPack.getMsgCw();
        Map<String, Object> map = Jacksons.me().json2Map(msgPack.getData());
        long orgId = MapUtils.getLongValue(map, "orgId");
        String token = MapUtils.getString(map, "token");
        HandleResult result = null;
        String dvId = (String) session.getAttribute(MyHandler.DVID_ATTR);
        CacheManager cacheManager = SpringContextUtil.getBean(CacheManager.class);
        switch (msg) {
            case Constants.MsgCw.DEVICE_APP_UNIINSTALL:
                logger.debug(">>>>>>解析app卸载应答包开始");
                Long busiId = MapUtils.getLongValue(map, "busiId");
                System.out.println(">>>>>>>>>>>>>>>busiId:" + busiId);
                cacheManager.set("DEVICE_UNINSTALL_" + dvId, 1);
                result = new HandleResult(msgPack);
                logger.debug(">>>>>>解析app卸载应答包结束");
                break;
            case Constants.MsgCw.DEVICE_BIND_REQUEST:
                logger.debug(">>>>>>解析设备注册应答包开始");
                cacheManager.set("DEVICE_BIND_REQUEST_" + dvId, 1);
                logger.debug(">>>>>>解析设备注册应答结束");
                break;
            default:
                break;
        }
        return result;
    }
}
