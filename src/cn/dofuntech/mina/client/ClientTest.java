package cn.dofuntech.mina.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import cn.dofuntech.core.util.json.Jacksons;
import cn.dofuntech.mina.protocol.Constants;
import cn.dofuntech.mina.protocol.MsgProtocol;
import cn.dofuntech.mina.protocol.domain.MsgPack;

/**
 *@function：基于mina框架的客户端，结构和mina服务端一直
 *@notice：
 */
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {
        //创建客户端连接器. 
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MsgProtocol())); //设置编码过滤器 
        connector.setHandler(new ClientHandler());//设置事件处理器 
        //        ConnectFuture cf = connector.connect(new InetSocketAddress("58.246.5.110", 8888));//建立连接 
        ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8888));//建立连接 
        cf.awaitUninterruptibly();//等待连接创建完成 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgId", "1");
        map.put("token", "RNfRiDIxYB");
        map.put("devId", "359549050951221");
        MsgPack mp = new MsgPack(Constants.MsgMethod.SEND, Constants.MsgCw.AUTHENTICATION, Jacksons.me().readAsString(map));
        cf.getSession().write(mp);//发送消息 

        Thread.sleep(10000);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("orgId", "1");
        map2.put("token", "RNfRiDIxYB");
        MsgPack mp2 = new MsgPack(Constants.MsgMethod.SEND, Constants.MsgCw.QUERY_APP, Jacksons.me().readAsString(map2));
        cf.getSession().write(mp2);//发送消息 

        //请求app列表

        //		cf.getSession().closeOnFlush();
        //		cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
        //		connector.dispose();

    }
}
