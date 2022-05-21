package com.vasyl.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    @Retry(name = "default", fallbackMethod = "hardcodedResponse")
    //@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
    //rate = 10s => 10000calls to the same api
    @RateLimiter(name = "default")
    @Bulkhead(name = "default", fallbackMethod = "hardcodedResponse")
    public String sampleApi(){
        logger.info("Sample API call received");
        ResponseEntity<String> entity =
                new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
        return entity.getBody();
    }

    public String hardcodedResponse(Exception ex){
        return "fallback";
    }
}
