package com.crediya.request.usecase.cases;

import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.UserDetails;
import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
import com.crediya.request.model.state.State;
import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.usecase.client.IUserClient;
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

@ExtendWith(MockitoExtension.class)
 class LoanApplicationUseCaseTest {
  @Mock
  private ILoanApplicationRepository loanApplicationRepository;
  private LoanApplicationUseCase loanApplicationUseCase;
  @Mock
  private  IUserClient userClient;
  @Mock
  private  TypeLoanUseCase typeLoanUseCase;
  @Mock
  private  StateUseCase stateUseCase;

  @BeforeEach
  void setUp() {
    loanApplicationUseCase = new LoanApplicationUseCase(loanApplicationRepository, userClient, typeLoanUseCase, stateUseCase);
  }

  @Test
  void validLoanApplicationCreateSuccess(){
    UserDetails user = new UserDetails("ABC", "Antonio", "Hernandez", "12744", "antonio@gmail.com");

    TypeLoan type = TypeLoan.builder().id(1L).build();
    State state = State.builder().id(1L).build();
    LoanApplication loanApplication = LoanApplication
      .builder()
      .amount(BigDecimal.valueOf(2000))
      .currency(Currency.getInstance("USD"))
      .term(6)
      .state(state)
      .type(type)
      .user(user)
      .build();

    when(userClient.getUserByEmail(user.email())).thenReturn(Mono.just(user));
    when(loanApplicationRepository.save(loanApplication)).thenReturn(Mono.just(loanApplication));
    when(stateUseCase.getStateById(loanApplication.getState().getId())).thenReturn(Mono.just(state));
    when(typeLoanUseCase.getTypeLoanById(loanApplication.getType().getId())).thenReturn(Mono.just(type));


    StepVerifier.create(loanApplicationUseCase.create(loanApplication)).
      expectNext(loanApplication)
      .verifyComplete();

  }



}
