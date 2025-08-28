package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.mapper.ILoanApplicationMapper;
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

  public Mono<ServerResponse> listenCreateLoan(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(CreateLoanApplicationDto.class)
      .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.REQUEST_BODY_INVALID)))
      .map(loanApplicationMapper::toModel)
      .flatMap(loanApplicationUseCase::save)
      .flatMap(savedLoanApplication -> ServerResponse.ok().bodyValue(savedLoanApplication));
  }
}
