package com.goodow.web.core.shared;

public interface Service {

  <T> T find(Class<T> clazz, String id);

  <T2> T2 invoke(final Operation operation, final Object... args);
}
