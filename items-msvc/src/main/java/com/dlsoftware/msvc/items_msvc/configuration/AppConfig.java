package com.dlsoftware.msvc.items_msvc.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
            return (factory) -> {
                factory.configureDefault(
                        id -> {
                            return new Resilience4JConfigBuilder(id)
                                        .circuitBreakerConfig(
                                            CircuitBreakerConfig.custom()
                                                    .slidingWindowSize(10) // Cantidad de llamadas utilizadas para crear la tasa de fallos y aciertos
                                                    .failureRateThreshold(50) // Tasa máxima de fallos antes de abrir el circuito
                                                    .waitDurationInOpenState(Duration.ofSeconds(10L)) // Cantidad de tiempo antes de pasar a HalfOpen
                                                    .permittedNumberOfCallsInHalfOpenState(5)
                                                    .slowCallDurationThreshold(Duration.ofSeconds(2L))
                                                    .slowCallRateThreshold(50)
                                                    .build())
                                        .timeLimiterConfig(
                                            TimeLimiterConfig.custom()
                                                    .timeoutDuration(Duration.ofSeconds(4L))
                                                    .build()
                                        )
                                        .build();
                        }
                );
            };
    }
}
