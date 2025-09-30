package ru.otus.hw.actuators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ActuatorHealthIndicator implements HealthIndicator {
    private final RestTemplate restTemplate;

    public ActuatorHealthIndicator() {
        restTemplate = new RestTemplate();
    }

    @Value("${server.port}")
    private String serverPort;

    @Override
    public Health health() {
        try {
            ResponseEntity<String> response = restTemplate
                    .getForEntity("http://localhost:" + serverPort + "/actuator", String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Health.up()
                        .withDetail("message", "Available")
                        .build();
            }
            return Health.down().withDetail("message", "Unreachable")
                    .withDetail("status", response.getStatusCode().value())
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("message", "exception")
                    .withException(e)
                    .build();
        }
    }
}
