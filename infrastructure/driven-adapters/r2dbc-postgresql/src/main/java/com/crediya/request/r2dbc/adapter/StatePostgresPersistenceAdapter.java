package com.crediya.request.r2dbc.adapter;


import com.crediya.request.model.state.State;
import com.crediya.request.model.state.spi.IStateRepository;
import com.crediya.request.r2dbc.mapper.IStatePersistenceMapper;
import com.crediya.request.r2dbc.repository.IStatePostgresRepository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

public record StatePostgresPersistenceAdapter(IStatePostgresRepository repository, IStatePersistenceMapper mapper, TransactionalOperator transactionalOperator) implements IStateRepository {
  @Override
  public Mono<State> save(State state) {
    return repository.save(mapper.toEntity(state))
      .map(mapper::toModel)
      .as(transactionalOperator::transactional);
  }

  @Override
  public Mono<State> findById(Long id) {
    return repository.findById(id)
      .map(mapper::toModel)
      .switchIfEmpty(Mono.empty())
      .as(transactionalOperator::transactional);
  }

  @Override
  public Mono<State> findByName(String name) {
    return repository.findByName(name)
      .map(mapper::toModel)
      .switchIfEmpty(Mono.empty())
      .as(transactionalOperator::transactional);
  }
}
