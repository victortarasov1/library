package com.example.library.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.library.model.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private String SECRET_KEY;

    private Integer ACCESS_TOKEN_TIME;

    private Integer REFRESH_TOKEN_TIME;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var email = request.getParameter("email");
        var password = request.getParameter("password");
        var token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(token);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        var instant = Instant.now();
        var user = (UserDetailsImpl) authResult.getPrincipal();
        var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        var access = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(instant.plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
        var refresh = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(instant.plus(REFRESH_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);


        var tokens = new HashMap<String, String>();
        tokens.put("access_token", access);
        tokens.put("refresh_token", refresh);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        var errors = new HashMap<String, String>();
        errors.put("debugMessage", "bad password and/or email");
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }
}
