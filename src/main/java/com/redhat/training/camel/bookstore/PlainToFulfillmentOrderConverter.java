package com.redhat.training.camel.bookstore;

import org.apache.camel.Converter;

import com.redhat.training.camel.bookstore.model.Book;
import com.redhat.training.camel.bookstore.model.FulfillmentOrder;
import com.redhat.training.camel.bookstore.model.Order;
import com.redhat.training.camel.bookstore.model.User;

@Converter
public class PlainToFulfillmentOrderConverter {
    @Converter
    public static FulfillmentOrder lalala(Order o) {
        FulfillmentOrder fo = new FulfillmentOrder();
        User u = new User();
        u.setId(o.getUserid());
        Book b = new Book();
        b.setTitle(o.getBook());

        fo.setUser(u);
        fo.setBook(b);

        return fo;
    }
}
