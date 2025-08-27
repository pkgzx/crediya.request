package com.crediya.request.r2dbc.repository;

import com.crediya.request.r2dbc.entity.TypeLoanEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeLoanPostgresRepository extends ReactiveCrudRepository<TypeLoanEntity, Long>, ReactiveQueryByExampleExecutor<TypeLoanEntity> {
}
