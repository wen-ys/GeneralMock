package com.dgh.basicmock.config.route.info;

import com.dgh.basicmock.config.filter.Filter;
import com.dgh.basicmock.config.route.Response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "default")
public class RoutingInfo {
    private List<RoutingPolicy> routingPolicies;

    @Getter
    @Setter
    public static class RoutingPolicy {
        private String path;
        private List<Filter> filters = Collections.emptyList();
        private Response response;
    }
}
