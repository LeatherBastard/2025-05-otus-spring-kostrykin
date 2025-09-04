package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Активируйте @PreAuthorize, @PostAuthorize и др.
public class MethodSecurityConfig {

    private final MethodSecurityExpressionHandler methodSecurityExpressionHandler;

    public MethodSecurityConfig(MethodSecurityExpressionHandler methodSecurityExpressionHandler) {
        this.methodSecurityExpressionHandler = methodSecurityExpressionHandler;
    }

    // ExpressionHandler будет автоматически использоваться Spring'ом
}