package com.redhat.training.camel.bookstore.model;

import java.util.Date;

public class FulfillmentOrder {
    private User user;
    private Book book;
    private Date orderPlacedDate;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Date getOrderPlacedDate() {
        return orderPlacedDate;
    }
    public void setOrderPlacedDate(Date orderPlacedDate) {
        this.orderPlacedDate = orderPlacedDate;
    }

    public String toString() {
        return "FulfillmentOrder:[[" + this.user.toString() + "][" + this.book.toString() + "]]";
    }
}
