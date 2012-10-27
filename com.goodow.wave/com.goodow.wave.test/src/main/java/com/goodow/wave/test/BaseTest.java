package com.goodow.wave.test;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;

import com.goodow.wave.bootstrap.server.BootstrapModule;
import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.junit4.GuiceBerryRule;

public abstract class BaseTest extends JerseyTest {

	public static class BaseTestModule extends GuiceBerryModule {
		@Override
		protected void configure() {
			super.configure();
			install(new TestModule());
			install(new BootstrapModule());
		}
	}

	@Rule
	public final GuiceBerryRule guiceBerry = new GuiceBerryRule(
			BaseTestModule.class);

}
