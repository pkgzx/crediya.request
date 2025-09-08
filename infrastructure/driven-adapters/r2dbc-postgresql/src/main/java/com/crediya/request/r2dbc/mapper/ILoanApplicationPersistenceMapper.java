package com.crediya.request.r2dbc.mapper;

import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.UserDetails;
import com.crediya.request.model.state.State;
import com.crediya.request.model.typeloan.TypeLoan;
import com.crediya.request.r2dbc.entity.LoanApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class})
public interface ILoanApplicationPersistenceMapper {
    @Mapping(target = "id", expression = "java(model.getId() != null ? model.getId().toString() : UUID.randomUUID().toString())")
    @Mapping(target = "amount", source = "model.amount")
    @Mapping(target = "term", source = "model.term")
    @Mapping(target = "idUser", source = "model.user.id")
    @Mapping(target = "idState", source = "model.state.id")
    @Mapping(target = "idTypeLoan", source = "model.type.id")
    LoanApplicationEntity toEntity(LoanApplication model);

  @Mapping(target = "id", source = "entity.id")
  @Mapping(target = "amount", source = "entity.amount")
  @Mapping(target = "term", source = "entity.term")
  @Mapping(target = "state", expression = "java(mapState(entity.getIdState()))")
  @Mapping(target = "type", expression = "java(mapType(entity.getIdTypeLoan()))")
  @Mapping(target = "user", expression = "java(mapUser(entity.getIdUser()))")
  LoanApplication toModel(LoanApplicationEntity entity);


  default State mapState(Long id, String name, String description) {
    return State.builder()
      .id(id)
      .name(name)
      .description(description)
      .build();
  }

  default TypeLoan mapType(Long id, String name, BigDecimal minAmount, BigDecimal maxAmount, String currency, Double interestRate, Boolean validationAutomatic) {
    return TypeLoan.builder()
      .id(id)
      .name(name)
      .minAmount(minAmount)
      .maxAmount(maxAmount)
      .currency(Currency.getInstance(currency))
      .interestRate(interestRate)
      .validationAutomatic(validationAutomatic)
      .build();
  }



    default UserDetails mapUser(String userId) {
        return new UserDetails(userId, null, null, null, null, null);
    }

    default State mapState(Long stateId) {
        return State.builder().id(stateId).build();
    }

    default TypeLoan mapType(Long typeId) {
        return TypeLoan.builder().id(typeId).build();
    }



}
