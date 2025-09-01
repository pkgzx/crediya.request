package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateStateDto;
import com.crediya.request.api.mapper.IStateMapper;
import com.crediya.request.api.validation.StateValidator;
import com.crediya.request.usecase.cases.StateUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StateHandler {
  private final StateUseCase stateUseCase;
  private final IStateMapper stateMapper;
  private final StateValidator stateValidor;

    public Mono<ServerResponse> listenCreateState(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateStateDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.REQUEST_BODY_INVALID)))
                .map(stateMapper::toModel)
                .flatMap(state -> stateValidor.validName(state.getName())
                  .then(stateValidor.validDescription(state.getDescription())
                  .then(Mono.just(state)))
                )
                .flatMap(stateUseCase::createState)
                .flatMap(state -> ServerResponse.ok().bodyValue(state));
    }
}
