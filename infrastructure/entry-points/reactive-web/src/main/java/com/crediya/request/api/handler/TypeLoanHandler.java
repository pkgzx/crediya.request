package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateTypeLoanDto;
import com.crediya.request.api.mapper.ITypeLoanMapper;
import com.crediya.request.api.validation.TypeLoanValidator;
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
  private final TypeLoanValidator  typeLoanValidator;


  public Mono<ServerResponse> saveTypeLoan(ServerRequest request) {
    return request.bodyToMono(CreateTypeLoanDto.class)
      .map(typeLoanMapper::toModel)
      .flatMap(typeLoan -> typeLoanValidator.validateTypeLoanName(typeLoan.getName())
        .then(typeLoanValidator.validateMinimumAmount(typeLoan.getMinAmount()))
        .then(typeLoanValidator.validateMaximumAmount(typeLoan.getMaxAmount()))
        .then(typeLoanValidator.validateInterestRate(typeLoan.getInterestRate()))
        .then(typeLoanValidator.validateCurrency(typeLoan.getCurrency().getCurrencyCode()))
        .then(typeLoanValidator.validateValidationAutomatic(typeLoan.getValidationAutomatic()))
        .then(Mono.just(typeLoan))
      )
      .flatMap(typeLoanUseCase::createTypeLoan)
      .flatMap(typeLoan -> ServerResponse.ok().bodyValue(typeLoan));
  }
}
