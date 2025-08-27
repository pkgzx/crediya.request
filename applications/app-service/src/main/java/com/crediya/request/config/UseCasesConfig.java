package com.crediya.request.config;

import com.crediya.request.model.state.spi.IStateRepository;
import com.crediya.request.r2dbc.adapter.StatePostgresPersistenceAdapter;
import com.crediya.request.r2dbc.mapper.IStatePersistenceMapper;
import com.crediya.request.r2dbc.repository.IStatePostgresRepository;
import com.crediya.request.usecase.cases.StateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
  private final IStatePostgresRepository statePostgresRepository;
  private final IStatePersistenceMapper statePersistenceMapper;
  private final TransactionalOperator transactionalOperator;

  @Bean
  public StatePostgresPersistenceAdapter getStatePostgresRepository() {
    return new StatePostgresPersistenceAdapter(statePostgresRepository, statePersistenceMapper, transactionalOperator);
  }

  @Bean
  public StateUseCase getStateUseCase(IStateRepository stateRepository) {
    return new StateUseCase(stateRepository);
  }
}
