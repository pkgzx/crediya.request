package com.crediya.request.r2dbc.adapter;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.model.typeloan.spi.ITypeLoanRepository;
import com.crediya.request.r2dbc.mapper.ITypeLoanPersistenceMapper;
import com.crediya.request.r2dbc.repository.ITypeLoanPostgresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TypeLoanPostgresPersistenceAdapter implements ITypeLoanRepository {
  private final ITypeLoanPostgresRepository repository;
  private final ITypeLoanPersistenceMapper mapper;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<TypeLoan> save(TypeLoan typeLoan) {
    return repository.save(mapper.toEntity(typeLoan))
      .map(mapper::toModel)
      .as(transactionalOperator::transactional);
  }

  @Override
  public Mono<TypeLoan> findById(Long id) {
    return repository.findById(id)
      .map(mapper::toModel);
  }

  @Override
  public Mono<TypeLoan> findByName(String name) {
    return repository.findByName(name)
      .map(mapper::toModel);
  }
}
