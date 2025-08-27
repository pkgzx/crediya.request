package com.crediya.request.r2dbc.mapper;

import com.crediya.request.model.state.State;
import com.crediya.request.r2dbc.entity.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IStatePersistenceMapper {
  @Mapping(target = "id", source = "entity.id")
  @Mapping(target = "name", source = "entity.name")
  @Mapping(target = "description", source = "entity.description")
  State toModel(StateEntity entity);

  @Mapping(target = "id", source = "state.id")
  @Mapping(target = "name", source = "state.name")
  @Mapping(target = "description", source = "state.description")
  StateEntity toEntity(State state);
}
