package com.crediya.request.r2dbc.repository;

import com.crediya.request.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ILoanApplicationPostgresRepository extends ReactiveCrudRepository<LoanApplicationEntity,String>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {
}
