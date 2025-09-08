package com.crediya.request.api.config;

import com.crediya.request.model.auth.Auth;
import com.crediya.request.model.auth.spi.IAuthClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthFilter implements WebFilter {

  private final IAuthClient validateTokenClient;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    String path = exchange.getRequest().getPath().value();
    if (path.startsWith("/v3/api-docs") ||
      path.startsWith("/api/swagger-ui") ||
      path.startsWith("/api/webjars/swagger-ui") ||
      path.equals("/api/docs") ||
      path.startsWith("/webjars/swagger-ui") ||
      path.equals("/favicon.ico") ||
      path.startsWith("/api/v1/loan-application/paginated")
    ) {
      return chain.filter(exchange);
    }
    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (auth == null) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    if (!auth.startsWith("Bearer ")) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    String token = auth.replace("Bearer ", "");
    return validateTokenClient.validateToken(token)
      .flatMap(user -> {
        Authentication authentication = createAuthentication(user);
        return chain.filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
      })
      .onErrorResume(e -> {
        log.error("Error validando token", e);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      });


  }
  private Authentication createAuthentication(Auth user) {
    return new UsernamePasswordAuthenticationToken(
      user.email(),
      null,
      List.of(new SimpleGrantedAuthority("ROLE_" + user.role()))
    );
  }
}