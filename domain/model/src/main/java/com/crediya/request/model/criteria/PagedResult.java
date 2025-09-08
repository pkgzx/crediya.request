package com.crediya.request.model.criteria;


import lombok.*;

import java.util.List;


@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PagedResult<T> {
  private final List<T> content;
  private final long totalElements;
  private final int totalPages;
  private final int currentPage;
  private final int pageSize;
  private final boolean hasNext;
  private final boolean hasPrevious;
}