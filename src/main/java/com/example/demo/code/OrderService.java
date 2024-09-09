package com.example.demo.code;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.integration.annotation.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    // 1. Transformacja zamówienia (dodanie rabatu jeśli kwota > 100)
    @Transformer(inputChannel = "request-in-channel", outputChannel = "transformed-order-channel")
    public Message<Order> transformOrder(Message<Order> orderMessage) {
        log.info("Transforming Order in OrderService : transformOrder method");

        Order order = orderMessage.getPayload();
        if (order.getAmount() > 100) {
            log.info("Applying discount for order over 100");
            order.setAmount(order.getAmount() * 0.9);  // 10% rabat
        }

        return orderMessage;
    }

    // 2. Filtr zamówień (przepuszcza zamówienia tylko jeśli kwota > 50)
    @Filter(inputChannel = "transformed-order-channel", outputChannel = "filtered-orders-channel")
    public boolean filterOrders(Order order) {
        log.info("Filtering order with amount: " + order.getAmount());

        // Przepuszcza zamówienia tylko jeśli kwota jest większa niż 50
        return order.getAmount() > 50;
    }

    // 3. Splitter - rozdzielenie zamówienia na poszczególne produkty
    @Splitter(inputChannel = "filtered-orders-channel", outputChannel = "split-orders-channel")
    public List<String> splitOrder(Order order) {
        log.info("Splitting order into individual products in OrderService : splitOrder method");
        return order.getProductList();  // Rozdziela produkty na pojedyncze wiadomości
    }

    // 4. Agregator - agregowanie produktów w jedną wiadomość
    @Aggregator(inputChannel = "split-orders-channel", outputChannel = "aggregated-orders-channel")
    public Order aggregateProducts(List<String> products) {
        log.info("Aggregating products into a single order");

        Order order = new Order();
        order.setProductList(products);
        return order;
    }

    // 5. Przetwarzanie zamówienia
    @ServiceActivator(inputChannel = "aggregated-orders-channel", outputChannel = "order-reply-channel")
    public Message<Order> processOrder(Message<Order> orderMessage) {
        log.info("Processing order in OrderService : processOrder method");

        orderMessage.getPayload().setOrderStatus("success");
        return orderMessage;
    }

    // 6. Wysłanie odpowiedzi na zamówienie
    @ServiceActivator(inputChannel = "order-reply-channel")
    public void replyOrder(Message<Order> orderMessage) {
        log.info("Replying to order in OrderService : replyOrder method");

        MessageChannel replyChannel = (MessageChannel) orderMessage.getHeaders().getReplyChannel();
        replyChannel.send(orderMessage);
    }
}
