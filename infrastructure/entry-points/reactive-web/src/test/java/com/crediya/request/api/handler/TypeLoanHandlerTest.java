package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateTypeLoanDto;
import com.crediya.request.api.mapper.ITypeLoanMapper;
import com.crediya.request.api.validation.TypeLoanValidator;
import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.usecase.cases.TypeLoanUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TypeLoanHandlerTest {

    private TypeLoanUseCase typeLoanUseCase;
    private ITypeLoanMapper typeLoanMapper;
    private TypeLoanValidator typeLoanValidator;
    private TypeLoanHandler typeLoanHandler;

    @BeforeEach
    void setUp() {
        typeLoanUseCase = mock(TypeLoanUseCase.class);
        typeLoanMapper = mock(ITypeLoanMapper.class);
        typeLoanValidator = mock(TypeLoanValidator.class);
        typeLoanHandler = new TypeLoanHandler(typeLoanUseCase, typeLoanMapper, typeLoanValidator);
    }

    @Test
    void saveTypeLoan_success() {
        CreateTypeLoanDto dto = new CreateTypeLoanDto("BASIC", null, null, null, 5.5, false);
        TypeLoan typeLoan = TypeLoan.builder().name("Test").minAmount(java.math.BigDecimal.ONE)
                .maxAmount(java.math.BigDecimal.TEN).interestRate(5.0)
                .currency(java.util.Currency.getInstance("USD")).validationAutomatic(true).build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateTypeLoanDto.class)).thenReturn(Mono.just(dto));
        when(typeLoanMapper.toModel(dto)).thenReturn(typeLoan);

        when(typeLoanValidator.validateTypeLoanName(any())).thenReturn(Mono.empty());
        when(typeLoanValidator.validateMinimumAmount(any())).thenReturn(Mono.empty());
        when(typeLoanValidator.validateMaximumAmount(any())).thenReturn(Mono.empty());
        when(typeLoanValidator.validateInterestRate(any())).thenReturn(Mono.empty());
        when(typeLoanValidator.validateCurrency(any())).thenReturn(Mono.empty());
        when(typeLoanValidator.validateValidationAutomatic(any())).thenReturn(Mono.empty());

        when(typeLoanUseCase.createTypeLoan(typeLoan)).thenReturn(Mono.just(typeLoan));

        Mono<ServerResponse> responseMono = typeLoanHandler.saveTypeLoan(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(typeLoanMapper).toModel(dto);
        verify(typeLoanUseCase).createTypeLoan(typeLoan);
    }

  @Test
  void saveTypeLoan_validationError() {
      CreateTypeLoanDto dto = new CreateTypeLoanDto("BASIC", null, null, "USD", 5.5, false);
      TypeLoan typeLoan = TypeLoan.builder()
        .name("BASIC")
        .currency(Currency.getInstance("USD"))
        .interestRate(5.5)
        .validationAutomatic(true).build();


      ServerRequest request = mock(ServerRequest.class);
      when(request.bodyToMono(CreateTypeLoanDto.class)).thenReturn(Mono.just(dto));
      when(typeLoanMapper.toModel(dto)).thenReturn(typeLoan);

      when(typeLoanValidator.validateTypeLoanName(any()))
          .thenReturn(Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_NAME_INVALID)));
      when(typeLoanValidator.validateMinimumAmount(any())).thenReturn(Mono.empty());
      when(typeLoanValidator.validateMaximumAmount(any())).thenReturn(Mono.empty());
      when(typeLoanValidator.validateInterestRate(any())).thenReturn(Mono.empty());
      when(typeLoanValidator.validateCurrency(any())).thenReturn(Mono.empty());
      when(typeLoanValidator.validateValidationAutomatic(any())).thenReturn(Mono.empty());

      Mono<ServerResponse> responseMono = typeLoanHandler.saveTypeLoan(request);

      StepVerifier.create(responseMono)
          .expectError(BusinessException.class)
          .verify();

      verify(typeLoanMapper).toModel(dto);
      verify(typeLoanValidator).validateTypeLoanName(any());
      verify(typeLoanUseCase, never()).createTypeLoan(any());
  }
}