package com.dgh.basicmock.controller;

import com.dgh.basicmock.config.route.info.LocalRoutingInfoLoader;
import com.dgh.basicmock.config.route.info.RoutingInfoLoader;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MockOperation {

    private final LocalRoutingInfoLoader localRoutingInfoLoader;

    public MockOperation(LocalRoutingInfoLoader localRoutingInfoLoader) {
        this.localRoutingInfoLoader = localRoutingInfoLoader;
    }

    public Mono<ServerResponse> loadLocalConfig(ServerRequest serverRequest) {
        return executeConfigLoad(serverRequest, localRoutingInfoLoader);
    }

    private Mono<ServerResponse> executeConfigLoad(ServerRequest serverRequest, RoutingInfoLoader routingInfoLoader) {
        return serverRequest.bodyToMono(String.class).doOnNext(body -> {
            try {
                JSONObject jsonMap = new JSONObject(body);
                routingInfoLoader.load(jsonMap.get("source").toString());
            } catch (JSONException e) {
                throw new IllegalArgumentException("json error");
            }
        }).then(ServerResponse.status(HttpStatus.ACCEPTED).build()).onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST).build());
    }
}

