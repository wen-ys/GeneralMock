package com.wenys.gerneralmock.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHandler {

    private static final Logger logger = LoggerFactory.getLogger("com.wenys.generalmock.log");

    public static void log(String text) {
        logger.info(text);
    }
}
