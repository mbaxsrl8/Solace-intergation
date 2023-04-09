package com.mbaxsrl8.solace.example.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;


@Configuration
@EnableScheduling
public class Configurations {

    private static final Logger log = LoggerFactory.getLogger(Configurations.class);

    @Value("testing")
    private String queueName;

    @Autowired
    private JmsTemplate jmsTemplate;

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
    public void sendEvent() throws Exception {
        String msg = "Hello World " + System.currentTimeMillis();
        log.info("==========SENDING MESSAGE========== " + msg);
        jmsTemplate.convertAndSend(queueName, msg);
    }

}
