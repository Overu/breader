package com.goodow.web.mvp.shared;

import com.goodow.web.core.shared.WebContentService;
import com.goodow.web.core.shared.WebContent;

import java.util.List;

public interface BaseService<E extends WebContent> extends WebContentService<E> {

  long count();

  List<E> find(final int start, final int length);

  void put(final Object domain);

  void remove(final Object domain);
}
