package com.mbaxsrl8.solace.example.components;

import jakarta.jms.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.qpid.jms.JmsConnectionFactory;
public class SolaceQpidUtils {
    private static final Logger log = LogManager.getLogger(SolaceQpidUtils.class);
    private static final String HOST = "amqp://localhost:5677";
    private static final String USERNAME = "xiaoming";
    private static final String PASSWORD = "123456";
    private static final String QUEUE_NAME = "tutorial";

    public static void main(String... args) {
        ConnectionFactory connectionFactory = new JmsConnectionFactory(USERNAME, PASSWORD, HOST);
        try (JMSContext context = connectionFactory.createContext()) {
            log.info("Connected with username '{}'.", USERNAME);
            Queue queue = context.createQueue(QUEUE_NAME);
            context.createProducer().setDeliveryMode(DeliveryMode.PERSISTENT).send(queue, "Hello world Queues!");

            log.info("Awaiting message...");
            Message message = context.createConsumer(queue).receive();

            // process received message
            if (message instanceof TextMessage) {
                log.info("TextMessage received: '{}'", ((TextMessage) message).getText());
            } else {
                log.info("Message received.");
            }
            log.info("Message Content:{}", message.toString());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
