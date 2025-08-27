package com.crediya.request.usecase.enums;

public enum StringConstants {
  DATE_PATTERN("dd/MM/yyyy"),
  TIME_PATTERN("HH:mm"),
  EMAIL_PATTERN("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
  PASSWORD_PATTERN("^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[^\\da-zA-Z\\s])[^\\s]{8,}$"),
  PHONE_PATTERN("^[0-9]{7,15}$"),
  REFERENCE_CURRENCY("COP"),
  IDENTIFICATION_PATTERN("^[a-zA-Z0-9]{1,20}$")
  ;

  private final String value;

  StringConstants(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
