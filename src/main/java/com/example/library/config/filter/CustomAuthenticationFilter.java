package com.example.library.config.filter;


import com.example.library.controller.security.TokenProvider;
import com.example.library.enums.AuthenticationRequestParameter;
import com.example.library.exception.BadPasswordOrEmailException;
import com.example.library.model.AuthorUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var email = request.getParameter(AuthenticationRequestParameter.EMAIL.getParameter());
        var password = request.getParameter(AuthenticationRequestParameter.PASSWORD.getParameter());
        var token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticate(token);
    }

    private Authentication authenticate(UsernamePasswordAuthenticationToken token) {
        try {
            return authenticationManager.authenticate(token);
        } catch (InternalAuthenticationServiceException ex) {
            throw new BadPasswordOrEmailException(token.getName());
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        var user = (AuthorUserDetails) authResult.getPrincipal();
        var tokens = tokenProvider.generateTokens(request.getRequestURL().toString(), user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
