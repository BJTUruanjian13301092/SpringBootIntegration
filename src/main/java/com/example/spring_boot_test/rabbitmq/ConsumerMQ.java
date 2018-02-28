package com.example.spring_boot_test.rabbitmq;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsumerMQ {

    private static final String EXCHANGE_NAME = "logs";

    private static final String EXCHANGE_NAME_DIRECT = "direct_logs";

    private static final String EXCHANGE_NAME_TOPIC = "topic_logs";

    private static final String EXCHANGE_ADAPTER = "delay_adapter";

    private static final String EXCHANGE_DELAY = "exchange_delay";

    private static final String queue_name = "message_ttl_queue";

    /**
     * 使用fanout方式接收广播消息
     * @throws Exception
     */
    public static void fanoutGet() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明 exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 生命一个队列，名字随机
        String queueName = channel.queueDeclare().getQueue();
        //将队列绑定到 exchange 上
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [Consumer] Waiting for messages. To exit press CTRL+C");

        //consumer 简单的将接收到的消息打印出来
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * 使用direct方式接收消息
     * @throws Exception
     */
    public static void directGet() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME_DIRECT, "direct");
        String queueName = channel.queueDeclare().getQueue();

        String severity = "direct_key_10086";
        channel.queueBind(queueName, EXCHANGE_NAME_DIRECT, severity);

        System.out.println(" [Consumer] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * 使用topic方式接收消息
     * @throws Exception
     */
    public static void topicGet() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME_TOPIC, "topic");
        String queueName = channel.queueDeclare().getQueue();

        //topic.key.number.default.10086
        String bindingKey = "*.key.number.#";
        channel.queueBind(queueName, EXCHANGE_NAME_TOPIC, bindingKey);

        System.out.println(" [Consumer] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * 使用direct方式延时接收消息
     * @throws Exception
     */
    public static void delayGet() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange", "amq.direct");
        arguments.put("x-dead-letter-routing-key", "message_ttl_routingKey");
        channel.queueDeclare("delay_queue", true, false, false, arguments);

        // 声明队列
        channel.queueDeclare(queue_name, true, false, false, null);
        // 绑定路由
        channel.queueBind(queue_name, "amq.direct", "message_ttl_routingKey");

        System.out.println(" [Consumer] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [Consumer] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queue_name, true, consumer);
    }

    public static void main(String[] argv) throws Exception {
        //fanoutGet();
        //directGet();
        //topicGet();
        delayGet();
    }
}
