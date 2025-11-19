package ru.otus.hw.models;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String username;
    private final String userId;
    private final String token;

    public JwtAuthenticationToken(String username, String userId, String token)
    {
        super(Collections.emptyList());
        this.username=username;
        this.userId=userId;
        this.token=token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    public String getUserId()
    {
        return userId;
    }
}
