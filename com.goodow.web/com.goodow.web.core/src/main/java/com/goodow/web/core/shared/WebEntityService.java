package com.goodow.web.core.shared;

import java.util.List;

public interface WebEntityService<E extends WebEntity> extends WebService<E> {

  List<E> find(WebEntity container);

  E getById(String id);

  void remove(final E entity);

  E save(final E entity);
}
