package com.redhat.training.camel.bookstore.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redhat.training.camel.bookstore.model.User;

@Component
public class UserRegistry extends Registry<Integer, User> {
    private final Logger LOG = LoggerFactory.getLogger(UserRegistry.class.getName());

    @Override
    public User get(Integer key) {
        if (!this.has(key)) {
            throw new UserNotFoundException("User not in registry: " + key);
        }
        LOG.info("Returning user: " + key);
        return this.registry.get(key);
    }

    @Override
    public void simpleAdd(User u) {
        LOG.info("Adding user: " + u);
        this.add(u.getId(), u);
    }
}
