package org.cloudlet.web.service.server;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

import org.cloudlet.web.service.server.ServiceServletModule.Factory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ExceptionHandler.class).to(ServiceExceptionHandler.class);
    bind(ServiceLayerDecorator.class).to(InjectionServiceLayerDecorator.class);
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
  ValidatorFactory validatorFactoryProvider(final Injector injector) {
    return Validation.byDefaultProvider().configure().constraintValidatorFactory(
        new InjectionConstraintValidationFactory(injector)).buildValidatorFactory();
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
