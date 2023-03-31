package com.example.library.config;

import com.example.library.config.filter.CustomAuthenticationFilter;
import com.example.library.config.filter.CustomAuthorizationFilter;
import com.example.library.config.filter.ExceptionHandlerFilter;
import com.example.library.config.filter.RefreshTokensFilter;
import com.example.library.controller.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;


    @Value("${project.login.url}")
    private String LOGIN_URL;

    @Value("${project.refresh.url}")
    private String REFRESH_URL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBuilder.getOrBuild(), tokenProvider);
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        http
                .cors(withDefaults())
                .csrf().disable()
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults())
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(tokenProvider), CustomAuthenticationFilter.class)
                .addFilterBefore(new RefreshTokensFilter(tokenProvider, REFRESH_URL), CustomAuthorizationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), RefreshTokensFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
