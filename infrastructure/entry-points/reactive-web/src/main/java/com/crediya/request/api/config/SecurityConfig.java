package com.crediya.request.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;


@Configuration
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthFilter jwtFilter) {

    return http
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .authorizeExchange(exchange -> exchange
        .pathMatchers(
          "/v3/api-docs",
          "/api/swagger-ui.html",
          "/api/swagger-ui/",
          "/api/webjars/swagger-ui/**",
          "/api/docs",
          "/webjars/swagger-ui/**",
          "/favicon.ico",
          "/v3/api-docs/swagger-config"
        ).permitAll()
        .pathMatchers(
          "/api/v1/loan-application"

        ).hasAnyRole("CLIENT")

        .pathMatchers(
          "/api/v1/loan-application/paginated",
          "/api/v1/loan-application/*"
        )
        .hasAnyRole("ADMIN", "ADVISOR")
        .anyExchange().authenticated()
      )
      .addFilterAfter(jwtFilter, SecurityWebFiltersOrder.FIRST)
      .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
      .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .logout(ServerHttpSecurity.LogoutSpec::disable)
      .build();
  }
}