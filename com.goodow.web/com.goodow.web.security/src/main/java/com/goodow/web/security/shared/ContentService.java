package com.goodow.web.security.shared;

import com.goodow.web.core.shared.Service;

import java.util.List;

public interface ContentService<E extends Content> extends Service<E> {

  long count();

  List<E> find(final int start, final int length);

  void put(final E domain);

  void put(final Object domain);

  void remove(final E domain);

  void remove(final Object domain);
}
