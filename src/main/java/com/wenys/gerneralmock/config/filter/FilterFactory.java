package com.wenys.gerneralmock.config.filter;

import com.wenys.gerneralmock.exceptions.UnknownFilterException;

public class FilterFactory {

    public static Filter create(String type, String args) {
        switch (type) {
            case "DelayFilter":
                return new DelayFilter(args);
            case "HeaderFilter":
                return new HeaderFilter(args);
            default:
                throw new UnknownFilterException(type);
        }
    }
}
