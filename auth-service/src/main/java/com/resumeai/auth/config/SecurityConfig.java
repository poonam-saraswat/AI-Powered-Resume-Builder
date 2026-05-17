package com.resumeai.auth.config;

import com.resumeai.auth.security.JwtAuthenticationFilter;
import com.resumeai.auth.security.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final @Lazy OAuth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CORS here because Gateway handles it
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/api/auth/register","/api/auth/login","/api/auth/refresh",
                                "/api/auth/forgot-password","/api/auth/reset-password",
                                "/oauth2/**","/login/**",
                                "/actuator/**","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(o -> o.successHandler(oauth2SuccessHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}