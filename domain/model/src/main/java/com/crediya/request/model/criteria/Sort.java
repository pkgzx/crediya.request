package com.crediya.request.model.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public  class Sort {
  private final String field;
  private final Direction direction;
}