package org.cloudlet.web.test;

import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.junit4.GuiceBerryRule;
import com.google.inject.Module;

import org.cloudlet.web.boot.server.BootModule;
import org.junit.Assert;
import org.junit.Rule;

public abstract class BaseTest extends Assert {

  public static class BaseTestModule extends GuiceBerryModule {
    @Override
    protected void configure() {
      super.configure();
      install(new TestModule());
      install(new BootModule());
    }
  }

  @Rule
  public final GuiceBerryRule guiceBerry = new GuiceBerryRule(providesModule());

  protected Class<? extends Module> providesModule() {
    return BaseTestModule.class;
  }
}