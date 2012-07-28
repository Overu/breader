package com.goodow.web.core.shared;

public interface WebService<E extends WebObject> {

  Class<E> getObjectType();

  <T> T invoke(final Wrapper<Operation> operation, final Object... args);

}
