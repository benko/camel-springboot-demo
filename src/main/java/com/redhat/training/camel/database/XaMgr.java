package com.redhat.training.camel.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class XaMgr {
    @Bean
    public PlatformTransactionManager getXaMgr(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    // public PlatformTransactionManager getGlobalXaMgr(Object... objs) {
        // configure jta mgr with the implementation instance
        // return new JtaTransactionManager(impl);
    // }
}
