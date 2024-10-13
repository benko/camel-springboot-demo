package com.redhat.training.camel.bookstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private String book;

    @JsonProperty("user")
    private int userid;

    public String getBook() {
        return book;
    }
    public void setBook(String book) {
        this.book = book;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public String toString() {
        return "[Book: " + book + ", userid: " + userid + "]";
    }
}
