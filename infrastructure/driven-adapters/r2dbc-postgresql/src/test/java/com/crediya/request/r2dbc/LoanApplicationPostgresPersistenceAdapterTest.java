package com.crediya.request.r2dbc;

import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.r2dbc.adapter.LoanApplicationPostgresPersistenceAdapter;
import com.crediya.request.r2dbc.entity.LoanApplicationEntity;
import com.crediya.request.r2dbc.mapper.ILoanApplicationPersistenceMapper;
import com.crediya.request.r2dbc.repository.ILoanApplicationPostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class LoanApplicationPostgresPersistenceAdapterTest {
  @Mock
  private ILoanApplicationPostgresRepository repository;
  @Mock
  private  ILoanApplicationPersistenceMapper mapper;
  @Mock
  private  TransactionalOperator transactionalOperator;

  private LoanApplicationPostgresPersistenceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new LoanApplicationPostgresPersistenceAdapter(repository, mapper, transactionalOperator);
  }


  @Test
  void validSave(){
    LoanApplication loanApplication = LoanApplication.builder()
      .amount(BigDecimal.valueOf(2000))
      .build();

    LoanApplicationEntity entity = LoanApplicationEntity.builder()
      .amount(BigDecimal.valueOf(2000))
      .build();

    when(mapper.toEntity(loanApplication)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(Mono.just(entity));
    when(mapper.toModel(entity)).thenReturn(loanApplication);
    when(transactionalOperator.transactional(any(Mono.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    StepVerifier.create(adapter.save(loanApplication))
      .expectNext(loanApplication)
      .verifyComplete();
  }
}
