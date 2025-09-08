package com.crediya.request.model.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Pagination {
  private final int page;
  private final int size;
  private final List<Sort> sorts;

  public long getOffset() { return (long) page * size; }
}