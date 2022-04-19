package com.wenys.gerneralmock.controller;

import com.wenys.gerneralmock.config.filter.Filter;
import com.wenys.gerneralmock.config.route.Response;
import com.wenys.gerneralmock.config.route.info.RoutingInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class RoutingHandler {

    private RoutingInfo routingInfo;

    @PostConstruct
    private void postConstruct() {
        this.prepareRoutingInfo();
    }

    public Mono<ServerResponse> route(ServerRequest serverRequest) {
        ServerHttpRequest serverHttpRequest = serverRequest.exchange().getRequest();
        String path = serverHttpRequest.getPath().value();
        String header = serverHttpRequest.getHeaders().toString();
        String queryParam = serverHttpRequest.getQueryParams().toString();
        log.info(String.format("Path: %s / QueryParam: %s / Header: %s", path, queryParam, header));
        ServerResponseBuilder serverResponseBuilder = new ServerResponseBuilder();
        Mono<ServerResponseBuilder> responseMono = Mono.just(serverResponseBuilder);

        for(RoutingInfo.RoutingPolicy policy : routingInfo.getRoutingPolicies()) {
            if(matchPath(path, policy.getPath())) {
                Response customResponse = policy.getResponse();
                if (customResponse != null) {
                    responseMono = responseMono.flatMap(pServerResponseBuilder -> {
                        pServerResponseBuilder.responseCode(customResponse.getCode()).responseBody(customResponse.getBody());
                        return Mono.just(pServerResponseBuilder);
                    });
                }

                for(Filter filter : policy.getFilters()) {
                    responseMono = filter.doFilter(responseMono);
                }
            }
        }
        return responseMono.flatMap(ServerResponseBuilder::build);
    }

    private boolean matchPath(String urlPath, String rolePath) {
        return urlPath.equals(rolePath);
    }

    public void prepareRoutingInfo() {
        for(RoutingInfo.RoutingPolicy policy : routingInfo.getRoutingPolicies()) {
            for(Filter filter : policy.getFilters()) {
                filter.init();
            }
        }
    }

    public void setRoutingInfo(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
        prepareRoutingInfo();
    }

}
