package com.crediya.request.model.state.spi;

import com.crediya.request.model.state.State;
import reactor.core.publisher.Mono;

public interface IStateRepository {
  Mono<State> save(State state);
  Mono<State> findById(Long id);
  Mono<State> findByName(String name);
}
