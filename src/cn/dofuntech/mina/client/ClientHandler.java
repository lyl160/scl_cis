package cn.dofuntech.mina.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.mina.protocol.CRC32Build;
import cn.dofuntech.mina.protocol.Constants;
import cn.dofuntech.mina.protocol.domain.MsgPack;
import cn.dofuntech.mina.protocol.parse.PackParseUtil;

public class ClientHandler extends IoHandlerAdapter {

    private final Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        LOG.debug("客户端收到消息：" + message);
        if (message instanceof MsgPack) { //TODO 判断是否属于心跳包
            MsgPack msgPack = (MsgPack) message;
            if (new CRC32Build(msgPack).validate()) {
                if (PackParseUtil.isHeartBeat(msgPack)) {
                    LOG.debug("收到心跳包:{}", msgPack.debugInfo());
                    session.write(msgPack);
                }
                else {
                    LOG.debug("收到响应包:{}", msgPack.debugInfo());
                }
            }
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // TODO Auto-generated method stub
        super.messageSent(session, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionClosed(session);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionCreated(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // TODO Auto-generated method stub
        super.sessionIdle(session, status);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        super.sessionOpened(session);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgId", "1");
        map.put("token", "IBJbuxAwtp");
        map.put("devId", "359549050951220");
        MsgPack mp = new MsgPack(Constants.MsgMethod.SEND, Constants.MsgCw.AUTHENTICATION, Jacksons.me().readAsString(map));
        System.out.println(mp.debugInfo());

        //        [msgHead=126,msgLength=68,version=10,msgMethod=2,msgCw=32,data={"devId":"359549050951220","orgId":"1","token":"IBJbuxAwtp"},authCode=1044049636
        //        msgHead=126,msgLength=68,version=10,msgMethod=2,msgCw=32,data={"devId":"359549050951220","orgId":"1","token":"IBJbuxAwtp"},authCode=-891217771]
    }
}
