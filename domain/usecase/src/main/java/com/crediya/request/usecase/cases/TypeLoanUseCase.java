package com.crediya.request.usecase.cases;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.model.typeloan.spi.ITypeLoanRepository;
import reactor.core.publisher.Mono;

public class TypeLoanUseCase {
  private final ITypeLoanRepository typeLoanRepository;

  public TypeLoanUseCase(ITypeLoanRepository typeLoanRepository) {
    this.typeLoanRepository = typeLoanRepository;
  }

  public Mono<TypeLoan> getTypeLoanById(Long id) {
    return typeLoanRepository.findById(id);
  }

  public Mono<TypeLoan> createTypeLoan(TypeLoan typeLoan) {
    return typeLoanRepository.save(typeLoan);
  }
}
