package cn.dofuntech.cis.api.util;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class ActivemqService {
	protected Logger         logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name="jmsTemplate")
    private JmsTemplate jmsTemplate;
    
    /**
     * 向默认队列发送消息
     */
      public Boolean sendMessage(final String msg) {
    	Boolean flag = false;
    	try {
    		String destination =  jmsTemplate.getDefaultDestination().toString();
            logger.debug("向队列" +destination+ "发送了消息------------" + msg);
            jmsTemplate.send(new MessageCreator() {
              public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
              }
            });
            flag = true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
        return flag;
      }
      
      /**
       * 向指定队列发送消息
       */
      public void sendMessage(Destination destination, final String msg) {
        logger.debug("向队列" + destination.toString() + "发送了消息------------" + msg);
        jmsTemplate.send(destination, new MessageCreator() {
          public Message createMessage(Session session) throws JMSException {
            return session.createTextMessage(msg);
          }
        });
      }
}
