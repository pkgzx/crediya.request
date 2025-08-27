package com.crediya.request.usecase.validation;

import com.crediya.request.usecase.enums.IntegerConstants;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import reactor.core.publisher.Mono;

public class StateValidor {
  private StateValidor() {
  }


  public static Mono<Void> validName(String name) {
    if (name == null || name.isBlank() || name.length() < IntegerConstants.MIN_STATE_NAME_LENGTH.value ||
      name.length() > IntegerConstants.MAX_STATE_NAME_LENGTH.value
    ) {
      return Mono.error(new BusinessException(TechnicalMessage.STATE_NAME_INVALID));
    }
    return Mono.empty();
  }

  public static Mono<Void> validDescription(String description) {
    if (description == null || description.isBlank()) {
      return Mono.error(new BusinessException(TechnicalMessage.STATE_DESCRIPTION_INVALID));
    }
    return Mono.empty();
  }


}
