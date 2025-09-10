package com.crediya.request.restclient.config;

import com.crediya.request.model.loan_application.UserDetails;
import com.crediya.request.usecase.client.IUserClient;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserClient implements IUserClient {
    private final WebClient webClient;

    public UserClient(WebClient.Builder webClientBuilder, @Value("${services.auth.url}") String userServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
    }

    public Mono<UserDetails> getUserByEmail(String email) {
        return webClient.get()
                .uri("/users/email/{email}", email)
                .retrieve()
          .onStatus(status -> status.value() == 404,
            response -> Mono.error(new BusinessException(TechnicalMessage.EMAIL_USER_NOT_FOUND)))
          .onStatus(HttpStatusCode::is4xxClientError,
            response -> Mono.error(new BusinessException(TechnicalMessage.CLIENT_ERROR)))
          .onStatus(HttpStatusCode::is5xxServerError,
            response -> Mono.error(new BusinessException(TechnicalMessage.SERVER_ERROR)))
                .bodyToMono(UserDetails.class);
    }

  @Override
  public Mono<UserDetails> getUserById(String id) {
    log.info("Enter to getUserById: {}", id);
    return webClient.get()
      .uri("/users/id/{id}", id)
      .retrieve()
      .onStatus(status -> status.value() == 404,
        response -> Mono.error(new BusinessException(TechnicalMessage.EMAIL_USER_NOT_FOUND)))
      .onStatus(HttpStatusCode::is4xxClientError,
        response -> Mono.error(new BusinessException(TechnicalMessage.CLIENT_ERROR)))
      .onStatus(HttpStatusCode::is5xxServerError,
        response -> Mono.error(new BusinessException(TechnicalMessage.SERVER_ERROR)))
      .bodyToMono(UserDetails.class);
  }
}