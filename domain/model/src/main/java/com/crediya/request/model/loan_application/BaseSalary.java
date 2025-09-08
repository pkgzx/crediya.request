package com.crediya.request.model.loan_application;

import java.math.BigDecimal;

public record BaseSalary(
  String currency,
  BigDecimal value
) {
}
