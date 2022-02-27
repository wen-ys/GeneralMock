package com.dgh.gerneralmock.config.route.info;

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
public class LocalRoutingInfoLoaderTest {

    @SpyBean
    private LocalRoutingInfoLoader localRoutingInfoLoader;

    @SpyBean
    private MockOperation mockOperation;

    @SpyBean
    private RoutingHandler routingHandler;

    @SpyBean
    private RoutingInfo routingInfo;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("[ Routing change ] Dynamic loading from local")
    void routingChangeWithLocalTest() {

        webTestClient.post().uri("/mock/load/local")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("{'source' : './localconfig.yml' }")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        webTestClient.get().uri("/test/local")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo("This is")
                .jsonPath("name").isEqualTo("Local");

    }
}
