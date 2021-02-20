package cn.dofuntech.mina.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.dofuntech.mina.protocol.Constants;

public class MainTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("applicationContext.xml");
        //		ApplicationContext wac = new ClassPathXmlApplicationContext("applicationContext.xml");

        byte a = Constants.MsgCw.AUTHENTICATION;
    }
}
