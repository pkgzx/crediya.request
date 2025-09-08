package com.crediya.request.api.dto;

import java.math.BigDecimal;

public record LoanApplicationDetailsDto(

    String id,
    BigDecimal amount,
    String currency,
    String type,
    Double interestRate,
    String state,
    String name,
    String email,
    Integer term,
    BigDecimal baseSalary
) {
}
