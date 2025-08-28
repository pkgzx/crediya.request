package com.crediya.request.model.typeloan.spi;

import com.crediya.request.model.typeloan.TypeLoan;
import reactor.core.publisher.Mono;

public interface ITypeLoanRepository {
  Mono<TypeLoan> save(TypeLoan typeLoan);
  Mono<TypeLoan> findById(Long id);
  Mono<TypeLoan> findByName(String name);
}
