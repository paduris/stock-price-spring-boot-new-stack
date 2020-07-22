package com.paduris.git.stock.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;

@EnableDiscoveryClient
@SpringBootApplication
public class StockPriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockPriceApplication.class, args);
    }


    @RestController
    class StockService {


        // Server pushing events to clients, uni directional
        @GetMapping(value = "/stock/{name}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<String> getStockPrice(@PathVariable String name) {

            return Flux.interval(Duration.ofSeconds(2))
                    .map(m -> getStockPriceFromYahoo(name));

        }


        private String getStockPriceFromYahoo(String name) {
            try {
                Stock stock = YahooFinance.get(name);

                BigDecimal price = stock.getQuote().getPrice();
                BigDecimal change = stock.getQuote().getChangeInPercent();
                BigDecimal peg = stock.getStats().getPeg();
                BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

                StringBuilder builder = new StringBuilder();
                builder.append("Price :: " + price + " \n");
                builder.append("Change :: " + change + " \n");
                builder.append("Peg :: " + peg + " \n");
                builder.append("Dividend :: " + dividend + " \n");

                return builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // handle this ->
            return null;
        }
    }
}
