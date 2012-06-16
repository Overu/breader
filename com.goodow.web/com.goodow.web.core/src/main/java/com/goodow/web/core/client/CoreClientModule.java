package com.goodow.web.core.client;

import com.goodow.web.core.shared.*;
import com.google.inject.*;

@Singleton
public class CoreClientModule extends ClientModule {
  public static class Startup {
    @Inject
    public Startup(final CorePackage pkg, final WebPlatform platform) {
      platform.getPackages().put(pkg.getName(), pkg);
    }
  }
  
  @java.lang.Override
  protected void configure() {
    requestStaticInjection(CoreFactory.class);
    bind(Startup.class).asEagerSingleton();
  }
}