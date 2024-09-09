package com.example.demo.code;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface OrderGateway {
    @Gateway(requestChannel = "request-in-channel")
    Message<Order> placeOrder(Order order);
}
