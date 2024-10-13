package com.redhat.training.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redhat.training.camel.bookstore.BookstoreRoutes;
import com.redhat.training.camel.database.DatabaseRoutes;
import com.redhat.training.camel.intro.CamelHello;
import com.redhat.training.camel.jms.JmsExample;
import com.redhat.training.camel.kafka.KafkaExample;
import com.redhat.training.camel.rest.HelloService;
import com.redhat.training.camel.rest.HttpClient;
import com.redhat.training.camel.rest.RestClient;

@Configuration
public class CamelContextConfig {
    private final Logger LOG = LoggerFactory.getLogger(CamelContextConfig.class.getName());

    // start with...
    //  mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dcamel.problem=FOO"
    @Value("${camel.problem:}")
    private String problem;

    // this is invoked by CamelContextConfiguration below, no need to annotate it
    RoutesBuilder giveMeRoutes() {
        LOG.info("Checking which problem to load (" + problem + ")...");

        if (problem == null || problem.isEmpty()) {
            System.err.println("""
                    ERROR: No problem specified. Must set camel.problem system property to one of:
                     - intro
                     - database
                     - jms
                     - kafka
                     - rest-service
                     - rest-client
                     - http-client
                     - health
                     - bookstore
                    """);
            System.exit(1);
        }

        switch (problem) {
            case "intro":
                LOG.info("Loading CamelHello...");
                return new CamelHello();
            case "database":
                LOG.info("Loading DatabaseRoutes...");
                return new DatabaseRoutes();
            case "jms":
                LOG.info("Loading JmsExample...");
                return new JmsExample();
            case "kafka":
                LOG.info("Loading KafkaExample...");
                return new KafkaExample();
            case "rest-service":
                LOG.info("Loading HelloService...");
                return new HelloService();
            case "rest-client":
                LOG.info("Loading RestClient...");
                return new RestClient();
            case "http-client":
                LOG.info("Loading HttpClient...");
                return new HttpClient();
            case "bookstore":
                LOG.info("Loading BookstoreRoutes...");
                return new BookstoreRoutes();
        }

        LOG.info("No recognisable problem specified.");
        return null;
    }

    @Bean
    CamelContextConfiguration giveMeCamelContext() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext ctx) {
                try {
                    ctx.addRoutes(giveMeRoutes());
                } catch (Exception e) {
                    LOG.info("Exception in CamelContextConfiguration.beforeApplicationStart()", e);
                }
                ctx.start();
            }
            public void afterApplicationStart(CamelContext ctx) {
                //
            }
        };
    }
}
