package com.mbaxsrl8.solace.example.components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHandler;



@Configuration
public class Configurations {
//    @Bean
//    @ServiceActivator(inputChannel = "exampleChannel")
//    public MessageHandler jmsOut(ConnectionFactory connectionFactory) {
//        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(new JmsTemplate(connectionFactory));
//        handler.setDestinationName("outQueue");
//        return handler;
//    }
}
