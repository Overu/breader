package com.goodow.web.service.server.jpa;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import com.goodow.web.service.server.InjectionListener;

import com.google.inject.AbstractModule;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.jpa.JpaPersistModule;

import org.aopalliance.intercept.MethodInterceptor;

import java.util.logging.Logger;

public final class JpaServiceModule extends AbstractModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");

    install(new JpaPersistModule("persist.jpaUnit")); // TODO read from config;

    // bind(RemoteServiceLocator.class); // TODO why bind RemoteServiceLocator?

    requestStaticInjection(InjectionListener.class);

    MethodInterceptor finderInterceptor = new JpaFinderProxy();
    requestInjection(finderInterceptor);
    bindInterceptor(any(), annotatedWith(Finder.class), finderInterceptor);
    logger.finest("Install JpaServiceModule end");
  }

}
