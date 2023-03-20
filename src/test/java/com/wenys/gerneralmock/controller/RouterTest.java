package com.wenys.gerneralmock.controller;

import com.wenys.gerneralmock.config.route.info.LocalRoutingInfoLoader;
import com.wenys.gerneralmock.config.route.info.RoutingInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = Router.class)
public class RouterTest {

    @SpyBean
    private RoutingInfo routingInfo;

    @SpyBean
    private MockOperation mockOperation;

    @SpyBean
    private RoutingHandler routingHandler;

    @SpyBean
    private LocalRoutingInfoLoader localRoutingInfoLoader;

    @SpyBean
    private WebTestClient webTestClient;

    @Test
    @DisplayName("[ Routing default - GET /test/ok ] 200 OK")
    void routeOkResponseTest() {

        // When & Then
        webTestClient.get().uri("/test/ok")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @DisplayName("[ Routing - GET /test/404 ] 404 NOT Found")
    void routeNotFoundResponseTest() {

        // When & Then
        webTestClient.get().uri("/test/404")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("Not Found");
    }

    @Test
    @DisplayName("[ Routing - GET /books/1 ] With Param")
    void routeWithParamResponseTest() {

        // When & Then
        webTestClient.get().uri("/books/1?number=1&isOn=on&number=2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Param Good");
    }

    @Test
    @DisplayName("[ Routing - GET /books/1 ] no Param")
    void routeNoParamResponseTest() {

        // When & Then
        webTestClient.get().uri("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("no Param");
    }

}
