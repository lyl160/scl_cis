package cn.dofuntech.mina.server;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.mina.process.AbstractHandle;
import cn.dofuntech.mina.process.ProcessFactory;
import cn.dofuntech.mina.process.domain.HandleResult;
import cn.dofuntech.mina.protocol.CRC32Build;
import cn.dofuntech.mina.protocol.domain.MsgPack;
import cn.dofuntech.mina.protocol.parse.PackParseUtil;

public class MyHandler extends IoHandlerAdapter {

    private final int                                  IDLE                      = 5;                                                    //单位秒

    public static final String                         DVID_ATTR                 = "devId";                                              //单位秒

    private final Logger                               LOG                       = LoggerFactory.getLogger(MyHandler.class);

    public static Set<IoSession>                       sessions                  = Collections.synchronizedSet(new HashSet<IoSession>());

    public static ConcurrentHashMap<String, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<String, IoSession>();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        //        session.closeOnFlush();
        LOG.error(cause.getMessage(), cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String ip = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
        LOG.debug("客户端" + ip + "连接成功！");
        session.setAttribute("ip", ip);
        if (message instanceof MsgPack) {
            MsgPack msgPack = (MsgPack) message;
            LOG.debug("服务器收到消息：" + msgPack.debugInfo());
            //数据合法性校验
            if (new CRC32Build(msgPack).validate()) {
                AbstractHandle handle = ProcessFactory.getHandle(msgPack);
                HandleResult result = handle.doHandle(msgPack, session);
                if (PackParseUtil.isAuthRequset(msgPack)) {
                    String dvId = (String) result.get(DVID_ATTR);
                    session.setAttribute(DVID_ATTR, dvId);
                    sessionsConcurrentHashMap.put(dvId, session);
                }
            }
            else {
                LOG.debug("数据不合法");
            }
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.debug("messageSent:" + message);

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LOG.debug("remote client [" + session.getRemoteAddress().toString() + "] connected.");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOG.debug("sessionClosed.");
        String devId = (String) session.getAttribute(DVID_ATTR);
        if (StringUtils.isNotBlank(devId)) {
            sessionsConcurrentHashMap.remove(session.getAttribute(DVID_ATTR));
        }
        sessions.remove(session);
        session.closeOnFlush();
        //设备离线
//        new DeviceLiveHandle().handleOffline(devId);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        LOG.debug("session idle");
        //		session.closeOnFlush();
        //		LOG.warn("disconnected.");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOG.debug("sessionOpened.");
        sessions.add(session);
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE);
    }

}
