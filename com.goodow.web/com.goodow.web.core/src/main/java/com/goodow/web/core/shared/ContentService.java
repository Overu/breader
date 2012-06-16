package com.goodow.web.core.shared;

public interface ContentService<E extends Content> extends Service<E> {

  void put(final E content);

  void remove(final E content);
}
