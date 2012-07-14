package com.goodow.web.core.shared;

public interface WebService<E extends WebEntity> {

  E find(String id);

  Class<E> getEntityClass();

  <T> T invoke(final Wrapper<Operation> operation, final Object... args);

  void remove(final E entity);

  E save(final E entity);
}
