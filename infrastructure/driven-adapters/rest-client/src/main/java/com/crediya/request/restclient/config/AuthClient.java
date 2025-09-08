package com.crediya.request.restclient.config;

import com.crediya.request.model.auth.Auth;
import com.crediya.request.model.auth.spi.IAuthClient;
import com.crediya.request.restclient.config.dto.ValidateTokenDto;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthClient implements IAuthClient {
  private final WebClient webClient;

  public AuthClient(WebClient.Builder webClientBuilder, @Value("${services.auth.url}") String userServiceUrl) {
    this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
  }

  @Override
  public Mono<Auth> validateToken(String token) {
    return webClient.post()
      .uri("/auth/validate-token")
      .bodyValue(new ValidateTokenDto(token))
      .retrieve()
      .onStatus(HttpStatusCode::is4xxClientError,
        response -> Mono.error(new BusinessException(TechnicalMessage.CLIENT_ERROR)))
      .onStatus(HttpStatusCode::is5xxServerError,
        response -> Mono.error(new BusinessException(TechnicalMessage.SERVER_ERROR)))
      .bodyToMono(Auth.class);
  }
}
