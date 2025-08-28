package com.crediya.request.usecase.enums;

public enum IntegerConstants {
 MIN_STATE_NAME_LENGTH(2),
  MAX_STATE_NAME_LENGTH(200),
  MAX_DESCRIPTION_LENGTH(255),
  MIN_TYPE_LOAN_NAME_LENGTH(2),
  MAX_TYPE_LOAN_NAME_LENGTH(200),
  TYPE_LOAN_MIN_RATE(0),
  TYPE_LOAN_MAX_RATE(100),
  MIN_APPLICATION_TERM(0)
  ;



  public final int value;

  IntegerConstants(int value) {
    this.value = value;
  }
}
