package com.goodow.web.service;

import com.goodow.web.service.impl.RepositoryImpl;

import com.google.inject.ImplementedBy;


@ImplementedBy(RepositoryImpl.class)
public interface Repository {

  <T> T create(Class<T> clazz);

  Content find(String uri);

  <T> T findById(Class<T> clazz, Object id);

  Content findRoot();

  String getUri(Object object);

  void save();

  void save(Object object);
}
