package com.crediya.request.api.mapper;

import com.crediya.request.api.dto.CreateLoanApplicationDto;
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
        return new UserDetails(null, null, null, null, email);
    }
}