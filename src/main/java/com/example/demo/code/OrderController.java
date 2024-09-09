package com.example.demo.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    public final OrderGateway orderGateway;

    @PostMapping("/placeorder")
    public Order placeOrder(@RequestBody Order order) {
        log.info("Place order in OrderController");

        Message<Order> replyObj = orderGateway.placeOrder(order);
        Order responseObj = replyObj.getPayload();
        return responseObj;
    }
}
