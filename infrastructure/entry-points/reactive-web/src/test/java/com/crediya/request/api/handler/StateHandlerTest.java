package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateStateDto;
import com.crediya.request.api.mapper.IStateMapper;
import com.crediya.request.api.validation.StateValidator;
import com.crediya.request.model.state.State;
import com.crediya.request.usecase.cases.StateUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StateHandlerTest {

    private StateUseCase stateUseCase;
    private IStateMapper stateMapper;
    private StateValidator stateValidator;
    private StateHandler stateHandler;

    @BeforeEach
    void setUp() {
        stateUseCase = mock(StateUseCase.class);
        stateMapper = mock(IStateMapper.class);
        stateValidator = mock(StateValidator.class);
        stateHandler = new StateHandler(stateUseCase, stateMapper, stateValidator);
    }

    @Test
    void listenCreateState_success() {
        CreateStateDto dto = new CreateStateDto("TestName", "TestDescription");
        State state = State.builder().name("TestName").description("TestDescription").build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateStateDto.class)).thenReturn(Mono.just(dto));
        when(stateMapper.toModel(dto)).thenReturn(state);

        when(stateValidator.validName(any())).thenReturn(Mono.empty());
        when(stateValidator.validDescription(any())).thenReturn(Mono.empty());
        when(stateUseCase.createState(state)).thenReturn(Mono.just(state));

        Mono<ServerResponse> responseMono = stateHandler.listenCreateState(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(stateMapper).toModel(dto);
        verify(stateValidator).validName("TestName");
        verify(stateValidator).validDescription("TestDescription");
        verify(stateUseCase).createState(state);
    }


    @Test
    void listenCreateState_invalidDescription() {
        CreateStateDto dto = new CreateStateDto("TestName", "");
        State state = State.builder().name("TestName").description("").build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(CreateStateDto.class)).thenReturn(Mono.just(dto));
        when(stateMapper.toModel(dto)).thenReturn(state);

        when(stateValidator.validName(any())).thenReturn(Mono.empty());
        when(stateValidator.validDescription(any()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.STATE_DESCRIPTION_INVALID)));

        Mono<ServerResponse> responseMono = stateHandler.listenCreateState(request);

        StepVerifier.create(responseMono)
                .expectError(BusinessException.class)
                .verify();

        verify(stateMapper).toModel(dto);
        verify(stateValidator).validName("TestName");
        verify(stateValidator).validDescription("");
        verify(stateUseCase, never()).createState(any());
    }
}