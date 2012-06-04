/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.wave.server.requestfactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import java.lang.reflect.Proxy;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

final class RequestFactoryServiceLayerDecorator extends ServiceLayerDecorator {

  /**
   * JSR 303 validator used to validate requested entities.
   */
  private final Validator validator;
  private final Injector injector;

  private final Provider<EntityManager> em;

  private final AutoBeanFactory beanFactory;

  @Inject
  RequestFactoryServiceLayerDecorator(final Injector injector, final Validator validator,
      final Provider<EntityManager> em, final AutoBeanFactory beanFactory) {
    super();
    this.injector = injector;
    this.validator = validator;
    this.em = em;
    this.beanFactory = beanFactory;
  }

  @Override
  public <T> T createDomainObject(final Class<T> clazz) {
    if (clazz.isInterface()) {
      return beanFactory.create(clazz).as();
    }
    // Class<? extends Locator<?, ?>> locatorType = getTop().resolveLocator(clazz);
    // if (locatorType == null) {
    T domain = injector.getInstance(clazz);
    return domain;
    // }
    // Locator<T, ?> l = (Locator<T, ?>) getTop().createLocator(locatorType);
    // return l.create(clazz);
  }

  @Override
  public <T extends Locator<?, ?>> T createLocator(final Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  @Override
  public Object createServiceInstance(final Class<? extends RequestContext> requestContext) {
    Class<?> serviceClass = getTop().resolveServiceClass(requestContext);
    // Class<? extends ServiceLocator> serviceLocatorClass;
    // if ((serviceLocatorClass = getTop().resolveServiceLocator(requestContext)) == null) {
    Object service = injector.getInstance(serviceClass);
    return service;
    // } else {
    // return injector.getInstance(serviceLocatorClass).getInstance(serviceClass);
    // }
  }

  @Override
  public Object getId(final Object domainObject) {
    return getTop().getProperty(domainObject, "id");
  }

  @Override
  public Class<?> getIdType(final Class<?> domainType) {
    return getTop().getGetter(domainType, "id").getReturnType();
  }

  @Override
  public Object getVersion(final Object domainObject) {
    return getTop().getProperty(domainObject, "version");
  }

  @Override
  public boolean isLive(final Object domainObject) {
    return em.get().contains(domainObject);
  }

  @Override
  public <T> T loadDomainObject(final Class<T> clazz, final Object domainId) {
    // TODO why not getTop().loadDomainObject(clazz, domainId)?
    return em.get().find(clazz, domainId);
  }

  @Override
  public <T> Class<? extends T> resolveClientType(final Class<?> domainClass,
      final Class<T> clientType, final boolean required) {
    if (Proxy.isProxyClass(domainClass)) {
      return clientType;
    }
    return super.resolveClientType(domainClass, clientType, required);
  }

  /**
   * Invokes JSR 303 validator on a given domain object.
   * 
   * @param domainObject the domain object to be validated
   * @param <T> the type of the entity being validated
   * @return the violations associated with the domain object
   */
  @Override
  public <T> Set<ConstraintViolation<T>> validate(final T domainObject) {
    return validator.validate(domainObject);
  }

}
