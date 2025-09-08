package com.crediya.request.model.loan_application.spi;

import com.crediya.request.model.criteria.FilterCriteria;
import com.crediya.request.model.criteria.PagedResult;
import com.crediya.request.model.criteria.Pagination;
import com.crediya.request.model.loan_application.LoanApplication;
import reactor.core.publisher.Mono;

public interface ILoanApplicationRepository {
  Mono<LoanApplication> save(LoanApplication loanApplication);
  Mono<PagedResult<LoanApplication>> findAllPaged(Pagination pagination, FilterCriteria filterCriteria);
   Mono<Long> count(FilterCriteria filterCriteria);
}
