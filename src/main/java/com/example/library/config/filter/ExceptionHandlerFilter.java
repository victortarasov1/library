package com.example.library.config.filter;

import com.example.library.dto.ApiError;
import com.example.library.exception.BadPasswordOrEmailException;
import com.example.library.exception.BadTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BadPasswordOrEmailException | BadTokenException ex) {
            response.setHeader("error", ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            var error = new ApiError("authorization or authentication exception", List.of(ex.getMessage()));
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
