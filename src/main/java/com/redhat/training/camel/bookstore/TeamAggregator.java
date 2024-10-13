package com.redhat.training.camel.bookstore;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import com.redhat.training.camel.bookstore.model.Team;
import com.redhat.training.camel.bookstore.model.User;

public class TeamAggregator implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Team t;
        if (oldExchange == null) {
            t = new Team();
            t.setName(newExchange.getMessage().getBody(User.class).getDepartment());
        } else {
            t = oldExchange.getMessage().getBody(Team.class);
        }

        t.addUser(newExchange.getMessage().getBody(User.class));

        newExchange.getMessage().setBody(t);

        return newExchange;
    }

}
