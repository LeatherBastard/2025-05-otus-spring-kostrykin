package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.JwtAuthenticationToken;
import ru.otus.hw.utils.JwtUtils;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getTokenFromCookies(exchange.getRequest());

        if (token != null && !token.isEmpty() && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            String userId = jwtUtils.getUserIdFromToken(token);

            if (username != null) {
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        token,
                        authorities
                );

                // Устанавливаем дополнительные детали, если нужно
                ((UsernamePasswordAuthenticationToken) authentication).setDetails(userId);

                // Правильно устанавливаем аутентификацию в контекст
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }

        // Если токен не валиден, продолжаем без аутентификации
        return chain.filter(exchange);
    }


    private String getTokenFromCookies(ServerHttpRequest request) {
        String token=null;
        List<HttpCookie> cookies=request.getCookies().get("AUTH_TOKEN");
        if(!cookies.isEmpty())
        {
           token = cookies.get(0).getValue();
        }
        return token;
    }
}
