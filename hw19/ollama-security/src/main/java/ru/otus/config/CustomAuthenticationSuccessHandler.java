package ru.otus.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.otus.exceptions.EntityNotFoundException;
import ru.otus.models.User;
import ru.otus.repositories.UserRepository;
import ru.otus.utils.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtil;
    private final UserRepository userRepository;

    @Value("${redirection.url}")
    private String redirectionUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String userName = authentication.getName();
        User user = userRepository.findByUsername(userName).
                orElseThrow(() -> new EntityNotFoundException(String.format("User with name %s was not found!", userName)));
        String token = jwtUtil.generateToken(userName, user.getId());

        response.setStatus(HttpServletResponse.SC_OK);

        Cookie myCookie = new Cookie("AUTH_TOKEN", token);

        // Set cookie properties (optional but recommended for security and control)
        myCookie.setMaxAge(60 * 60 * 24 * 7); // 1 week in seconds
        myCookie.setPath("/"); // Accessible across the entire application
        myCookie.setHttpOnly(true); // Prevents client-side JavaScript access (XSS protection)
        myCookie.setSecure(true); // Ensures cookie is only sent over HTTPS

        // Add the cookie to the response
        response.addCookie(myCookie);

        // Optionally, redirect the user after setting the cookie
        response.sendRedirect(redirectionUrl);

    }
}