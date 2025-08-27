package com.crediya.request.usecase.enums;

import java.math.BigDecimal;

public enum MoneyConstants {
    MIN_AMOUNT(new BigDecimal("1000.00"));
    private final BigDecimal value;

    MoneyConstants(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}