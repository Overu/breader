package com.goodow.web.core.shared;

import java.util.List;

public interface WebContentService<E extends WebContent> extends WebService<E> {

  List<E> find(WebContent container);

  E getById(String id);

  void remove(final E entity);

  E save(final E entity);
}
