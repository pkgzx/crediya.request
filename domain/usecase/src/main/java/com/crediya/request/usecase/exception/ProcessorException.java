package com.crediya.request.usecase.exception;


import com.crediya.request.usecase.enums.TechnicalMessage;

public class ProcessorException extends RuntimeException {

  private final TechnicalMessage technicalMessage;

  public ProcessorException(Throwable cause, TechnicalMessage message) {
    super(cause);
    technicalMessage = message;
  }

  public ProcessorException(String message,
                            TechnicalMessage technicalMessage) {
    super(message);
    this.technicalMessage = technicalMessage;
  }

  public TechnicalMessage getTechnicalMessage() {
    return technicalMessage;
  }
}
