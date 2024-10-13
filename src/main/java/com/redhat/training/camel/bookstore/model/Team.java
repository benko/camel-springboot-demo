package com.redhat.training.camel.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<User> members;

    public Team() {
        members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUser(User u) {
        if (members.contains(u)) {
            throw new IllegalStateException("User already a member.");
        }
        members.add(u);
    }
    public List<User> getMembers() {
        return members;
    }
    public String toString() {
        String rv = "[" + this.name + "[";
        for (User u : members) {
            if (!rv.equals("[")) {
                rv += ",";
            }
            rv += u.getFirstName() + " " + u.getLastName();
        }
        rv += "]]";
        return rv;
    }
}
