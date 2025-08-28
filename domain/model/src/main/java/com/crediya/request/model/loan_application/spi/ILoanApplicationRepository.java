package com.crediya.request.model.loan_application.spi;

import com.crediya.request.model.loan_application.LoanApplication;
import reactor.core.publisher.Mono;

public interface ILoanApplicationRepository {
  Mono<LoanApplication> save(LoanApplication loanApplication);
}
