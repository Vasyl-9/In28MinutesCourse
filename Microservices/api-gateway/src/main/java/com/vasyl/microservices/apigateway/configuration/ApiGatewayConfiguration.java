package com.vasyl.microservices.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
        return builder.routes()
//                .route(route -> route.path("/get")
//                        .filters(f-> f.addRequestParameter("MyHeader", "MyURI")
//                                      .addRequestParameter("Param", "value"))
//                        .uri("http://httpbin.org:80"))
                .route(r -> r.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(r -> r.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(r -> r.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(r -> r.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath("/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
