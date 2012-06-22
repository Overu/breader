package com.goodow.web.core.jpa;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import com.goodow.web.core.client.CoreClientPlugin.Startup;
import com.goodow.web.core.shared.ContentService;
import com.goodow.web.core.shared.Service;
import com.goodow.web.core.shared.UserService;

import com.google.inject.Singleton;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.jpa.JpaPersistModule;

import org.aopalliance.intercept.MethodInterceptor;

import java.util.logging.Logger;

@Singleton
public class CoreJpaModule extends JpaModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @java.lang.Override
  protected void configure() {

    logger.finest("Install JpaServiceModule begin");

    bind(Startup.class).asEagerSingleton();
    bind(UserService.class).to(JpaUserService.class);
    bind(ContentService.class).to(JpaContentService.class);
    bind(Service.class).to(JpaService.class);

    install(new JpaPersistModule("persist.jpaUnit")); // TODO read from config;

    requestStaticInjection(InjectionListener.class);

    MethodInterceptor finderInterceptor = new JpaFinderProxy();
    requestInjection(finderInterceptor);
    bindInterceptor(any(), annotatedWith(Finder.class), finderInterceptor);
    logger.finest("Install JpaServiceModule end");
  }
}