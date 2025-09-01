package com.crediya.request.usecase.cases;

import com.crediya.request.model.state.State;
import com.crediya.request.model.state.spi.IStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
 class StateUseCaseTest {
  @Mock
  private  IStateRepository stateRepository;

  private StateUseCase stateUseCase;

  @BeforeEach
  void setUp() {
    stateUseCase = new StateUseCase(stateRepository);
  }

  @Test
  void validCreateStateSuccess()
  {
    State state = State.builder()
      .name("PENDING")
      .description("initial state")
      .build();

    when(stateRepository.findByName(state.getName())).thenReturn(Mono.empty());
    when(stateRepository.save(state)).thenReturn(Mono.just(state));

    StepVerifier.create(stateUseCase.createState(state))
      .expectNext(state)
      .verifyComplete();
  }

  @Test
  void validateGetByIdStateSuccess()
  {
    State state = State.builder()
      .id(1L)
      .name("PENDING")
      .description("initial state")
      .build();

    when(stateRepository.findById(state.getId())).thenReturn(Mono.just(state));

    StepVerifier.create(stateUseCase.getStateById(state.getId()))
      .expectNext(state)
      .verifyComplete();
  }



}
