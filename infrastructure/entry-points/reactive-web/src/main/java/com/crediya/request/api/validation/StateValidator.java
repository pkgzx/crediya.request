package com.crediya.request.api.validation;

import com.crediya.request.usecase.enums.IntegerConstants;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StateValidator {
  private StateValidator() {
  }


  public  Mono<Void> validName(String name) {
    if (name == null || name.isBlank() || name.length() < IntegerConstants.MIN_STATE_NAME_LENGTH.value ||
      name.length() > IntegerConstants.MAX_STATE_NAME_LENGTH.value
    ) {
      return Mono.error(new BusinessException(TechnicalMessage.STATE_NAME_INVALID));
    }
    return Mono.empty();
  }

  public  Mono<Void> validDescription(String description) {
    if (description == null || description.isBlank()) {
      return Mono.error(new BusinessException(TechnicalMessage.STATE_DESCRIPTION_INVALID));
    }
    return Mono.empty();
  }


}
