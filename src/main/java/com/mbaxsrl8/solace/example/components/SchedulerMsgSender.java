package com.mbaxsrl8.solace.example.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SchedulerMsgSender {
    private static final Logger log = LoggerFactory.getLogger(SchedulerMsgSender.class);
    private final JmsTemplate jmsTemplate;

    @Value("testing")
    private String queueName;

    public SchedulerMsgSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostConstruct
    private void customizeJmsTemplate() {
        // Update the jmsTemplate's connection factory to cache the connection
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
        jmsTemplate.setConnectionFactory(ccf);

        // By default Spring Integration uses Queues, but if you set this to true you
        // will send to a PubSub+ topic destination
        jmsTemplate.setPubSubDomain(false);
    }

    @Scheduled(fixedRate = 5000)
    public void sendEvent() {
        String msg = "Hello World " + System.currentTimeMillis();
        log.info("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend(queueName, msg);
    }
}
