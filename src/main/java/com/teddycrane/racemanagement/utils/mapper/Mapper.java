package com.teddycrane.racemanagement.utils.mapper;

import java.util.Collection;

public abstract class Mapper<E, D> {
  abstract D convertEntityToDTO(E entity);

  abstract Collection<D> convertEntityListToDTOList(Collection<E> entityList);

  abstract E convertDTOToEntity(D dto);

  abstract Collection<E> convertDTOListToEntityList(Collection<D> dtoList);
}
