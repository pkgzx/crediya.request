package com.crediya.request.r2dbc;

import com.crediya.request.model.state.State;
import com.crediya.request.r2dbc.adapter.StatePostgresPersistenceAdapter;
import com.crediya.request.r2dbc.entity.StateEntity;
import com.crediya.request.r2dbc.mapper.IStatePersistenceMapper;
import com.crediya.request.r2dbc.repository.IStatePostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StatePostgresPersistenceAdapterTest {
  private StatePostgresPersistenceAdapter statePostgresPersistenceAdapter;
  @Mock
  private IStatePostgresRepository repository;
  @Mock
  private IStatePersistenceMapper mapper;
  @Mock
  private TransactionalOperator transactionalOperator;

  @BeforeEach
  void setUp() {
    statePostgresPersistenceAdapter = new StatePostgresPersistenceAdapter(repository, mapper, transactionalOperator);

    when(transactionalOperator.transactional(any(Mono.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void testSave() {
    State state = State.builder().id(1L).name("state").build();
    StateEntity stateEntity = StateEntity.builder().id(1L).name("state").build();

    when(mapper.toEntity(state)).thenReturn(stateEntity);

    when(repository.save(stateEntity)).thenReturn(Mono.just(stateEntity));
    when(mapper.toModel(stateEntity)).thenReturn(state);

    StepVerifier.create(statePostgresPersistenceAdapter.save(state))
      .expectNext(state)
      .verifyComplete();
  }


  @Test
  void validGetById() {
    State state = State.builder().id(1L).name("state").build();

    StateEntity stateEntity = StateEntity.builder().id(1L).name("state").build();

    when(mapper.toModel(stateEntity)).thenReturn(state);
    when(repository.findById(1L)).thenReturn(Mono.just(stateEntity));

    StepVerifier.create(statePostgresPersistenceAdapter.findById(1L))
      .expectNext(state)
      .verifyComplete();
  }

  @Test
  void validGetByName() {
    State state = State.builder().name("state").build();
    StateEntity stateEntity = StateEntity.builder().id(1L).name("state").build();

    when(mapper.toModel(stateEntity)).thenReturn(state);
    when(repository.findByName(state.getName())).thenReturn(Mono.just(stateEntity));
    StepVerifier.create(statePostgresPersistenceAdapter.findByName(state.getName()))
      .expectNext(state)
      .verifyComplete();
  }
}
