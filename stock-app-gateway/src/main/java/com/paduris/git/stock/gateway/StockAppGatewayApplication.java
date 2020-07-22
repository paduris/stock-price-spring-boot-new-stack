package com.paduris.git.stock.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class StockAppGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockAppGatewayApplication.class, args);
    }


    /**
     * https://cloud.spring.io/spring-cloud-circuitbreaker/reference/html/index.html
     *
     * @return
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }
}

@RestController
class GatewayController {
    @GetMapping(path = "/fallback", produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<String> fallback() {
        return Flux.just("{}");
    }

}


