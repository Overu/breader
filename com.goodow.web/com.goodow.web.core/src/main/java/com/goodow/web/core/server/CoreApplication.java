package com.goodow.web.core.server;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jettison.JettisonBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.goodow.wave.bootstrap.server.BootstrapModule;
import com.goodow.web.core.jpa.JpaGroupService;
import com.goodow.web.core.jpa.JpaUserService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

public class CoreApplication extends ResourceConfig {

	@Inject
	public CoreApplication(PersistService persistService) {
		super(JaxbContextResolver.class, JpaUserService.class,
				JpaGroupService.class);
		persistService.start();
		addBinders(new JettisonBinder());
	}

	private static final URI BASE_URI = URI
			.create("http://localhost:8080/core/");

	public static void main(String[] args) {
		try {
			System.out.println("JSON with JAXB Jersey Example App");
			CoreApplication app = createApp();
			final HttpServer server = GrizzlyHttpServerFactory
					.createHttpServer(BASE_URI, app);

			System.out
					.println(String
							.format("Application started.%nTry out %s%nHit enter to stop it...",
									BASE_URI));
			System.in.read();
			server.stop();
		} catch (IOException ex) {
			Logger.getLogger(CoreApplication.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public static CoreApplication createApp() {
		final Injector injector = Guice.createInjector(new BootstrapModule());
		return injector.getInstance(CoreApplication.class);
	}
}
