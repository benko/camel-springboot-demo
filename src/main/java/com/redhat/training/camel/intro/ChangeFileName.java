package com.redhat.training.camel.intro;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeFileName {
    private final Logger LOG = LoggerFactory.getLogger(ChangeFileName.class);

    public void changeFileName(Message in) {
        LOG.info("Checking file name...");

        String existingName = in.getHeader("CamelFileName", String.class);
        if (existingName.endsWith(".txt")) {
            LOG.info("File name already correct.");
            return;
        }
        if (existingName.indexOf(".") != -1) {
            LOG.info("File name already has suffix.");
            return;
        }

        LOG.info("Adding suffix...");
        in.setHeader("CamelFileName", existingName + ".txt");
    }

    public void logFileSize(Message in) {
        LOG.info("File has " + in.getBody().toString().length() + " characters.");
    }
}
