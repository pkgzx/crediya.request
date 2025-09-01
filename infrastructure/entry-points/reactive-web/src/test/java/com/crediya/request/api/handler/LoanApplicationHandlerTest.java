package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.mapper.ILoanApplicationMapper;
import com.crediya.request.api.validation.LoanApplicationValidator;
import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.UserDetails;
import com.crediya.request.usecase.cases.LoanApplicationUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanApplicationHandlerTest {

    private LoanApplicationUseCase loanApplicationUseCase;
    private ILoanApplicationMapper loanApplicationMapper;
    private LoanApplicationValidator loanApplicationValidator;
    private LoanApplicationHandler loanApplicationHandler;

    @BeforeEach
    void setUp() {
        loanApplicationUseCase = mock(LoanApplicationUseCase.class);
        loanApplicationMapper = mock(ILoanApplicationMapper.class);
        loanApplicationValidator = mock(LoanApplicationValidator.class);
        loanApplicationHandler = new LoanApplicationHandler(loanApplicationUseCase, loanApplicationMapper, loanApplicationValidator);
    }

    @Test
    void listenCreateLoan_success() {
        CreateLoanApplicationDto dto = new CreateLoanApplicationDto(
                BigDecimal.valueOf(1000),
                "USD",
                5,
                "user@email.com",
          1L
        );
        UserDetails user = new UserDetails("ABC", "Antonio", "Roa", "12376363", "user@email.com");
        LoanApplication loanApplication = LoanApplication.builder()
                .amount(BigDecimal.valueOf(1000))
                .term(12)
                .user(user)
                .currency(Currency.getInstance("USD"))
                .build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateLoanApplicationDto.class)).thenReturn(Mono.just(dto));
        when(loanApplicationMapper.toModel(dto)).thenReturn(loanApplication);

        when(loanApplicationValidator.validateAmount(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateTerm(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateEmail(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateCurrency(any())).thenReturn(Mono.empty());

        when(loanApplicationUseCase.create(loanApplication)).thenReturn(Mono.just(loanApplication));

        Mono<ServerResponse> responseMono = loanApplicationHandler.listenCreateLoan(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(loanApplicationMapper).toModel(dto);
        verify(loanApplicationValidator).validateAmount(BigDecimal.valueOf(1000));
        verify(loanApplicationValidator).validateTerm(12);
        verify(loanApplicationValidator).validateEmail("user@email.com");
        verify(loanApplicationValidator).validateCurrency("USD");
        verify(loanApplicationUseCase).create(loanApplication);
    }

    @Test
    void listenCreateLoan_invalidAmount() {
      CreateLoanApplicationDto dto = new CreateLoanApplicationDto(
        BigDecimal.valueOf(1000),
        "USD",
        5,
        "user@email.com",
        1L
      );
      UserDetails user = new UserDetails("ABC", "Antonio", "Roa", "12376363", "user@email.com");
        LoanApplication loanApplication = LoanApplication.builder()
                .amount(BigDecimal.valueOf(0))
                .term(12)
                .user(user)
                .currency(Currency.getInstance("USD"))
                .build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateLoanApplicationDto.class)).thenReturn(Mono.just(dto));
        when(loanApplicationMapper.toModel(dto)).thenReturn(loanApplication);

        when(loanApplicationValidator.validateAmount(any()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_MIN_AMOUNT_INVALID)));
        when(loanApplicationValidator.validateTerm(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateEmail(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateCurrency(any())).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = loanApplicationHandler.listenCreateLoan(request);

        StepVerifier.create(responseMono)
                .expectError(BusinessException.class)
                .verify();

        verify(loanApplicationMapper).toModel(dto);
        verify(loanApplicationValidator).validateAmount(BigDecimal.valueOf(0));
        verify(loanApplicationUseCase, never()).create(any());
    }

    @Test
    void listenCreateLoan_invalidTerm() {
      CreateLoanApplicationDto dto = new CreateLoanApplicationDto(
        BigDecimal.valueOf(1000),
        "USD",
        5,
        "user@email.com",
        1L
      );
      UserDetails user = new UserDetails("ABC", "Antonio", "Roa", "12376363", "user@email.com");
        LoanApplication loanApplication = LoanApplication.builder()
                .amount(BigDecimal.valueOf(1000))
                .term(0)
                .user(user)
                .currency(Currency.getInstance("USD"))
                .build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateLoanApplicationDto.class)).thenReturn(Mono.just(dto));
        when(loanApplicationMapper.toModel(dto)).thenReturn(loanApplication);

        when(loanApplicationValidator.validateAmount(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateTerm(any()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_TERM_INVALID)));
        when(loanApplicationValidator.validateEmail(any())).thenReturn(Mono.empty());
        when(loanApplicationValidator.validateCurrency(any())).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = loanApplicationHandler.listenCreateLoan(request);

        StepVerifier.create(responseMono)
                .expectError(BusinessException.class)
                .verify();

        verify(loanApplicationMapper).toModel(dto);
        verify(loanApplicationValidator).validateTerm(0);
        verify(loanApplicationUseCase, never()).create(any());
    }


}