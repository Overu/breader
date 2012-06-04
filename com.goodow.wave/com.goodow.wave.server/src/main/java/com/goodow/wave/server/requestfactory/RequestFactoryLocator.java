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
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

import java.util.logging.Logger;

public class RequestFactoryLocator extends Locator<Object, Object> implements ServiceLocator {

  private final Logger logger;

  @Inject
  RequestFactoryLocator(final Logger logger) {
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
