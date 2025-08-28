package com.crediya.request.api.dto;


import java.math.BigDecimal;

public record CreateLoanApplicationDto(
   BigDecimal amount,
   String currency,
   Integer term, // in months
   String email,
   Long type
) {
}
