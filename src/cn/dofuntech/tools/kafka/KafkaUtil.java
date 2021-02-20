package cn.dofuntech.tools.kafka;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.tools.PropertyUtil;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2017 dofuntech. All Rights Reserved.</font>
 * @author lxu(@2017年5月22日)
 * @version 1.0
 * filename:KafkaUtil.java 
 */
public class KafkaUtil {

    private static Logger                        logger   = LoggerFactory.getLogger(KafkaUtil.class);

    private static KafkaProducer<String, byte[]> producer = null;

    static {
        //生产者配置文件，具体配置可参考ProducerConfig类源码，或者参考官网介绍  
        Map<String, Object> config = new HashMap<String, Object>();
        //kafka服务器地址  
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyUtil.get("metadata.broker.list"));
        //kafka消息序列化类 即将传入对象序列化为字节数组  
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        //kafka消息key序列化类 若传入key的值，则根据该key的值进行hash散列计算出在哪个partition上  
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 1024 * 5);
        //往kafka服务器提交消息间隔时间，0则立即提交不等待  
        config.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        producer = new KafkaProducer<String, byte[]>(config);
    }

    /** 
     *启动一个消费程序  
    * @param topic 要消费的topic名称 
    * @param handler 自己的处理逻辑的实现 
    * @param threadCount 消费线程数，该值应小于等于partition个数，多了也没用 
     */
    public static <T extends Serializable> void startConsumer(String topic, String groupid, final MqMessageHandler<T> handler, int threadCount) {
        if (threadCount < 1)
            throw new RuntimeException("处理消息线程数最少为1");
        //设置处理消息线程数，线程数应小于等于partition数量，若线程数大于partition数量，则多余的线程则闲置，不会进行工作  
        //key:topic名称 value:线程数  
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>." + groupid);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(threadCount));
        //消费者配置文件  
        Properties props = new Properties();
        //zookeeper地址  
        props.put("zookeeper.connect", PropertyUtil.get("zookeeper.connect"));
        //组id  
        props.put("group.id", groupid);
        props.put("auto.offset.reset", "smallest");
        props.put("enable.auto.commit", "true");
        //自动提交消费情况间隔时间  
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        //声明一个线程池，用于消费各个partition  
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        //获取对应topic的消息队列  
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        //为每一个partition分配一个线程去消费  
        for (final KafkaStream stream : streams) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
                    //有信息则消费，无信息将会阻塞  
                    while (it.hasNext()) {
                        T message = null;
                        try {
                            //将字节码反序列化成相应的对象  
                            byte[] bytes = it.next().message();
                            message = (T) bytes;
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        //调用自己的业务逻辑  
                        try {
                            handler.handle(message);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /** 
     *发送消息，发送的对象必须是可序列化的  
     */
    public static Future<RecordMetadata> send(String topic, byte[] value) {
        Future<RecordMetadata> future = null;
        try {
            //将对象序列化称字节码  
            //            byte[] bytes = SerializationUtils.serialize(value);
            future = producer.send(new ProducerRecord<String, byte[]>(topic, value));
            logger.info(">>>>>>>>kafka队列消息插入:topic =>{},data =>{}", topic, value);
//            logger.info(">>>>>>>>topic:{},offset:{}",future.get().topic(),future.get().offset());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return future;
    }

    //内部抽象类 用于实现自己的处理逻辑  
    public static abstract class MqMessageHandler<T extends Serializable> {

        public abstract void handle(T message);
    }

}
