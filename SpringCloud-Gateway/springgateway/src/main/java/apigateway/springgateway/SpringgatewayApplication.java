package apigateway.springgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringgatewayApplication.class, args);
    }

//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("items", predicateSpec -> predicateSpec.path("/item/**")
//                        .filters(gatewayFilterSpec -> gatewayFilterSpec.circuitBreaker(config -> config.setName("customCircuit").setFallbackUri("forward:/client/fallback/fail"))).uri("lb://ITEMS"))
//                .route("clients", predicateSpec -> predicateSpec.path("/client/**").uri("lb://CLIENTS"))
//                .build();
//    }

}