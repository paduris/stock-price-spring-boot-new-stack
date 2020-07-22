package com.paduris.git.stock.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("intel-service", route -> route.path("/inter-service/**")
                        .and()
                        .method(HttpMethod.GET)
                        .filters(filter -> filter.stripPrefix(1))
                        .uri("lb://intel-service"))
                .build();
    }
}
