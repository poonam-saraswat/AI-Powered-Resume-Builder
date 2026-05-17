package com.resumeai.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                // Disable default security CORS handling to let the YAML/Dedupe filter take over
                .cors(cors -> cors.disable())
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Temporarily permit all to verify the CORS fix
                )
                .build();
    }
}