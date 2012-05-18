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
import com.google.inject.Singleton;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * RfConstraintValidationFactory is a custom JSR 303 ConstraintValidatorFactory that is using
 * Guice's {@link Injector} to create {@link ConstraintValidator}'s and to inject dependencies that
 * are associated by each of the validators.
 * 
 * @author Miroslav Genov (mgenov@gmail.com)
 * @see @see
 *      com.trycatchsoft.gwt.requestfactory.InjectingRequestFactoryModule#getValidatorFactory(com
 *      .google.inject.Injector)
 */
@Singleton
final class RequestFactoryConstraintValidationFactory implements ConstraintValidatorFactory {
  /**
   * The entry point injector that is used to inject different kind of dependencies.
   */
  private final Injector injector;

  /**
   * Creates a new RfConstraintValidationFactory by using the injector.
   * 
   * @param injector the injector that will be used for the injection.
   */
  @Inject
  RequestFactoryConstraintValidationFactory(final Injector injector) {
    this.injector = injector;
  }

  @Override
  public <T extends ConstraintValidator<?, ?>> T getInstance(final Class<T> tClass) {
    return injector.getInstance(tClass);
  }
}