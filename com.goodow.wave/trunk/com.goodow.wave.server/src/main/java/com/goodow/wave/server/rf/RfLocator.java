package com.goodow.wave.server.rf;

import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import java.util.logging.Logger;

public class RfLocator extends Locator<Object, Object> implements ServiceLocator {

  private final Logger logger;

  @Inject
  RfLocator(final Logger logger) {
    this.logger = logger;
  }

  @Override
  public Object create(final Class<? extends Object> clazz) {
    throw new AssertionError();
  }

  @Override
  public Object find(final Class<? extends Object> clazz, final Object id) {
    throw new AssertionError();
  }

  @Override
  public Class<Object> getDomainType() {
    throw new AssertionError();
  }

  @Override
  public Object getId(final Object domainObject) {
    throw new AssertionError();
  }

  @Override
  public Class<Object> getIdType() {
    throw new AssertionError();
  }

  @Override
  public Object getInstance(final Class<?> clazz) {
    throw new AssertionError();
  }

  @Override
  public Object getVersion(final Object domainObject) {
    throw new AssertionError();
  }
}
