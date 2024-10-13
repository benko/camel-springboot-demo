package com.redhat.training.camel.jms;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsExample extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:jmsSender?period=1000")
            .routeId("jmsProducer")
            .setBody().constant("Hello, world!")
            .to("log:jmsProducer?showAll=true")
            .to("brokerA:queue:HelloQueue");

        from("amqp:queue:HelloQueue")
            .routeId("amqpConsumer")
            .to("log:amqpConsumer?showAll=true");
    }

}
