package com.example.library.config.filter;

import com.example.library.controller.security.TokenProvider;
import com.example.library.enums.AuthorizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AuthorizationType.BEARER.getPrefix()))
            tokenProvider.authorizeIfAccessTokenIsValid(authorizationHeader.substring(AuthorizationType.BEARER.getPrefix().length()));
        filterChain.doFilter(request, response);
    }

}
