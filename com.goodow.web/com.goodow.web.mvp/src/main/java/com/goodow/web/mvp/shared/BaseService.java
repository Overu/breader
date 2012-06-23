package com.goodow.web.mvp.shared;

import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebService;

import java.util.List;

public interface BaseService<E extends WebEntity> extends WebService<E> {

  long count();

  List<E> find(final int start, final int length);

  void put(final Object domain);

  void remove(final Object domain);
}
