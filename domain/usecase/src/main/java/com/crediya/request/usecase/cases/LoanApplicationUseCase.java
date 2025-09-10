package com.crediya.request.usecase.cases;

  import com.crediya.request.model.criteria.FilterCriteria;
  import com.crediya.request.model.criteria.PagedResult;
  import com.crediya.request.model.criteria.Pagination;
  import com.crediya.request.model.loan_application.LoanApplication;
  import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
  import com.crediya.request.model.notification.LoanApplicationStatusChangedEvent;
  import com.crediya.request.model.notification.spi.INotificationClient;
  import com.crediya.request.usecase.client.IUserClient;
  import com.crediya.request.usecase.enums.TechnicalMessage;
  import com.crediya.request.usecase.exception.BusinessException;
  import reactor.core.publisher.Flux;
  import reactor.core.publisher.Mono;

  import java.util.Objects;

public class LoanApplicationUseCase {
      private final ILoanApplicationRepository repository;
      private final IUserClient userClient;
      private final TypeLoanUseCase typeLoanUseCase;
      private final StateUseCase stateUseCase;
      private final INotificationClient  notificationClient;

      public LoanApplicationUseCase(ILoanApplicationRepository repository, IUserClient userClient, TypeLoanUseCase typeLoanUseCase, StateUseCase stateUseCase, INotificationClient notificationClient) {
          this.repository = repository;
          this.userClient = userClient;
        this.typeLoanUseCase = typeLoanUseCase;
        this.stateUseCase = stateUseCase;
        this.notificationClient = notificationClient;
      }

      public Mono<LoanApplication> create(LoanApplication loanApplication, String subject) {
        return checkOtherEntities(loanApplication)
          .then(Mono.defer(() -> {
            if (!Objects.equals(loanApplication.getUser().email(), subject)) {
              return Mono.error(new BusinessException(TechnicalMessage.UNAUTHORIZED_ACCESS));
            }
            return Mono.just(loanApplication);
          }))
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

  public Mono<PagedResult<LoanApplication>> findAllPaginated(Pagination pagination,
                                                             FilterCriteria filterCriteria) {
    return repository.findAllPaged(pagination, filterCriteria)
      .flatMap(result ->
        Flux.fromIterable(result.getContent())
          .flatMap(loanApplication ->
            typeLoanUseCase.getTypeLoanById(loanApplication.getType().getId())
              .flatMap(typeLoan -> {
                loanApplication.setType(typeLoan);
                return stateUseCase.getStateById(loanApplication.getState().getId())
                  .flatMap(state -> {
                    loanApplication.setState(state);
                    return userClient.getUserById(loanApplication.getUser().id())
                      .map(userDetails -> {
                        loanApplication.setUser(userDetails);
                        return loanApplication;
                      });
                  });
              })
          )
          .collectList()
          .map(enrichedList -> PagedResult.<LoanApplication>builder()
            .content(enrichedList)
            .totalElements(result.getTotalElements())
            .totalPages(result.getTotalPages())
            .currentPage(result.getCurrentPage())
            .pageSize(result.getPageSize())
            .hasNext(result.isHasNext())
            .hasPrevious(result.isHasPrevious())
            .build()
          )
      );


  }

  public Mono<Void> changeStatus(String idLoanApplication, Long idStatus) {
    return repository.findById(idLoanApplication)
      .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.LOAN_APPLICATION_NOT_FOUND)))
      .flatMap(loanApplication ->
        stateUseCase.getStateById(idStatus)
          .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.STATE_NOT_FOUND)))
          .flatMap(state -> {
            loanApplication.setState(state);
            return repository.updateStateById(idLoanApplication, idStatus)
              .then(userClient.getUserById(loanApplication.getUser().id())
                .flatMap(userDetails -> {
                  loanApplication.setUser(userDetails);
                  var event = new LoanApplicationStatusChangedEvent(
                    loanApplication.getId(),
                    userDetails.email(),
                    state.getName(),
                    String.valueOf(System.currentTimeMillis()),
                    loanApplication.getAmount(),
                    userDetails.name(),
                    userDetails.id()
                  );
                  return notificationClient.sendNotification(event);
                })
              );
          })
      );
  }
  }