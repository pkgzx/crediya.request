package com.crediya.request.usecase.cases;

import com.crediya.request.model.state.State;
import com.crediya.request.model.state.spi.IStateRepository;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import reactor.core.publisher.Mono;


public class StateUseCase {
  private final IStateRepository stateRepository;

  public StateUseCase(IStateRepository stateRepository) {
    this.stateRepository = stateRepository;
  }

  public Mono<State> createState(State state) {
    return checkExistName(state)
      .then(stateRepository.save(state));
  }

  public Mono<State> getStateById(Long id) {
    return stateRepository.findById(id)
      .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.STATE_NOT_FOUND)));
  }

  private Mono<Void> checkExistName(State state) {
    return stateRepository.findByName(state.getName().toUpperCase())
      .flatMap(found -> Mono.error(new BusinessException(TechnicalMessage.STATE_NAME_ALREADY_EXIST)).cast(Void.class))
      .switchIfEmpty(Mono.empty());
  }
}
