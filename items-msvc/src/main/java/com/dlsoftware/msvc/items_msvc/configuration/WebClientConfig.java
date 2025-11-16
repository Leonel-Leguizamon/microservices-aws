package com.dlsoftware.msvc.items_msvc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(
            @Value("${configs.baseurls.products}") String productBaseUrl,
            WebClient.Builder webClientBuilder,
            ReactorLoadBalancerExchangeFilterFunction lbFunction
    ) {
        return webClientBuilder
                    .baseUrl(productBaseUrl)
                    .filter(lbFunction)
                    .build();
    }


//    No se autoconfigura el contexto para propagacion de las trazas
//    @Bean
//    @LoadBalanced
//    WebClient.Builder webClient(){
//        return WebClient.builder().baseUrl(productBaseUrl);
//    }
}
