package com.crediya.request.model.auth.spi;

import com.crediya.request.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface IAuthClient {
  Mono<Auth> validateToken(String token);
}
