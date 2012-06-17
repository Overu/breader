package com.goodow.web.reader.client;

import com.goodow.web.core.shared.*;
import com.google.inject.*;
import com.goodow.web.core.client.*;
import com.goodow.web.reader.shared.*;

@Singleton
public class ReaderClientModule extends ClientModule {
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