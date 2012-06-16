package com.goodow.web.example.client;

import com.goodow.web.core.shared.*;
import com.google.inject.*;
import com.goodow.web.core.client.*;
import com.goodow.web.example.shared.*;

@Singleton
public class ExampleClientModule extends ClientModule {
  public static class Startup {
    @Inject
    public Startup(final ExamplePackage pkg, final WebPlatform platform) {
      platform.getPackages().put(pkg.getName(), pkg);
    }
  }
  
  @java.lang.Override
  protected void configure() {
    requestStaticInjection(ExampleFactory.class);
    bind(Startup.class).asEagerSingleton();
  }
}