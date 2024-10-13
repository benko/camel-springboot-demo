package com.redhat.training.camel.database;

import org.apache.camel.Exchange;

public class RandomBean {
    public void doRandomStuff(Exchange e) throws RandomException {
        throw new RandomException();
    }

    class RandomException extends Exception {
    }
}
