package com.crediya.request.r2dbc.adapter;

import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
import com.crediya.request.r2dbc.mapper.ILoanApplicationPersistenceMapper;
import com.crediya.request.r2dbc.repository.ILoanApplicationPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class LoanApplicationPostgresPersistenceAdapter implements ILoanApplicationRepository {

  private final ILoanApplicationPostgresRepository repository;
  private final ILoanApplicationPersistenceMapper mapper;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<LoanApplication> save(LoanApplication loanApplication) {
    return repository.save(mapper.toEntity(loanApplication))
      .doOnNext(e -> log.info("Loan Application saved with id {}", e.getId()))
      .map(mapper::toModel)
      .as(transactionalOperator::transactional);
  }
}
