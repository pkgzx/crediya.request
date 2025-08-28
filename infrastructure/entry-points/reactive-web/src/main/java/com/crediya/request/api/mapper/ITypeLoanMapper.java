package com.crediya.request.api.mapper;

  import com.crediya.request.api.dto.CreateTypeLoanDto;
  import com.crediya.request.model.typeloan.TypeLoan;
  import com.crediya.request.usecase.enums.TechnicalMessage;
  import com.crediya.request.usecase.exception.BusinessException;
  import org.mapstruct.Mapper;
  import org.mapstruct.Mapping;

  import java.util.Currency;

  @Mapper(componentModel = "spring")
  public interface ITypeLoanMapper {
    @Mapping(target = "name", expression = "java(dto.name().toUpperCase())")
    @Mapping(target = "minAmount", source = "minAmount")
    @Mapping(target = "maxAmount", source = "maxAmount")
    @Mapping(target = "interestRate", source = "interestRate")
    @Mapping(target = "validationAutomatic", source = "validationAutomatic")
    @Mapping(target = "currency", expression = "java(mapCurrency(dto.currency()))")
    TypeLoan toModel(CreateTypeLoanDto dto);

    default Currency mapCurrency(String currencyCode) {
      if (currencyCode == null || currencyCode.length() != 3) {
        throw new BusinessException(TechnicalMessage.INVALID_CURRENCY);
      }
      return Currency.getInstance(currencyCode);
    }
  }