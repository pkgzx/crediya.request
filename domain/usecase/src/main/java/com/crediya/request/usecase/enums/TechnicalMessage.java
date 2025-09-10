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
  STATE_NAME_ALREADY_EXIST(409, "State name already exist.", "name"),
  TYPE_LOAN_NAME_INVALID(400, "Type loan name is invalid. must contains almost 2 chars and max 200 chars", "name"),
  TYPE_LOAN_MIN_AMOUNT_INVALID(400, "Type loan minimum amount is invalid. must be greater than 0", "minAmount"),
  INVALID_CURRENCY(400, "Invalid currency. Must be a valid ISO 4217 currency code.", "baseSalary.currency"),
  TYPE_LOAN_VALIDATION_AUTOMATIC_INVALID(400, "Type loan validation automatic is invalid. must be true or false", "validationAutomatic"),
  TYPE_LOAN_ALREADY_EXIST(409, "Type loan name already exist.", "name"),
  EMAIL_USER_NOT_FOUND(404, "Email user not found.", "email"),
  CLIENT_ERROR(400, "Client error occurred when communicating with external service.", "clientError"),
  SERVER_ERROR(500, "Server error occurred in external service.", "serverError"),
  TYPE_LOAN_TERM_INVALID(400, "Type loan term is invalid. must be greater than 0", "term"),
  INVALID_EMAIL_FORMAT(400, "Invalid email format.", "email"),
  TYPE_LOAN_NOT_FOUND(404, "Type loan not found.", "id"),
  STATE_NOT_FOUND(404, "State not found.", "id"),
  UNAUTHORIZED_ACCESS(401, "Unauthorized Access", ""),
  PAGE_OR_SIZE_TOO_LARGE(400, "Page or size over range", ""),
  LOAN_APPLICATION_NOT_FOUND(404, "Loan application not found.", "id"),
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