package com.redhat.training.camel.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HelloService extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Reset Camel's default thread pool profile.
        // N.B.: need to use the inherited getContext() as injection will not have happened here yet
        ThreadPoolProfile tpp = getContext().getExecutorServiceManager().getDefaultThreadPoolProfile();
        tpp.setPoolSize(10);
        tpp.setMaxPoolSize(100);
        tpp.setMaxQueueSize(50);

        // Use a custom thread pool profile.
        // N.B.: need to use the inherited getContext() as injection will not have happened here yet
        ThreadPoolProfileBuilder tppb = new ThreadPoolProfileBuilder("restPool");
        ThreadPoolProfile restTpp = tppb.poolSize(5)
                                        .maxPoolSize(100)
                                        .maxQueueSize(50)
                                        .build();
        getContext().getExecutorServiceManager().registerThreadPoolProfile(restTpp);

        restConfiguration()
            .component("servlet")
            .componentProperty("executorRef", "restPool")
            .port(8080);

        rest("/hello")
            .get()
                .to("direct:sayHello")
            .get("/{name}")
                .to("direct:sayHello")
            .post("/new")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.TEXT_PLAIN_VALUE)
                .to("direct:getHello");

        from("rest:get:/goodbye:/{name}")
            .transform().simple("Goodbye, ${header.name}");

        from("direct:sayHello")
            // .threads(1, 100) <-- makes no sense here, rest component decides parallelism
            .to("log:sayHello?showAll=true")
            .setBody().constant("Hello, world!");

        from("direct:getHello")
            .unmarshal(new JacksonDataFormat(Person.class))
            .to("log:getHello?showAll=true")
            .setBody().simple("Hello, ${body.name}!");
    }

}
