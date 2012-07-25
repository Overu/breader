package com.goodow.web.core.shared;

import java.util.List;

public interface WebService<E extends WebEntity> {

  E find(String id);

  List<E> find(WebEntity container);

  Class<E> getEntityClass();

  <T> T invoke(final Wrapper<Operation> operation, final Object... args);

  void remove(final E entity);

  E save(final E entity);
}
