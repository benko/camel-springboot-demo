package com.redhat.training.camel.database;

import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MyXaPropagationPolicy {
    @Bean(name = "myRequiresNewPolicy")
    public SpringTransactionPolicy requiresNew(PlatformTransactionManager ptm) {
        SpringTransactionPolicy stp = new SpringTransactionPolicy();
        stp.setTransactionManager(ptm);
        stp.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        return stp;
    }
}
