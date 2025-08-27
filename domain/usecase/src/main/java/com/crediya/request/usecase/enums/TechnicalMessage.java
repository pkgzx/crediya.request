package com.crediya.request.usecase.enums;

public enum TechnicalMessage {

  INTERNAL_ERROR(500,"Something went wrong, please try again", ""),
  REQUEST_BODY_EMPTY(400, "Body can't be empty.", ""),
  INVALID_PARAM(400, "Invalid param", "param"),
  DATE_FORMAT_INVALID(400, "Invalid date format. Expected format: " + StringConstants.DATE_PATTERN.getValue(),
    "birthday"),
  REQUEST_BODY_INVALID(400, "Request body is invalid.", "body"),
  STATE_NAME_INVALID(400, "State name is invalid. must contains almost 2 chars and max 200 chars", "name"),
  STATE_DESCRIPTION_INVALID(400, "State description is invalid. must contains max 255 chars ", "description"),
  STATE_NAME_ALREADY_EXIST(409, "State name already exist.", "name")
  ;


  private final Integer code;
  private final String message;
  private final String param;

  TechnicalMessage(Integer code, String message, String param) {
    this.code = code;
    this.message = message;
    this.param = param;
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getParam() {
    return param;
  }
}