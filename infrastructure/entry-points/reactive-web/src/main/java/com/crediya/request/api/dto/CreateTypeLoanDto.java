package com.crediya.request.api.dto;

import java.math.BigDecimal;

public record CreateTypeLoanDto(
    String name,
    BigDecimal minAmount,
    BigDecimal maxAmount,
    String currency,
    Double interestRate,
    Boolean validationAutomatic
) {
}

