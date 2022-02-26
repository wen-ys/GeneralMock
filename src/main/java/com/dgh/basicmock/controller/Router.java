package com.dgh.basicmock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@RequiredArgsConstructor
@Configuration
public class Router {

    private final RoutingHandler routingHandler;
    private final MockOperation mockOperation;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .POST("/mock/load/local", mockOperation::loadLocalConfig)
                .POST("/**", routingHandler::route)
                .GET("/**", routingHandler::route)
                .build();
    }

}
