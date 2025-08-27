package com.crediya.request.usecase.cases;

import com.crediya.request.model.state.State;
import com.crediya.request.model.state.spi.IStateRepository;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import com.crediya.request.usecase.validation.StateValidor;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class StateUseCase {
  private final IStateRepository stateRepository;

  public StateUseCase(IStateRepository stateRepository) {
    this.stateRepository = stateRepository;
  }
  public Mono<State> createState(State state) {
    return StateValidor.validName(state.getName())
      .then(StateValidor.validDescription(state.getDescription()))
      .then(checkExistName(state))
      .then(stateRepository.save(state));
  }

  public Mono<State> getStateById(Long id) {
    return stateRepository.findById(id);
  }

  private Mono<State> checkExistName(State state){
    return stateRepository.findByName(state.getName().toUpperCase())
      .flatMap(exist -> exist != null
        ? Mono.error(new BusinessException(TechnicalMessage.STATE_NAME_ALREADY_EXIST))
        : Mono.just(state)
      )
      .switchIfEmpty(Mono.just(state));
  }
}
