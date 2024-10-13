package com.redhat.training.camel.bookstore;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import com.redhat.training.camel.bookstore.model.Book;

public class MyBookFilterPredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        Object t = exchange.getIn().getBody();

        if (!(t instanceof Book))
            return false;

        Book b = (Book)t;
        if (!b.getAuthor().equals("George R. R. Martin"))
            return true;

        return false;
    }

}
