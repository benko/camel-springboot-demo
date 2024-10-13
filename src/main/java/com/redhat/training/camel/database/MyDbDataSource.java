package com.redhat.training.camel.database;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyDbDataSource {
    @Bean(name = "mydb")
    public DataSource getDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL("jdbc:postgresql://localhost:5432/items");
        ds.setUser("developer");
        ds.setPassword("developer");
        return ds;
    }
}
