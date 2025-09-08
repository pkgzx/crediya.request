package com.crediya.request.r2dbc.adapter;

import com.crediya.request.model.criteria.FilterCriteria;
import com.crediya.request.model.criteria.PagedResult;
import com.crediya.request.model.criteria.Pagination;
import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.spi.ILoanApplicationRepository;
import com.crediya.request.r2dbc.entity.LoanApplicationEntity;
import com.crediya.request.r2dbc.helper.QueryBuilder;
import com.crediya.request.r2dbc.mapper.ILoanApplicationPersistenceMapper;
import com.crediya.request.r2dbc.repository.ILoanApplicationPostgresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class LoanApplicationPostgresPersistenceAdapter implements ILoanApplicationRepository {
  private final R2dbcEntityTemplate template;
  private final ILoanApplicationPostgresRepository repository;
  private final ILoanApplicationPersistenceMapper mapper;
  private final TransactionalOperator transactionalOperator;

  @Override
  public Mono<LoanApplication> save(LoanApplication loanApplication) {
    return repository.save(mapper.toEntity(loanApplication))
      .doOnNext(e -> log.info("Loan Application saved with id {}", e.getId()))
      .map(mapper::toModel)
      .as(transactionalOperator::transactional);
  }

@Override
public Mono<PagedResult<LoanApplication>> findAllPaged(Pagination pagination, FilterCriteria filterCriteria) {
  // Copia mutable de los filtros
  var mutableFilters = new java.util.HashMap<>(filterCriteria.getFilters());
  if (mutableFilters.containsKey("status")) {
    mutableFilters.put("id_state", Integer.parseInt((String) mutableFilters.get("status")));
    mutableFilters.remove("status");
  }
  // Crea un nuevo FilterCriteria con los filtros mutados
  FilterCriteria mutableCriteria = FilterCriteria.builder()
    .filters(mutableFilters)
    .searchTerm(filterCriteria.getSearchTerm())
    .searchFields(filterCriteria.getSearchFields())
    .build();

  Query query = QueryBuilder.buildQuery(pagination, mutableCriteria);
  Query countQuery = QueryBuilder.buildCountQuery(mutableCriteria);

  Mono<Long> totalCount = template.count(countQuery, LoanApplicationEntity.class);
  Flux<LoanApplicationEntity> entities = template.select(query, LoanApplicationEntity.class);
  Flux<LoanApplication> loanApplications = entities.map(mapper::toModel);

  return totalCount.flatMap(count ->
    loanApplications.collectList()
      .map(loanApplicationslist -> PagedResult.<LoanApplication>builder()
        .content(loanApplicationslist)
        .totalElements(count)
        .totalPages((int) Math.ceil((double) count / pagination.getSize()))
        .currentPage(pagination.getPage())
        .pageSize(pagination.getSize())
        .hasNext((long) (pagination.getPage() + 1) * pagination.getSize() < count)
        .hasPrevious(pagination.getPage() > 0)
        .build())
  );
}
  @Override
  public Mono<Long> count(FilterCriteria filterCriteria) {
    return Mono.just(QueryBuilder.buildCountQuery(filterCriteria))
      .flatMap(query -> template.count(query, LoanApplicationEntity.class));
  }
}
