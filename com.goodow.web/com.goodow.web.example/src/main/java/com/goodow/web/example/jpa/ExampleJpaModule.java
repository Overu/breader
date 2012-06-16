package com.goodow.web.example.jpa;

import com.goodow.web.core.jpa.JpaModule;
import com.goodow.web.core.shared.CoreFactory;
import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.example.shared.LibraryService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class ExampleJpaModule extends JpaModule {

  public static class Startup {
    @Inject
    public Startup(final CorePackage pkg, final WebPlatform platform) {
      platform.getPackages().put(pkg.getName(), pkg);
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @java.lang.Override
  protected void configure() {
    logger.finest("Install JpaServiceModule begin");
    requestStaticInjection(CoreFactory.class);
    bind(Startup.class).asEagerSingleton();
    bind(LibraryService.class).to(JpaLibraryService.class);
    logger.finest("Install JpaServiceModule end");
  }
}