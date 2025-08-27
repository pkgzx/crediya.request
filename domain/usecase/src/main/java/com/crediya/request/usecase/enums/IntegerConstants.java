package com.crediya.request.usecase.enums;

public enum IntegerConstants {
 MIN_STATE_NAME_LENGTH(2),
  MAX_STATE_NAME_LENGTH(200),
  MAX_DESCRIPTION_LENGTH(255)
  ;



  public final int value;

  IntegerConstants(int value) {
    this.value = value;
  }
}
