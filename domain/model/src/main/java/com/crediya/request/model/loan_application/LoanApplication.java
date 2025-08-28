package com.crediya.request.model.loan_application;
import com.crediya.request.model.state.State;
import com.crediya.request.model.typeloan.TypeLoan;
import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class LoanApplication {
    private String id;
    private BigDecimal amount;
    private Currency currency;
    private Integer term; // in months
    private UserDetails user;
    private TypeLoan type;
    private State state;
}
