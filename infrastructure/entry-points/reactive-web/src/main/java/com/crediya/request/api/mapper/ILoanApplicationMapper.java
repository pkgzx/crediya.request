package com.crediya.request.api.mapper;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
import com.crediya.request.api.dto.LoanApplicationDetailsDto;
import com.crediya.request.model.loan_application.LoanApplication;
import com.crediya.request.model.loan_application.UserDetails;
import com.crediya.request.model.state.State;
import com.crediya.request.model.typeloan.TypeLoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ILoanApplicationMapper {

    @Mapping(target = "type", expression = "java(mapType(dto.type()))")
    @Mapping(target = "state", expression = "java(defaultState())")
    @Mapping(target = "user", expression = "java(mapUser(dto.email()))")
    LoanApplication toModel(CreateLoanApplicationDto dto);

    default TypeLoan mapType(Long typeId) {
        return TypeLoan.builder().id(typeId).build();
    }

    default State defaultState() {
        return State.builder().id(1L).build(); // ID del estado predeterminado
    }

    default UserDetails mapUser(String email) {
        return new UserDetails(null, null, null, null, email, null);
    }

    @Mapping(target  = "type", source = "model.type.name")
    @Mapping(target  = "interestRate", source = "model.type.interestRate")
    @Mapping(target  = "state", source = "state.name")
    @Mapping(target  = "name", source = "user.name")
    @Mapping(target  = "email", source = "user.email")
    @Mapping(target = "baseSalary", source= "user.baseSalary.value")
    @Mapping(target = "monthlyFee", expression = "java(java.math.BigDecimal.ZERO)")
    LoanApplicationDetailsDto toDetailDto(LoanApplication model);
}

