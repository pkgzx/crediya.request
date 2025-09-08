package com.crediya.request.config;

import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
import com.crediya.request.model.state.spi.IStateRepository;
import com.crediya.request.model.typeloan.spi.ITypeLoanRepository;
import com.crediya.request.r2dbc.adapter.LoanApplicationPostgresPersistenceAdapter;
import com.crediya.request.r2dbc.adapter.StatePostgresPersistenceAdapter;
import com.crediya.request.r2dbc.adapter.TypeLoanPostgresPersistenceAdapter;
import com.crediya.request.r2dbc.mapper.ILoanApplicationPersistenceMapper;
import com.crediya.request.r2dbc.mapper.IStatePersistenceMapper;
import com.crediya.request.r2dbc.mapper.ITypeLoanPersistenceMapper;
import com.crediya.request.r2dbc.repository.ILoanApplicationPostgresRepository;
import com.crediya.request.r2dbc.repository.IStatePostgresRepository;
import com.crediya.request.r2dbc.repository.ITypeLoanPostgresRepository;
import com.crediya.request.usecase.cases.LoanApplicationUseCase;
import com.crediya.request.usecase.cases.StateUseCase;
import com.crediya.request.usecase.cases.TypeLoanUseCase;
import com.crediya.request.usecase.client.IUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
  private final IStatePostgresRepository statePostgresRepository;
  private final IStatePersistenceMapper statePersistenceMapper;
  private final ITypeLoanPostgresRepository typeLoanPostgresRepository;
  private final ITypeLoanPersistenceMapper typeLoanPersistenceMapper;

  private final ILoanApplicationPostgresRepository loanApplicationPostgresRepository;
  private final ILoanApplicationPersistenceMapper loanApplicationPersistenceMapper;
  private final TransactionalOperator transactionalOperator;
  private final R2dbcEntityTemplate  r2dbcEntityTemplate;

  @Bean
  public StatePostgresPersistenceAdapter getStatePostgresRepository() {
    return new StatePostgresPersistenceAdapter(statePostgresRepository, statePersistenceMapper, transactionalOperator);
  }

  @Bean
  public StateUseCase getStateUseCase(IStateRepository stateRepository) {
    return new StateUseCase(stateRepository);
  }

  @Bean
  public TypeLoanPostgresPersistenceAdapter getTypeLoanPostgresRepository() {
    return new TypeLoanPostgresPersistenceAdapter(typeLoanPostgresRepository, typeLoanPersistenceMapper, transactionalOperator);
  }

  @Bean
  public TypeLoanUseCase getTypeLoanUseCase(ITypeLoanRepository typeLoanRepository) {
    return new TypeLoanUseCase(typeLoanRepository);
  }


  @Bean
  public LoanApplicationPostgresPersistenceAdapter getLoanApplicationPostgresRepository() {
    return new LoanApplicationPostgresPersistenceAdapter(r2dbcEntityTemplate, loanApplicationPostgresRepository, loanApplicationPersistenceMapper, transactionalOperator);
  }


  @Bean
  public LoanApplicationUseCase getLoanApplicationUseCase(ILoanApplicationRepository loanApplicationRepository, IUserClient userClient, TypeLoanUseCase typeLoanUseCase, StateUseCase stateUseCase) {
    return new LoanApplicationUseCase(loanApplicationRepository, userClient, typeLoanUseCase, stateUseCase);
  }


}
