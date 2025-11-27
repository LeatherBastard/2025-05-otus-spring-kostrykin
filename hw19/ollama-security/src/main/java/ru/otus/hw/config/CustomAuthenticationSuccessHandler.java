package ru.otus.hw.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.utils.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtil;

    private final UserRepository userRepository;

    @Value("${redirection.url}")
    private String redirectionUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String userName = authentication.getName();
        User user = userRepository.findByUsername(userName).
                orElseThrow(() -> new EntityNotFoundException(
                        String.format("User with name %s was not found!", userName)));
        String token = jwtUtil.generateToken(userName, user.getId());

        response.setStatus(HttpServletResponse.SC_OK);

        Cookie myCookie = new Cookie("AUTH_TOKEN", token);

        myCookie.setMaxAge(604800);
        myCookie.setPath("/");
        myCookie.setHttpOnly(true);
        myCookie.setSecure(true);

        response.addCookie(myCookie);
        response.sendRedirect(redirectionUrl);

    }
}