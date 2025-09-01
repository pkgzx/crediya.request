package com.crediya.request.usecase.cases;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.model.typeloan.spi.ITypeLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
 class TypeLoanUseCaseTest {
  @Mock
  private ITypeLoanRepository typeLoanRepository;

  private TypeLoanUseCase typeLoanUseCase;

  @BeforeEach
   void setup() {
    typeLoanUseCase = new TypeLoanUseCase(typeLoanRepository);
  }

  @Test
   void validCreateTypeLoanSuccess() {
    TypeLoan typeLoan = TypeLoan.builder()
      .name("BASIC_LOAN")
      .currency(Currency.getInstance("USD"))
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();

    when(typeLoanRepository.findByName(typeLoan.getName())).thenReturn(Mono.empty());
    when(typeLoanRepository.save(typeLoan)).thenReturn(Mono.just(typeLoan));

    StepVerifier.create(typeLoanUseCase.createTypeLoan(typeLoan))
      .expectNext(typeLoan)
      .verifyComplete();
  }

  @Test
  void validGetTypeLoanSuccess() {
    TypeLoan typeLoan = TypeLoan.builder()
      .id(1L)
      .name("BASIC_LOAN")
      .currency(Currency.getInstance("USD"))
      .maxAmount(BigDecimal.valueOf(10000))
      .minAmount(BigDecimal.valueOf(0))
      .interestRate(5.5)
      .validationAutomatic(true)
      .build();
    when(typeLoanRepository.findById(1L)).thenReturn(Mono.just(typeLoan));

    StepVerifier.create(typeLoanUseCase.getTypeLoanById(typeLoan.getId()))
      .expectNext(typeLoan)
      .verifyComplete();
  }
}
