package com.example.spring_boot_test.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class ProducerMQ {

    private static final String EXCHANGE_NAME = "logs";

    private static final String EXCHANGE_NAME_DIRECT = "direct_logs";

    private static final String EXCHANGE_NAME_TOPIC = "topic_logs";

    private static final String EXCHANGE_DELAY = "exchange_delay";

    private static final String EXCHANGE_ADAPTER = "delay_adapter";

    private static final String queue_name = "message_ttl_queue";

    /**
     * 使用fanout方式广播消息
     * @throws Exception
     */
    public static void fanoutSend() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个 exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = "This is the message from producer";

        //向指定 exchange 发送一个消息。由于是 fanout 模式， routing key 为空
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [Producer] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    /**
     * 使用direct方式发送消息
     * @throws Exception
     */
    public static void directSend() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME_DIRECT, "direct");

        String severity = "direct_key_10086";
        String message = "This is the message from Producer";

        channel.basicPublish(EXCHANGE_NAME_DIRECT, severity, null, message.getBytes());
        System.out.println(" [Producer] Sent '" + severity + "':'" + message + "'");

        channel.close();
        connection.close();
    }

    /**
     * 使用topic方式发送消息
     * @throws Exception
     */
    public static void topicSend() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME_TOPIC, "topic");

        String routingKey = "topic.key.number.default.10086";
        String message = "This is the message from Producer";

        channel.basicPublish(EXCHANGE_NAME_TOPIC, routingKey, null, message.getBytes());
        System.out.println(" [Producer] Sent '" + routingKey + "':'" + message + "'");

        connection.close();
    }

    /**
     * 使用direct方式延时发送消息
     * @throws Exception
     */
    public static void delaySend() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //先把消息放到delay_queue中，过期后自动转入exchange:amq.direct, routing-key:message_ttl_routingKey 的队列中
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "amq.direct");
        arguments.put("x-dead-letter-routing-key", "message_ttl_routingKey");
        channel.queueDeclare("delay_queue", true, false, false, arguments);

        // 声明队列
        channel.queueDeclare(queue_name, true, false, false, null);
        // 绑定路由
        channel.queueBind(queue_name, "amq.direct", "message_ttl_routingKey");

        String message = "This is the delay message" + System.currentTimeMillis();
        // 设置延时属性
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        // 持久性 non-persistent (1) or persistent (2)
        AMQP.BasicProperties properties = builder.expiration("60000").deliveryMode(2).build();
        // routingKey =delay_key 进行转发
        channel.basicPublish("", "delay_queue", properties, message.getBytes());
        System.out.println("sent message: " + message + " ,date:" + System.currentTimeMillis());
    }

    public static void main(String[] argv) throws Exception {
        //fanoutSend();
        //directSend();
        //topicSend();
        delaySend();
    }
}
