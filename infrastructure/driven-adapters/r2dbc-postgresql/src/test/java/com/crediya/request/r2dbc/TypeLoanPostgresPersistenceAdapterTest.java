package com.crediya.request.r2dbc;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.r2dbc.adapter.TypeLoanPostgresPersistenceAdapter;
import com.crediya.request.r2dbc.entity.TypeLoanEntity;
import com.crediya.request.r2dbc.mapper.ITypeLoanPersistenceMapper;
import com.crediya.request.r2dbc.repository.ITypeLoanPostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class TypeLoanPostgresPersistenceAdapterTest {
  @Mock
  private  ITypeLoanPostgresRepository repository;
  @Mock
  private  ITypeLoanPersistenceMapper mapper;
  @Mock
  private  TransactionalOperator transactionalOperator;

  private TypeLoanPostgresPersistenceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new TypeLoanPostgresPersistenceAdapter(repository, mapper, transactionalOperator);


  }

  @Test
  void validCreateTypeLoan() {
    TypeLoan typeLoan = TypeLoan.builder()
      .name("BASIC_LOAN")
      .currency(Currency.getInstance("USD"))
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    TypeLoanEntity entity = TypeLoanEntity.builder()
      .name("BASIC_LOAN")
      .currency("USD")
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();
    when(transactionalOperator.transactional(any(Mono.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));
    when(mapper.toEntity(typeLoan)).thenReturn(entity);
    when(mapper.toModel(entity)).thenReturn(typeLoan);
    when(repository.save(any())).thenReturn(Mono.just(entity));

    StepVerifier.create(adapter.save(typeLoan))
      .expectNext(typeLoan)
      .verifyComplete();
  }

  @Test
  void validGetByIdTypeLoan() {
    TypeLoan typeLoan = TypeLoan.builder()
      .id(1L)
      .name("BASIC_LOAN")
      .currency(Currency.getInstance("USD"))
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    TypeLoanEntity entity = TypeLoanEntity.builder()
      .id(1L)
      .name("BASIC_LOAN")
      .currency("USD")
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    when(mapper.toModel(entity)).thenReturn(typeLoan);
    when(repository.findById(1L)).thenReturn(Mono.just(entity));

    StepVerifier.create(adapter.findById(1L))
      .expectNext(typeLoan)
      .verifyComplete();
  }


  @Test
  void validfindByName(){
    TypeLoan typeLoan = TypeLoan.builder()
      .id(1L)
      .name("BASIC_LOAN")
      .currency(Currency.getInstance("USD"))
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    TypeLoanEntity entity = TypeLoanEntity.builder()
      .id(1L)
      .name("BASIC_LOAN")
      .currency("USD")
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    when(mapper.toModel(entity)).thenReturn(typeLoan);
    when(repository.findByName(entity.getName())).thenReturn(Mono.just(entity));

    StepVerifier.create(adapter.findByName(entity.getName()))
      .expectNext(typeLoan)
      .verifyComplete();
  }
}
