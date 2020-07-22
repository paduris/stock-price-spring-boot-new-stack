package com.paduris.git.stock.gateway;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/fallback")
public class GatewayController {

    @GetMapping(path = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> fallback() {
        System.out.println("Calling Fallback");
        return Flux.just("Service is down, please try after sometime");
    }

}
