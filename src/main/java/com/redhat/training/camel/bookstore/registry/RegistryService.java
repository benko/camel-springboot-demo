package com.redhat.training.camel.bookstore.registry;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class RegistryService {
    UserRegistry ureg;
    BookRegistry breg;

    public RegistryService() {
        this.ureg = new UserRegistry();
        this.breg = new BookRegistry();
    }

    @Bean(name = "userCatalog")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public UserRegistry getUserRegistry() {
        return this.ureg;
    }

    @Bean(name = "bookCatalog")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BookRegistry getBookRegistry() {
        return this.breg;
    }
}
