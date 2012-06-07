package com.goodow.web.core.shared;

public interface Service<E extends Entity> {

  <T> T find(Class<T> clazz, String id);

  <T2> T2 invoke(final Wrapper<Operation> operation, final Object... args);
}
