package com.redhat.training.camel.kafka;

import java.util.Random;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaExample extends RouteBuilder {
    private Random rng = new Random();

    @Bean(name = "rng")
    public Random getRng() {
        return this.rng;
    }

    @Override
    public void configure() throws Exception {
        from("timer:kafkaMessage?period=1000")
            .routeId("kafkaProducer")
            .setHeader(KafkaConstants.KEY).method("rng", "nextInt(100)")
            .setBody().constant("Hello, Kafka!")
            .to("log:kafkaProducer?showAll=true")
            .to("kafka:hello-topic?brokers=localhost:9092");

        from("kafka:hello-topic?brokers=localhost:9092&groupId=camelConsumer&autoOffsetReset=earliest")
            .routeId("kafkaConsumer")
            .to("log:kafkaConsumer?showAll=true");
    }
}
