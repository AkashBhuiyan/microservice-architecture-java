package com.akash.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator bankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/bank/accounts/**")
                        .filters(f -> f.rewritePath("/bank/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contactSupport")))
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path("/bank/loan/**")
                        .filters(f -> f.rewritePath("/bank/loan/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3) // Retry up to 3 times if the request fails
                                        .setMethods(HttpMethod.GET) // Apply retry only for GET requests
                                        .setBackoff(
                                                Duration.ofMillis(100),   // Initial backoff delay of 100 milliseconds
                                                Duration.ofMillis(1000),  // Maximum backoff delay of 1000 milliseconds
                                                2,                        // Exponential multiplier of 2 for each retry
                                                true                      // Enable jitter to randomize backoff slightly and avoid retry spikes
                                        )
                                ))
                        .uri("lb://LOAN"))
                .route(p -> p
                        .path("/bank/cards/**")
                        .filters(f -> f.rewritePath("/bank/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()) // Adds a custom response header "X-Response-Time" with the current timestamp
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter()) // Configures rate limiting using the RedisRateLimiter bean defined below
                                        .setKeyResolver(userKeyResolver())  // Uses the KeyResolver bean to identify users based on a header (e.g., "user")
                                )
                        )
                        .uri("lb://CARDS"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        // Define a custom configuration for the ReactiveResilience4JCircuitBreakerFactory
        return factory -> factory.configureDefault(id ->
                new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()) // Use default circuit breaker configuration
                        .timeLimiterConfig(
                                TimeLimiterConfig.custom()
                                        .timeoutDuration(Duration.ofSeconds(4)) // Set timeout duration to 4 seconds for time limiter
                                        .build()
                        )
                        .build()
        );
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // Replenish Rate: 1 token per second (this controls how fast tokens are added to the bucket)
        // Burst Capacity: 1 (this is the maximum number of tokens the bucket can hold, allowing 1 burst request)
        // Requested Tokens: 1 (each request consumes 1 token)
        return new RedisRateLimiter(1, 1, 1);
    }

    @Bean
    KeyResolver userKeyResolver() {
        // Defines how to resolve the user based on the request headers.
        // If the 'user' header is present, it resolves the user from that header.
        // If the 'user' header is not found, it defaults to "anonymous".
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
