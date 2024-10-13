package com.redhat.training.camel.intro;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileNameProcessor implements Processor {
    private final Logger LOG = LoggerFactory.getLogger(FileNameProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {
        String currentFileName = (String)exchange.getIn().getHeader("CamelFileName");

        if (currentFileName.endsWith(".txt")) {
            LOG.info("File name suffix already correct.");
            return;
        }

        if (currentFileName.indexOf(".") != -1) {
            LOG.info("File name already has suffix.");
            return;
        }

        // quick-and-dirty way
        // exchange.getIn().setHeader("CamelFileName", currentFileName + ".txt");

        // take the "in" message and put it into the out part of the exchange
        exchange.setMessage(exchange.getIn().copy());
        exchange.getMessage().setHeader("CamelFileName", currentFileName + ".txt");
    }

}
