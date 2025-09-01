package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.mapper.ILoanApplicationMapper;
import com.crediya.request.api.validation.LoanApplicationValidator;
import com.crediya.request.usecase.cases.LoanApplicationUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {
  private final LoanApplicationUseCase loanApplicationUseCase;
  private final ILoanApplicationMapper loanApplicationMapper;
  private final LoanApplicationValidator loanApplicationValidator;

  public Mono<ServerResponse> listenCreateLoan(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(CreateLoanApplicationDto.class)
      .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.REQUEST_BODY_INVALID)))
      .map(loanApplicationMapper::toModel)
      .flatMap(loanApplication -> loanApplicationValidator.validateAmount(loanApplication.getAmount())
        .then(loanApplicationValidator.validateTerm(loanApplication.getTerm()))
        .then(loanApplicationValidator.validateEmail(loanApplication.getUser().email()))
        .then(loanApplicationValidator.validateCurrency(loanApplication.getCurrency().getCurrencyCode()))
        .then(Mono.just(loanApplication))
      )
      .flatMap(loanApplicationUseCase::create)
      .flatMap(savedLoanApplication -> ServerResponse.ok().bodyValue(savedLoanApplication));
  }
}
