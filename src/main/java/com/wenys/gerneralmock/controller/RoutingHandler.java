package com.wenys.gerneralmock.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.wenys.gerneralmock.config.filter.Filter;
import com.wenys.gerneralmock.config.route.Response;
import com.wenys.gerneralmock.config.route.info.RoutingInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

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

		return serverHttpRequest.getBody().collectList().flatMap(dataBuffer -> {
			String path = serverHttpRequest.getPath().value();
			String header = serverHttpRequest.getHeaders().toString();
			String queryParam = serverHttpRequest.getQueryParams().toString();

			StringBuilder rawBody = new StringBuilder();
			dataBuffer.forEach(data -> {
				byte[] bytes = new byte[data.readableByteCount()];
				data.read(bytes);
				DataBufferUtils.release(data);
				rawBody.append(new String(bytes, StandardCharsets.UTF_8));
			});

			log.info(String.format("Path: %s / QueryParam: %s / Header: %s / body : %s", path, queryParam, header, rawBody));
			ServerResponseBuilder serverResponseBuilder = new ServerResponseBuilder();
			Mono<ServerResponseBuilder> responseMono = Mono.just(serverResponseBuilder);

			for (RoutingInfo.RoutingPolicy policy : routingInfo.getRoutingPolicies()) {
				if (matchRouting(serverHttpRequest, policy)) {
					Response customResponse = policy.getResponse();
					if (customResponse != null) {
						responseMono = responseMono.flatMap(pServerResponseBuilder -> {
							pServerResponseBuilder.responseCode(customResponse.getCode())
									.responseBody(customResponse.getBody());
							return Mono.just(pServerResponseBuilder);
						});
					}

					for (Filter filter : policy.getFilters()) {
						responseMono = filter.doFilter(responseMono);
					}
					break;
				}
			}
			return responseMono.flatMap(ServerResponseBuilder::build);

		});

	}

	private boolean matchRouting(ServerHttpRequest serverHttpRequest, RoutingInfo.RoutingPolicy policy) {
		return matchPath(serverHttpRequest.getPath().toString(), policy.getPath())
				&& matchParams(serverHttpRequest.getQueryParams(), policy.getParams());
	}

	private boolean matchPath(String urlPath, String rolePath) {
		return urlPath.equals(rolePath);
	}

	private boolean matchParams(MultiValueMap<String, String> requestParams, HashMap<String, ArrayList<String>> policyParams) {
		if (policyParams.isEmpty()) {
			return true;
		}

		if (requestParams.size() != policyParams.size()) {
			return false;
		}

		for (Map.Entry<String, List<String>> entry : requestParams.entrySet()) {
			if (!policyParams.containsKey(entry.getKey())) {
				return false;
			}

			if (!requestParams.get(entry.getKey()).equals(policyParams.get(entry.getKey()))) {
				return false;
			}
		}
		return true;
	}


	public void prepareRoutingInfo() {
		for (RoutingInfo.RoutingPolicy policy : routingInfo.getRoutingPolicies()) {
			for (Filter filter : policy.getFilters()) {
				filter.init();
			}
		}
	}

	public void setRoutingInfo(RoutingInfo routingInfo) {
		this.routingInfo = routingInfo;
		prepareRoutingInfo();
	}

}
