package com.goodow.web.core.shared;

public interface ContentService<E extends WebEntity> extends Service<E> {

  void put(final E content);

  void remove(final E content);
}
