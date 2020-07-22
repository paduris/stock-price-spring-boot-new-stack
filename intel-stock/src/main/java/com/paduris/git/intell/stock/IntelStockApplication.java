package com.paduris.git.intell.stock;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class IntelStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelStockApplication.class, args);
    }


    @RequiredArgsConstructor
    @RestController
    class IntelStockController {

        @NonNull
        private Environment environment;

        @NonNull
        private WebClient.Builder webClientBuilder;

        @CircuitBreaker(fallbackMethod = "emptyResponse", name = "default")
        @GetMapping(value = "/stock", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        Flux<IntelData> getIntelStockData() {
            return webClientBuilder.build()
                    .get()
                    .uri("http://stock-service/stock/INTC")
                    .retrieve()
                    .bodyToFlux(String.class)
                    .map(map -> IntelData.builder()
                            .ceoName("Bob Swan")
                            .headQuatersLocation("Santa Clara, California")
                            .companyName("Intel Corporation")
                            .providedByServiceName("Intel Serivce")
                            .otherMessage(" This respose coming from server with port :: "+ environment.getProperty("local.server.port"))
                            .stockData(map)
                            .build());
        }

        @GetMapping(path = "/empty", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        Flux<IntelData> emptyResponse(Exception exception) {
            return Flux.just(new IntelData.IntelDataBuilder()
                    .stockData("No data available at this moment")
                    .build());
        }

    }


    @Configuration
    class Config {

        @Bean
        @LoadBalanced
        WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }
    }


}

@Data
@Builder
class IntelData {
    private String companyName;
    private String headQuatersLocation;
    private String ceoName;
    private String stockData;
    private String providedByServiceName;
    private String otherMessage;
}
