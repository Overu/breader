package com.goodow.web.layout.client;

import com.goodow.web.core.shared.*;
import com.google.inject.*;
import com.goodow.web.core.client.*;
import com.goodow.web.layout.shared.*;

@Singleton
public class LayoutClientModule extends ClientModule {
  public static class Startup {
    @Inject
    public Startup(final LayoutPackage pkg, final WebPlatform platform) {
      platform.getPackages().put(pkg.getName(), pkg);
    }
  }
  
  @java.lang.Override
  protected void configure() {
    requestStaticInjection(LayoutFactory.class);
    bind(Startup.class).asEagerSingleton();
  }
}