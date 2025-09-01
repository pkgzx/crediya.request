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
    private UserDetails user; // dto: email ->  --amount
                                                // term
                                                // userDetails (null, null, null, "olvadis@gmail.com"
                                                // solictud -> "olvadis@gmail.com" -> authentication
                                                // userDetails(1, "olvadis", 28377373, "....")
                                                // Entity: amount, term, id_user: 1
                                                // term, amount, user: (1, "olvadis", 28377373, "....")
    private TypeLoan type; // dto: type: 1 -> (id:1, name: null)
    private State state; // dto: 1 -> (id:1, name: null)
}
