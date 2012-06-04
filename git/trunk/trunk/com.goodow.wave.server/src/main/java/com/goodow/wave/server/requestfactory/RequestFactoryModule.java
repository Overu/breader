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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class RequestFactoryModule extends AbstractModule {
  @AutoBeanFactory.Category(value = {
      EntityProxyCategory.class, ValueProxyCategory.class, BaseProxyCategory.class})
  @AutoBeanFactory.NoWrap(EntityProxyId.class)
  interface Factory extends AutoBeanFactory {
  }

  @Override
  protected void configure() {
    bind(ExceptionHandler.class).to(RequestFactoryExceptionHandler.class);
    bind(ServiceLayerDecorator.class).to(RequestFactoryServiceLayerDecorator.class);
    bind(ConstraintValidatorFactory.class).to(RequestFactoryConstraintValidationFactory.class);
  }

  @Provides
  @Singleton
  AutoBeanFactory autoBeanFactoryProvider() {
    return AutoBeanFactorySource.create(Factory.class);
  }

  @Provides
  @Singleton
  ServiceLayer serviceLayerProvider(final ServiceLayerDecorator serviceLayerDecorator) {
    return ServiceLayer.create(serviceLayerDecorator);
  }

  /**
   * Creates and reuses injecting JSR 303 Validator factory.
   * 
   * @param injector the injector that will be used for the injection.
   * @return The ValidatorFactory.
   */
  @Provides
  @Singleton
  ValidatorFactory validatorFactoryProvider(final ConstraintValidatorFactory factory) {
    return Validation.byDefaultProvider().configure().constraintValidatorFactory(factory)
        .buildValidatorFactory();
  }

  /**
   * Creates and reuses injecting JSR 303 Validator.
   * 
   * @param validatorFactory the ValidatorFactory to get the Validator from.
   * @return the Validator.
   */
  @Provides
  @Singleton
  Validator validatorProvider(final ValidatorFactory validatorFactory) {
    return validatorFactory.getValidator();
  }
}
