package com.goodow.web.core.server;

import java.util.Set;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.internal.inject.ServiceBindingBuilder;
import org.glassfish.jersey.server.spi.ComponentProvider;

import com.goodow.web.core.shared.WebContentService;
import com.goodow.web.core.shared.WebPlatform;
import com.google.inject.Injector;

public class GuiceComponentProvider implements ComponentProvider {

	private class GuiceFactory<T> implements Factory<T> {

		private Class<T> clz;

		private GuiceFactory(Class<T> clz) {
			this.clz = clz;
		}

		@Override
		public T provide() {
//			return WebPlatform.getInstance().getInjector().getInstance(clz);
			return null;
		}

		@Override
		public void dispose(T instance) {
		}
	}

	private ServiceLocator locator = null;

	// ComponentProvider
	@Override
	public void initialize(final ServiceLocator locator) {
		this.locator = locator;
	}

	// ComponentProvider
	@SuppressWarnings("unchecked")
	@Override
	public boolean bind(Class<?> component, Set<Class<?>> providerContracts) {

		if (locator == null) {
			throw new IllegalStateException(
					"Guice component is not initialized properly.");
		}

		if (!WebContentService.class.isAssignableFrom(component)) {
			return false;
		}

		// TODO register interecepters

		DynamicConfiguration dc = Injections.getConfiguration(locator);
		final ServiceBindingBuilder bindingBuilder = Injections
				.newFactoryBinder(new GuiceFactory(component));
		bindingBuilder.to(component);
		for (Class contract : providerContracts) {
			bindingBuilder.to(contract);
		}

		Injections.addBinding(bindingBuilder, dc);

		dc.commit();
		return true;
	}

	@Override
	public void done() {
		// TODO bind ExceptionMapper ?
	}

}