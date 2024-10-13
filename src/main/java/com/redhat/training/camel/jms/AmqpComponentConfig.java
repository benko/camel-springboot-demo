package com.redhat.training.camel.jms;

import org.apache.camel.component.amqp.AMQPComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AmqpComponentConfig {
    @Bean
    public AMQPComponent amqpComponent() {
        return AMQPComponent.amqpComponent("amqp://localhost:61616", "admin", "admin");
    }
}
