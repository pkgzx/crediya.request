package com.crediya.request.r2dbc.helper;

import com.crediya.request.model.criteria.Direction;
import com.crediya.request.model.criteria.FilterCriteria;
import com.crediya.request.model.criteria.Pagination;
import com.crediya.request.model.criteria.Sort;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.sql.OrderByField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
  public static Query buildQuery(Pagination pagination, FilterCriteria filterCriteria) {
    Query query = buildCountQuery(filterCriteria);

    // Aplicar ordenamiento
    query = applySorting(query, pagination);

    // Aplicar paginación
    query = query.offset(pagination.getOffset()).limit(pagination.getSize());

    return query;
  }

  public static Query buildCountQuery(FilterCriteria filterCriteria) {
    Query query = Query.empty();

    Criteria criteria = buildCriteria(filterCriteria);
    if (criteria != null) {
      query = Query.query(criteria);
    }

    return query;
  }

  private static Criteria buildCriteria(FilterCriteria filterCriteria) {
    List<Criteria> criteriaList = new ArrayList<>();

    // Filtros exactos
    for (Map.Entry<String, Object> filter : filterCriteria.getFilters().entrySet()) {
      if (filter.getValue() != null) {
        criteriaList.add(Criteria.where(filter.getKey()).is(filter.getValue()));
      }
    }

    // Búsqueda por texto en múltiples campos
    if (filterCriteria.hasSearch()) {
      List<Criteria> searchCriteria = new ArrayList<>();
      for (String field : filterCriteria.getSearchFields()) {
        searchCriteria.add(Criteria.where(field).like("%" + filterCriteria.getSearchTerm() + "%"));
      }
      if (!searchCriteria.isEmpty()) {
        Criteria searchCriteriaGroup = searchCriteria.get(0);
        for (int i = 1; i < searchCriteria.size(); i++) {
          searchCriteriaGroup = searchCriteriaGroup.or(searchCriteria.get(i));
        }
        criteriaList.add(searchCriteriaGroup);
      }
    }

    if (criteriaList.isEmpty()) {
      return null;
    }

    Criteria result = criteriaList.get(0);
    for (int i = 1; i < criteriaList.size(); i++) {
      result = result.and(criteriaList.get(i));
    }

    return result;
  }

  private static Query applySorting(Query query, Pagination pagination) {
    for (Sort sort : pagination.getSorts()) {
      query = query.sort(org.springframework.data.domain.Sort.by(
        sort.getDirection() == Direction.DESC ?
          org.springframework.data.domain.Sort.Direction.DESC :
          org.springframework.data.domain.Sort.Direction.ASC,
        sort.getField()
      ));
    }
    return query;
  }
}
