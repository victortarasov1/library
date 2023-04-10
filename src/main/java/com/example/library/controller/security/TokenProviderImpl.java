package com.example.library.controller.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.library.enums.Claim;
import com.example.library.enums.Token;
import com.example.library.exception.AuthorNotFoundException;
import com.example.library.exception.BadTokenException;
import com.example.library.model.Actuality;
import com.example.library.model.AuthorUserDetails;
import com.example.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

import static java.util.Arrays.stream;

@Component
public class TokenProviderImpl implements TokenProvider {

    private final Algorithm algorithm;
    private final AuthorRepository repository;
    @Value("${jwt.access_token.time}")
    private long ACCESS_TOKEN_TIME;

    @Value("${jwt.refresh_token.time}")
    private long REFRESH_TOKEN_TIME;

    public TokenProviderImpl(AuthorRepository repository, @Value("${jwt.secret.key}") String SECRET_KEY) {
        this.repository = repository;
        this.algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    }

    @Override
    public Map<String, String> generateTokens(String requestUrl, AuthorUserDetails user) {
        return Map.of(Token.ACCESS_TOKEN.getTokenType(), generateAccessToken(requestUrl, user),
                Token.REFRESH_TOKEN.getTokenType(), generateRefreshToken(requestUrl, user));
    }

    @Override
    public void authorizeIfAccessTokenIsValid(String accessToken) {
        try {
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(accessToken);
            var username = decoderJWT.getSubject();
            var roles = decoderJWT.getClaim(Claim.ROLES.getClaim()).asArray(String.class);
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (TokenExpiredException | JWTDecodeException ex) {
            throw new BadTokenException();
        }
    }

    @Override
    public Map<String, String> regenerateAccessTokenIfRefreshTokenIsValid(String refreshToken, String requestUrl) {
        try {
            var verifier = JWT.require(algorithm).build();
            var decoderJWT = verifier.verify(refreshToken);
            var username = decoderJWT.getSubject();
            var user = new AuthorUserDetails(repository.findByEmail(username)
                    .orElseThrow(() -> new AuthorNotFoundException(username)));
            return Map.of(Token.ACCESS_TOKEN.getTokenType(), generateAccessToken(requestUrl, user));
        } catch (TokenExpiredException | JWTDecodeException | AuthorNotFoundException ex) {
            throw new BadTokenException();
        }
    }

    private String generateRefreshToken(String requestUrl, AuthorUserDetails user) {
        var instant = Instant.now();
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(REFRESH_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(requestUrl).sign(algorithm);
    }

    private String generateAccessToken(String requestUrl, AuthorUserDetails user) {
        var instant = Instant.now();
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(instant.plus(ACCESS_TOKEN_TIME, ChronoUnit.MINUTES))
                .withIssuer(requestUrl)
                .withClaim(Claim.ROLES.getClaim(), user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList()).sign(algorithm);
    }
}
