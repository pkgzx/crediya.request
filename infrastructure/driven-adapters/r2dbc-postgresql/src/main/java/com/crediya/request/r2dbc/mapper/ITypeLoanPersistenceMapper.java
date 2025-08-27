package com.crediya.request.r2dbc.mapper;

import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.r2dbc.entity.TypeLoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITypeLoanPersistenceMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "currency", expression = "java(entity.getCurrency() != null ? java.util.Currency.getInstance(entity.getCurrency()) : null)")
  @Mapping(target = "validationAutomatic", source = "validationAutomatic")
  @Mapping(target = "interestRate", source = "interestRate")
  @Mapping(target = "maxAmount", source = "maxAmount")
  @Mapping(target = "minAmount", source = "minAmount")
  @Mapping(target = "name", source = "name")
  TypeLoan toModel(TypeLoanEntity entity);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "currency", expression = "java(model.getCurrency() != null ? model.getCurrency().getCurrencyCode() : null)")
  @Mapping(target = "validationAutomatic", source = "validationAutomatic")
  @Mapping(target = "interestRate", source = "interestRate")
  @Mapping(target = "maxAmount", source = "maxAmount")
  @Mapping(target = "minAmount", source = "minAmount")
  @Mapping(target = "name", source = "name")
  TypeLoanEntity toEntity(TypeLoan model);
}
