package cn.dofuntech.mina.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.mina.protocol.CRC32Build;
import cn.dofuntech.mina.protocol.Constants;
import cn.dofuntech.mina.protocol.domain.MsgPack;
import cn.dofuntech.mina.protocol.parse.PackParseUtil;

public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {

    private final Logger LOG = LoggerFactory.getLogger(MyKeepAliveMessageFactory.class);

    public Object getRequest(IoSession session) {
        //TODO 根据sesson 绑定的SN获取更新令牌
        MsgPack msgPack = new MsgPack(Constants.MsgMethod.HEARTBEAT, Constants.MsgCw.UPDATE_TOKEN, "1");
        LOG.debug("请求预设信息: {}", msgPack.debugInfo());
        return msgPack;
    }

    public Object getResponse(IoSession session, Object request) {
        return false;
    }

    public boolean isRequest(IoSession session, Object message) {
        return false;
    }

    public boolean isResponse(IoSession session, Object message) {
        if (message instanceof MsgPack) {
            MsgPack msgPack = (MsgPack) message;
            if (PackParseUtil.isHeartBeatResponse(msgPack) && new CRC32Build(msgPack).validate()) {
                LOG.debug("心跳包应答:{} ", msgPack.debugInfo());
                //                new DeviceLiveHandle().handleOnline(msgPack, session);
                //                //设备再次上线时，服务端推送文件时，todolist机制
                //                new TodoListHandle().TodoAppPush(msgPack, session);
                return true;
            }
        }
        return false;
    }

}
