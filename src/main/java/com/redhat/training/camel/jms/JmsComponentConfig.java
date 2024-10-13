package com.redhat.training.camel.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.JMSException;

@Configuration
public class JmsComponentConfig {
    @Bean(name = "brokerA")
    public JmsComponent jmsComponent() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUser("admin");
        cf.setPassword("admin");

        JmsComponent jc = new JmsComponent();
        jc.setConnectionFactory(cf);

        return jc;
    }
}
