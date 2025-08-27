package com.crediya.request.model.typeloan;
import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TypeLoan {
    private Long id;
    private String name;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Currency currency;
    private Double interestRate; // as a percentage, e.g., 5.5 for 5.5%
    private Boolean validationAutomatic;
}
