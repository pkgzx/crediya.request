package com.crediya.request.usecase.cases;

  import com.crediya.request.model.loan_application.LoanApplication;
  import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
  import com.crediya.request.usecase.client.IUserClient;
  import reactor.core.publisher.Mono;

  public class LoanApplicationUseCase {
      private final ILoanApplicationRepository repository;
      private final IUserClient userClient;
      private final TypeLoanUseCase typeLoanUseCase;
      private final StateUseCase stateUseCase;

      public LoanApplicationUseCase(ILoanApplicationRepository repository, IUserClient userClient, TypeLoanUseCase typeLoanUseCase, StateUseCase stateUseCase) {
          this.repository = repository;
          this.userClient = userClient;
        this.typeLoanUseCase = typeLoanUseCase;
        this.stateUseCase = stateUseCase;
      }

      public Mono<LoanApplication> create(LoanApplication loanApplication) {
        return checkOtherEntities(loanApplication)
             .then(userClient.getUserByEmail(loanApplication.getUser().email())
                  .flatMap(user -> {
                      loanApplication.setUser(user);
                      return repository.save(loanApplication)
                        .map(savedLoanApplication -> {
                            savedLoanApplication.setUser(user);
                            return savedLoanApplication;
                        });
                  })
                  .flatMap(savedLoanApplication -> typeLoanUseCase.getTypeLoanById(savedLoanApplication.getType().getId())
                  .flatMap(typeLoan -> {
                      savedLoanApplication.setType(typeLoan);
                      return stateUseCase.getStateById(savedLoanApplication.getState().getId())
                              .flatMap(state -> {
                                  savedLoanApplication.setState(state);
                                  return Mono.just(savedLoanApplication);
                              });
                  })));
      }

    private Mono<Void> checkOtherEntities(LoanApplication loanApplication) {
      return typeLoanUseCase.getTypeLoanById(loanApplication.getType().getId())
        .then(stateUseCase.getStateById(loanApplication.getState().getId()))
        .then();
    }
  }