package com.crediya.request.model.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FilterCriteria {
  private final Map<String, Object> filters;
  private final List<String> searchFields;
  private final String searchTerm;

  public boolean hasSearch() {
    return searchTerm != null && !searchTerm.trim().isEmpty() && !searchFields.isEmpty();
  }
}
