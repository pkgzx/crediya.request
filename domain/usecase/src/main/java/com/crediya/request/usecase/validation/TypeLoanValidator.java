package com.crediya.request.usecase.validation;

import com.crediya.request.usecase.enums.IntegerConstants;
import com.crediya.request.usecase.enums.StringConstants;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class TypeLoanValidator {

  private TypeLoanValidator() {
  }

  public static Mono<Void> validateTypeLoanName(String name) {
    if (name == null || name.isBlank() || name.length() < IntegerConstants.MIN_TYPE_LOAN_NAME_LENGTH.value || name.length() > IntegerConstants.MAX_TYPE_LOAN_NAME_LENGTH.value) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_NAME_INVALID));
    }
    return Mono.empty();
  }

  public static Mono<Void> validateMinimumAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(new BigDecimal(StringConstants.MAX_AMOUNT_TYPE_LOAN.getValue())) > 0) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_MIN_AMOUNT_INVALID));
    }
    return Mono.empty();
  }

  public static Mono<Void> validateMaximumAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(new BigDecimal(StringConstants.MAX_AMOUNT_TYPE_LOAN.getValue())) > 0  || amount.compareTo(BigDecimal.ZERO) <= 0) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_MIN_AMOUNT_INVALID));
    }
    return Mono.empty();
  }



  public static Mono<Void> validateCurrency(String currency) {
    try {
      java.util.Currency.getInstance(currency);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return Mono.error(new BusinessException(TechnicalMessage.INVALID_CURRENCY));
    }
    return Mono.empty();
  }

  public static Mono<Void> validateInterestRate(Double interestRate) {
    if (interestRate == null || interestRate < IntegerConstants.TYPE_LOAN_MIN_RATE.value || interestRate > IntegerConstants.TYPE_LOAN_MAX_RATE.value) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_MIN_AMOUNT_INVALID));
    }
    return Mono.empty();
  }

  public static Mono<Void> validateValidationAutomatic(Boolean validationAutomatic) {
    if (validationAutomatic == null) {
      return Mono.error(new BusinessException(TechnicalMessage.TYPE_LOAN_VALIDATION_AUTOMATIC_INVALID));
    }
    return Mono.empty();
  }
}
