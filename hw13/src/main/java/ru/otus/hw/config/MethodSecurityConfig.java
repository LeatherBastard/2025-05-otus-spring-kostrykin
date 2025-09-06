package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {

    private final MethodSecurityExpressionHandler methodSecurityExpressionHandler;

    public MethodSecurityConfig(MethodSecurityExpressionHandler methodSecurityExpressionHandler) {
        this.methodSecurityExpressionHandler = methodSecurityExpressionHandler;
    }

}