package com.redhat.training.camel.bookstore;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.redhat.training.camel.bookstore.model.Book;
import com.redhat.training.camel.bookstore.model.FulfillmentOrder;
import com.redhat.training.camel.bookstore.model.User;
import com.redhat.training.camel.bookstore.registry.BookRegistry;
import com.redhat.training.camel.bookstore.registry.UserRegistry;

public class FulfillmentEnrichment {
    private final Logger LOG = LoggerFactory.getLogger(FulfillmentEnrichment.class.getName());

    @Autowired
    @Qualifier("userCatalog")
    UserRegistry ureg;

    @Autowired
    @Qualifier("bookCatalog")
    BookRegistry breg;

    public void enrichUser(Message m) {
        FulfillmentOrder fo = m.getBody(FulfillmentOrder.class);
        if (fo.getUser() == null || fo.getUser().getId() == 0) {
            throw new IllegalArgumentException("FulfillmentOrder has no user (or user ID).");
        }
        User u = ureg.get(fo.getUser().getId());
        LOG.info("Enriching user: " + fo.getUser().getId() + " -> " + u);
        fo.setUser(u);
        m.setBody(fo);
        return;
    }

    public FulfillmentOrder enrichBook(FulfillmentOrder fo) {
        if (fo.getBook() == null || fo.getBook().getTitle() == null) {
            throw new IllegalArgumentException("FulfillmentOrder has no book (or book title).");
        }
        Book b = breg.get(fo.getBook().getTitle());
        LOG.info("Enriching book: " + fo.getBook().getTitle() + " -> " + b);
        fo.setBook(b);
        return fo;
    }
}
