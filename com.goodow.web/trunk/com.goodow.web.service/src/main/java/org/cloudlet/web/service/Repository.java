package org.cloudlet.web.service;

import com.google.inject.ImplementedBy;

import org.cloudlet.web.service.impl.RepositoryImpl;

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
