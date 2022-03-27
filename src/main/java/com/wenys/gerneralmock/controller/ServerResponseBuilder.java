package com.wenys.gerneralmock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class ServerResponseBuilder {

    private int responseCode = 200;

    private String responseBody = "OK";

    private final HashMap<String, String> headerValues = new HashMap<>();

    public Mono<ServerResponse> build() {
        ServerResponse.BodyBuilder srb = ServerResponse.status(HttpStatus.valueOf(responseCode));
        headerValues.forEach(srb::header);
        return srb.contentType(MediaType.APPLICATION_JSON).bodyValue(responseBody);
    }

    public ServerResponseBuilder responseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public ServerResponseBuilder responseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public ServerResponseBuilder addResponseHeader(String key, String value) {
        this.headerValues.put(key, value);
        return this;
    }

}
