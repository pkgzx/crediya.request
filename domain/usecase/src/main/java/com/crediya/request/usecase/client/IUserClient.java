package com.crediya.request.usecase.client;

import com.crediya.request.model.loan_application.UserDetails;
import reactor.core.publisher.Mono;

public interface IUserClient {
  Mono<UserDetails> getUserByEmail(String email);
}
