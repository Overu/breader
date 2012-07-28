package com.goodow.web.core.shared;

import java.util.List;

public interface WebEntityService<E extends WebEntity> extends WebService<E> {

  E getById(String id);

  List<E> find(WebEntity container);

  void remove(final E entity);

  E save(final E entity);
}
