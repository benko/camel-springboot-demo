package com.redhat.training.camel.bookstore.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.training.camel.bookstore.model.Book;

public class BookRegistry extends Registry<String, Book> {
    private final Logger LOG = LoggerFactory.getLogger(BookRegistry.class.getName());

    @Override
    public Book get(String key) {
        if (!this.has(key)) {
            throw new BookNotFoundException("Book not in registry: " + key);
        }
        LOG.info("Returning book: " + key);
        return this.registry.get(key);
    }

    @Override
    public void simpleAdd(Book b) {
        LOG.info("Adding book: " + b);
        this.add(b.getTitle(), b);
    }
}
