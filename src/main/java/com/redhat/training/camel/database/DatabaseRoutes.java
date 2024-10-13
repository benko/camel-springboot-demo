package com.redhat.training.camel.database;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.stereotype.Component;

import com.redhat.training.camel.database.RandomBean.RandomException;

@Component
public class DatabaseRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:singleRead?repeatCount=1")
            .routeId("jdbc-component")
            .setHeader("stuffid", constant(1))
            .setBody().simple("SELECT * FROM randomstuff WHERE id = ${header.stuffid}")
            .to("jdbc:mydb")
            .to("log:jdbcComponent?showAll=true");

        from("sql:SELECT * FROM randomstuff?dataSource=#mydb&repeatCount=1")
            .routeId("sql-component-no-params")
            .to("log:sqlComponentNoParam?showAll=true");

        from("timer:singleRead?repeatCount=1")
            .routeId("sql-component-with-param")
            .setHeader("stuffid", constant(3))
            .to("sql:SELECT * FROM randomstuff WHERE id = :#stuffid?dataSource=#mydb&repeatCount=1")
            .to("log:sqlComponentParam?showAll=true");

        from("jpa:com.redhat.training.camel.database.Item?persistenceUnit=jpaRoute&consumeDelete=false")
            .idempotentConsumer(simple("${body.id}"),
                                MemoryIdempotentRepository.memoryIdempotentRepository(1000))
            .routeId("jpa-component-consumer")
            .to("log:jpaComponentConsumer?showAll=true");

        onException(RandomException.class)
            .to("log:randomExceptionHandler?showAll=true")
            .handled(true)
            .markRollbackOnly();

        from("timer:jpaStore?repeatCount=1")
            .routeId("jpa-component-producer")
            .transacted("myRequiresNewPolicy")
            .setBody().constant(new Item("ladder"))
            .to("log:jpaComponentProducerPre?showAll=true")
            .to("jpa:com.redhat.training.database.Item?persistenceUnit=jpaRoute")
            .to("log:jpaComponentProducerPost?showAll=true")
            .bean(RandomBean.class)
            .to("log:jpaAfterBean?showAll=true");
    }

}
