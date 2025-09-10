package com.crediya.request.r2dbc.repository;

import com.crediya.request.r2dbc.entity.LoanApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ILoanApplicationPostgresRepository extends ReactiveCrudRepository<LoanApplicationEntity,String>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

 @Query("SELECT * FROM \"LoanApplication\" WHERE id = :id LIMIT 1")
 Mono<LoanApplicationEntity> findById(String id);

  @Query("UPDATE \"LoanApplication\" SET id_state = :idState WHERE id = :id")
  Mono<Integer> updateStateById(Long idState, String id);
}
