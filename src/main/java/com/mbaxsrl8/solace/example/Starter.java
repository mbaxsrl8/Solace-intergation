package com.mbaxsrl8.solace.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
public class Starter {
    private static final Logger log = LoggerFactory.getLogger(Starter.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(Starter.class)
                        .run(args);

        DirectChannel channel = (DirectChannel) context.getBean("exampleChannel");
        channel.send(new GenericMessage<>("Hello"));
        log.info("Sent msg to Solace");
    }
}
