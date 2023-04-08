package com.mbaxsrl8.solace.example.components;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;

@Slf4j
public class SolaceUtils {
    private static final String HOST = "localhost:55554";
    private static final String VPN = "tutorial";
    private static final String USERNAME = "xiaoming";
    private static final String PASSWORD = "123456";
    private static final String QUEUE = "testing";

    public static Connection getConnection() throws Exception {
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(HOST);
        connectionFactory.setVPN(VPN);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);

        return connectionFactory.createConnection();
    }

    public static void sendMsg(Session session) throws JMSException {
        Queue queue = session.createQueue(QUEUE);
        MessageProducer messageProducer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello world Queues!");
        messageProducer.send(queue, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
        log.info("Sending message '{}' to queue '{}'...%n", message.getText(), queue.toString());
    }

    public static MessageConsumer getConsumer(Session session, CountDownLatch latch) throws JMSException {
        Queue queue = session.createQueue(QUEUE);
        MessageConsumer messageConsumer = session.createConsumer(queue);

        // Use the anonymous inner class for receiving messages asynchronously
        messageConsumer.setMessageListener(message -> {
            try {
                if (message instanceof TextMessage) {
                    log.info("TextMessage received: '{}'%n", ((TextMessage) message).getText());
                } else {
                    log.info("Message received.");
                }
                log.info("Message Content:%n{}%n", SolJmsUtility.dumpMessage(message));

                // ACK the received message manually because of the set SupportedProperty.SOL_CLIENT_ACKNOWLEDGE above
                message.acknowledge();
                latch.countDown();

            } catch (JMSException ex) {
                log.error("Error processing incoming message.", ex);
            }
        });
        return messageConsumer;
    }

    public static void main(String... args) throws Exception {
        Connection connection = getConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final CountDownLatch latch = new CountDownLatch(1);
        MessageConsumer consumer = getConsumer(session, latch);

        connection.start();
        log.info("Awaiting message...");
        // the main thread blocks at the next statement until a message received
        latch.await();

        connection.stop();
        // Close everything in the order reversed from the opening order
        // NOTE: as the interfaces below extend AutoCloseable,
        // with them it's possible to use the "try-with-resources" Java statement
        // see details at https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        consumer.close();
        session.close();
        connection.close();
    }
}
