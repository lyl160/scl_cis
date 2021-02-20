package cn.dofuntech.mina.server;

import java.util.Iterator;

import org.apache.mina.core.session.IoSession;

/**
 * <p>
 * 模拟群发，在服务端和客户端都启动之后，可以运行此方法群发消息
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月14日)
 * @version 1.0
 * filename:SendToAllTest.java 
 */
public class SendToAllTest {

    public static void main(String[] args) {
        System.out.println(MyHandler.sessions.size());
        for (Iterator iterator = MyHandler.sessions.iterator() ; iterator.hasNext() ;) {
            IoSession session = (IoSession) iterator.next();
            session.write("发送系统消息");
        }
    }
}
