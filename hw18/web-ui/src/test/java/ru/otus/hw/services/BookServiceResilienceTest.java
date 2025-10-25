package ru.otus.hw.services;

import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookServiceResilienceTest {
    @Autowired
    BookService bookService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RetryRegistry retryRegistry;

    @BeforeEach
    void setUp() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("uiCircuitBreakerService");
        circuitBreaker.transitionToClosedState();
    }

    @Test
    void shouldActivateRetryBehavior() {
        Retry retry = retryRegistry.retry("uiRetryService");
        assertThatThrownBy(() -> bookService.findAll())
                .isInstanceOf(RetryableException.class);
        assertThat(retry.getMetrics().getNumberOfTotalCalls()).isEqualTo(3);
    }

    @Test
    void shouldActivateCircuitBreakerAfterFailures() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("uiCircuitBreakerService");
        Retry retry = retryRegistry.retry("uiRetryService");

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        for (int i = 0; i < 3; i++) {
            try {
                bookService.findAll();
            } catch (Exception e) {

            }
        }
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        assertThat(retry.getMetrics().getNumberOfTotalCalls()).isEqualTo(12);
    }


}
