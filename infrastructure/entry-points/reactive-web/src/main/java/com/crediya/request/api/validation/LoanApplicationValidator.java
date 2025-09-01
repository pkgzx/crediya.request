package com.crediya.request.api.validation;

import com.crediya.request.usecase.enums.IntegerConstants;
import com.crediya.request.usecase.enums.StringConstants;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class LoanApplicationValidator {
  public  Mono<Void> validateAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_MIN_AMOUNT_INVALID));
    }
    return Mono.empty();
  }

  public  Mono<Void> validateTerm(Integer term) {
    if (term == null || term <= IntegerConstants.MIN_APPLICATION_TERM.value) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_TERM_INVALID));
    }
    return Mono.empty();
  }

  public  Mono<Void> validateEmail(String email) {
    if (email == null || !email.matches(StringConstants.EMAIL_PATTERN.getValue())) {
      return Mono.error(new BusinessException(TechnicalMessage.INVALID_EMAIL_FORMAT));
    }
    return Mono.empty();
  }


  public  Mono<Void> validateCurrency(String currency) {
    try {
      java.util.Currency.getInstance(currency);
    } catch (IllegalArgumentException e) {
      return Mono.error(new BusinessException(TechnicalMessage.INVALID_CURRENCY));
    }
    return Mono.empty();
  }
}
