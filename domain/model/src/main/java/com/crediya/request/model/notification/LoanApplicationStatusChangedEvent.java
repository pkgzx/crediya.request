package com.crediya.request.model.notification;

import java.math.BigDecimal;

public record LoanApplicationStatusChangedEvent(
  String requestId,
  String applicantEmail,
  String status,
  String updatedAt,
  BigDecimal amount,
  String applicantName,
  String applicantId
) {
}
