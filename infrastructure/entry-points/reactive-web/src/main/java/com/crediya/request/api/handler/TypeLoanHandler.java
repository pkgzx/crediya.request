package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateTypeLoanDto;
import com.crediya.request.api.mapper.ITypeLoanMapper;
import com.crediya.request.usecase.cases.TypeLoanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TypeLoanHandler {
  private final TypeLoanUseCase typeLoanUseCase;
  private final ITypeLoanMapper typeLoanMapper;


  public Mono<ServerResponse> saveTypeLoan(ServerRequest request) {
    return request.bodyToMono(CreateTypeLoanDto.class)
      .map(typeLoanMapper::toModel)
      .flatMap(typeLoanUseCase::createTypeLoan)
      .flatMap(typeLoan -> ServerResponse.ok().bodyValue(typeLoan));
  }
}
