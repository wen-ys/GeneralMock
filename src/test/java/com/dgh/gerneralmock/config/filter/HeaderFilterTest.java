package com.dgh.gerneralmock.config.filter;

import com.dgh.gerneralmock.config.route.info.LocalRoutingInfoLoader;
import com.dgh.gerneralmock.config.route.info.RoutingInfo;
import com.dgh.gerneralmock.controller.MockOperation;
import com.dgh.gerneralmock.controller.Router;
import com.dgh.gerneralmock.controller.RoutingHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = Router.class)
public class HeaderFilterTest {

    @SpyBean
    private RoutingInfo routingInfo;

    @SpyBean
    private MockOperation mockOperation;

    @SpyBean
    private RoutingHandler routingHandler;

    @SpyBean
    private LocalRoutingInfoLoader localRoutingInfoLoader;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("[ HeaderFilter ] test")
    void headerFilterTest() {

        webTestClient.post().uri("/books/2")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("responseHeader", "test");
    }
}
