package com.redhat.training.camel.intro;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("DetectFileType")
public class DetectFileType {
    private final Logger LOG = LoggerFactory.getLogger(DetectFileType.class);

    // set the "size" message header containing one of "small", "medium", "large", or "xlarge"
    public void getFileSize(Message m) {
        LOG.info("Classifying file by size...");
        int fs = m.getHeader("CamelFileLength", Integer.class).intValue();
        if (fs < 10000) {
            LOG.info("Size is small.");
            m.setHeader("size", "small");
        } else if (fs >= 10000 && fs < 50000) {
            LOG.info("Size is medium.");
            m.setHeader("size", "medium");
        } else if (fs >= 50000 && fs < 100000) {
            LOG.info("Size is large.");
            m.setHeader("size", "large");
        } else if (fs >= 100000) {
            LOG.info("Size is xlarge.");
            m.setHeader("size", "xlarge");
        }
    }

    // set the "format" message header containing one of "html", "pdf", "txt", or "unknown"
    public void getFileFormat(Message m) {
        LOG.info("Classifying file by type...");
        String fn = m.getHeader("CamelFileName", String.class);
        if (fn.endsWith(".html")) {
            LOG.info("Format is html.");
            m.setHeader("format", "html");
        } else if (fn.endsWith(".pdf")) {
            LOG.info("Format is pdf.");
            m.setHeader("format", "pdf");
        } else if (fn.endsWith(".txt")) {
            LOG.info("Format is txt.");
            m.setHeader("format", "txt");
        } else {
            LOG.info("Format is unknown.");
            m.setHeader("format", "unknown");
        }
    }
}
