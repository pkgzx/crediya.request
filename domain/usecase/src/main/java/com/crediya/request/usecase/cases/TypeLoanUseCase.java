package com.crediya.request.usecase.cases;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.model.typeloan.spi.ITypeLoanRepository;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import com.crediya.request.usecase.validation.TypeLoanValidator;
import reactor.core.publisher.Mono;

public class TypeLoanUseCase {
  private final ITypeLoanRepository typeLoanRepository;

  public TypeLoanUseCase(ITypeLoanRepository typeLoanRepository) {
    this.typeLoanRepository = typeLoanRepository;
  }

  public Mono<TypeLoan> getTypeLoanById(Long id) {
    return typeLoanRepository.findById(id)
        .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_NOT_FOUND)));
  }

  public Mono<TypeLoan> createTypeLoan(TypeLoan typeLoan) {
    return TypeLoanValidator.validateTypeLoanName(typeLoan.getName())
        .then(TypeLoanValidator.validateMinimumAmount(typeLoan.getMinAmount()))
        .then(TypeLoanValidator.validateMaximumAmount(typeLoan.getMaxAmount()))
        .then(TypeLoanValidator.validateInterestRate(typeLoan.getInterestRate()))
        .then(TypeLoanValidator.validateCurrency(typeLoan.getCurrency().getCurrencyCode()))
        .then(TypeLoanValidator.validateValidationAutomatic(typeLoan.getValidationAutomatic()))
        .then(checkIfTypeLoanExists(typeLoan.getName()))
        .then(typeLoanRepository.save(typeLoan));
  }

  private Mono<Void> checkIfTypeLoanExists(String name) {
    return typeLoanRepository.findByName(name)
        .flatMap(existingTypeLoan -> Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_ALREADY_EXIST)))
        .then();
  }
}
