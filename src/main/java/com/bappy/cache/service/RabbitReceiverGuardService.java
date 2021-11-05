package com.bappy.cache.service;

import com.bappy.cache.helper.RabbitHelper;
import com.rabbitmq.client.*;

import java.io.IOException;

public class RabbitReceiverGuardService extends Thread {
    public boolean flag = true;

    public void run() {
        while (flag) {
            Connection conn;
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUri("amqp://localhost");
                conn = factory.newConnection();
                final Channel channel = conn.createChannel();
                boolean autoAck = false;
                String queueName = RabbitHelper.QUEUE_NAME;
                channel.basicConsume(queueName, autoAck, RabbitHelper.READER_NAME, new DefaultConsumer(channel) {
                    public void handleDelivery(String consumerTag, Envelope envelope, byte[] body) {
                        try {
                            long deliveryTag = envelope.getDeliveryTag();
                            String payLoadString = new String(body);
                            channel.basicAck(deliveryTag, false);

                            RedisService.saveToRedisString(payLoadString);
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                System.out.println("Exception raised from Receiver (MQ)");
            }
        }
    }
}
