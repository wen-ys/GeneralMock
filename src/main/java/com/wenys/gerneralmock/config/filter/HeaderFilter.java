package com.wenys.gerneralmock.config.filter;

import com.wenys.gerneralmock.controller.ServerResponseBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

public class HeaderFilter extends Filter {

    private final Map<String, String> headerMap;

    HeaderFilter(String args) {
        headerMap = parseArgs(args);
    }

    @Override
    public <T> Mono<T> doFilter(Mono<T> response) {
        return response.flatMap(res -> {
            if (res instanceof ServerResponseBuilder) {
                ServerResponseBuilder responseBuilder = (ServerResponseBuilder) res;
                headerMap.forEach(responseBuilder::addResponseHeader);
                return Mono.just(res);
            }
            return Mono.error(new IllegalAccessException(String.format("No %s type is in this stream", ServerResponseBuilder.class)));
        });
    }
}
