package com.mbaxsrl8.solace.example.components;

import org.springframework.integration.annotation.MessagingGateway;

public interface SimpleGateway {
    void publishTextMessage(String msg);
}
