package com.example.library.config.filter;

import com.example.library.controller.security.TokenProvider;
import com.example.library.enums.AuthorizationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class RefreshTokensFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final String refreshUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AuthorizationType.BEARER.getPrefix()) && request.getServletPath().equals(refreshUrl))
            regenerateToken(request, response, authorizationHeader);
        else filterChain.doFilter(request, response);
    }

    private void regenerateToken(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        var token = tokenProvider.regenerateAccessTokenIfRefreshTokenIsValid(
                authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length()),
                request.getRequestURL().toString());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
