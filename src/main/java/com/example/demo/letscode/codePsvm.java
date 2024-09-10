package com.example.demo.letscode;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Configuration
public class codePsvm {
    public static void main(String[] args) {

    }

    @Bean
    MessageChannel greetings() {
        return MessageChannels.direct().getObject();
    }

//    @Bean
//    ApplicationRunner runner(){
//        return args -> {
//            for (var i = 0; i < 10; i++)
//                greetings().send(MessageBuilder.withPayload(text()).build());
//        };
//    }
    @Component
    static class MyMessageSOurce implements MessageSource<String> {
        //poller
        @Override
        public Message<String> receive() {
            return MessageBuilder.withPayload(text()).build();
        }
    }

    @Bean
    IntegrationFlow flow(MyMessageSOurce myMessageSOurce) {
        return IntegrationFlow
//                .from((MessageSource<String>) () -> MessageBuilder.withPayload("Hello").build(), poller -> poller.poller(pm -> pm.fixedRate(1000))).channel(greetings())
                .from(myMessageSOurce, p -> p.poller(
                        pollerFactory -> pollerFactory.fixedRate(1)))
                .filter(String.class, source -> source.contains("NO"))
                .transform((GenericTransformer<String, String>) source-> source.toUpperCase())
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println("The payload is: " + payload);
                    return null;
                })
                .get();
    }

    @Bean
    IntegrationFlow flow1() {
        return IntegrationFlow.from(greetings())
                .handle((GenericHandler<String>) (payload, headers) -> {
                    System.out.println("The payload is: " + payload);
                    return null;
                }).get();
    }

    private static String text() {
        return Math.random() > 0.5 ?
                "HEEEELO" :
                "NO HELLO";
    }
}
