package com.crediya.request.api.exception;



import com.crediya.request.api.dto.ErrorDto;
import com.crediya.request.api.util.ErrorBuilder;
import com.crediya.request.usecase.enums.TechnicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import com.crediya.request.usecase.exception.BusinessException;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Component
public class GlobalWebExceptionHandler implements HandlerFilterFunction<ServerResponse, ServerResponse> {

  @Override
  public Mono<ServerResponse> filter(
    @NonNull ServerRequest request,
    @NonNull HandlerFunction<ServerResponse> next) {
    return next.handle(request)
      .onErrorResume(BusinessException.class, ex -> {
        log.error("BusinessException occurred: {}", ex.getMessage());
        return ErrorBuilder.buildErrorResponse(
          HttpStatus.resolve(ex.getTechnicalMessage().getCode()),
          List.of(ErrorDto.builder().message(ex.getMessage()).build()));
      })
      .onErrorResume(DateTimeParseException.class, ex -> {
        log.error("DateTimeParseException occurred: {}", ex.getMessage());
        return ErrorBuilder.buildErrorResponse(
          HttpStatus.BAD_REQUEST,
          List.of(ErrorDto.builder()
            .code(TechnicalMessage.DATE_FORMAT_INVALID.getCode())
            .message(TechnicalMessage.DATE_FORMAT_INVALID.getMessage())
            .build()));
      })
      .onErrorResume(ServerWebInputException.class, ex -> {
        log.error("ServerWebInputException occurred: {}", ex.getMessage());
        return ErrorBuilder.buildErrorResponse(
          HttpStatus.BAD_REQUEST,
          List.of(ErrorDto.builder()
            .code(TechnicalMessage.REQUEST_BODY_INVALID.getCode())
            .message(TechnicalMessage.REQUEST_BODY_INVALID.getMessage())
            .build()));
      })
      .onErrorResume(ex -> {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ErrorBuilder.buildErrorResponse(
          HttpStatus.INTERNAL_SERVER_ERROR,
          List.of(ErrorDto.builder()
            .code(TechnicalMessage.INTERNAL_ERROR.getCode())
            .message(TechnicalMessage.INTERNAL_ERROR.getMessage())
            .build()));
      });
  }
}

