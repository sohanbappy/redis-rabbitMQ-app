package com.bappy.cache.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;

/*
channel.messageCount(queue) - will give Count value of messages of that queue
channel.queuePurge(queueName) - clear queue Data
 */
@Service
public class RabbitMQService {

    public static void sendToQueue(String data,String queueName) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicPublish("", queueName, null, data.getBytes("UTF-8"));
        }
    }


}
