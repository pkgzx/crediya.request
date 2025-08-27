package com.crediya.request.api.mapper;

import com.crediya.request.api.dto.CreateStateDto;
import com.crediya.request.model.state.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IStateMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", expression = "java(dto.name().toUpperCase())")
  @Mapping(target = "description", source = "dto.description")
  State toModel(CreateStateDto dto);
}
