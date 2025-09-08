package com.crediya.request.api.handler;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.dto.LoanApplicationDetailsDto;
import com.crediya.request.api.mapper.ILoanApplicationMapper;
import com.crediya.request.api.validation.LoanApplicationValidator;
import com.crediya.request.model.criteria.*;
import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.usecase.cases.LoanApplicationUseCase;
import com.crediya.request.usecase.enums.TechnicalMessage;
import com.crediya.request.usecase.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {
  private final LoanApplicationUseCase loanApplicationUseCase;
  private final ILoanApplicationMapper loanApplicationMapper;
  private final LoanApplicationValidator loanApplicationValidator;

  public Mono<ServerResponse> listenCreateLoan(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(CreateLoanApplicationDto.class)
      .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.REQUEST_BODY_INVALID)))
      .map(loanApplicationMapper::toModel)
      .flatMap(loanApplication -> loanApplicationValidator.validateAmount(loanApplication.getAmount())
        .then(loanApplicationValidator.validateTerm(loanApplication.getTerm()))
        .then(loanApplicationValidator.validateEmail(loanApplication.getUser().email()))
        .then(loanApplicationValidator.validateCurrency(loanApplication.getCurrency().getCurrencyCode()))
        .then(Mono.just(loanApplication))
      )
      .flatMap(loanApplication ->
        ReactiveSecurityContextHolder.getContext()
          .map(ctx -> ctx.getAuthentication().getPrincipal().toString())
          .flatMap(subject ->   loanApplicationUseCase.create(loanApplication, subject))
      )
      .flatMap(savedLoanApplication -> ServerResponse.ok().bodyValue(savedLoanApplication));
  }

  public Mono<ServerResponse> findAllPaginated(ServerRequest serverRequest) {
    Integer page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
    Integer size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(10);
    String sort  = serverRequest.queryParam("sort").map(String::toLowerCase).orElse(null);
    String search = serverRequest.queryParam("search").map(String::toLowerCase).orElse(null);

    List<Sort> sorts = parseSort(sort);
    Pagination pagination = Pagination.builder()
      .page(page)
      .size(size)
      .sorts(sorts)
      .build();

    Map<String, String> filters = new HashMap<>();
    serverRequest.queryParams().forEach((key, values) -> {
      if (!List.of("page", "size", "sort", "search").contains(key)) {
        filters.put(key, values.get(0));
      }
    });

    List<String> searchFields = List.of("term");
    FilterCriteria filterCriteria = FilterCriteria.builder()
      .filters(Map.copyOf(filters))
      .searchTerm(search)
      .searchFields(searchFields)
      .build();

return loanApplicationUseCase.findAllPaginated(pagination, filterCriteria)
  .flatMap(result ->
    Flux.fromIterable(result.getContent())
      .map(loanApplicationMapper::toDetailDto)
      .collectList()
      .map(dtoList -> PagedResult.<LoanApplicationDetailsDto>builder()
        .content(dtoList)
        .totalElements(result.getTotalElements())
        .totalPages(result.getTotalPages())
        .currentPage(result.getCurrentPage())
        .pageSize(result.getPageSize())
        .hasNext(result.isHasNext())
        .hasPrevious(result.isHasPrevious())
        .build()
      )
  )
  .flatMap(dataToReturn -> ServerResponse.ok().bodyValue(dataToReturn));

  }


  private List<Sort> parseSort(String sort) {
    if (sort == null || sort.isEmpty()) {
      return List.of();
    }

    return Stream.of(sort.split(";"))
      .map(s -> {
        String[] parts = s.split(",");
        String field = parts[0];
        Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1])
          ? Direction.DESC
          : Direction.ASC;
        return new Sort(field, direction);
      })
      .toList();
  }
}
