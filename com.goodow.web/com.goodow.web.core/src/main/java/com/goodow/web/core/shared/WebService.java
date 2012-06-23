package com.goodow.web.core.shared;

public interface WebService<E extends WebEntity> {

  E find(String id);

  Class<E> getEntityClass();

  <T> T invoke(final Wrapper<Operation> operation, final Object... args);

  void save(final E entity);

  void remove(final E entity);
}
