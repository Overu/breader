package org.cloudlet.web.service.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

@Singleton
final class ServiceServlet extends RequestFactoryServlet {

  @Inject
  ServiceServlet(final ExceptionHandler exceptionHandler,
      final ServiceLayerDecorator serviceLayerDecorator) {
    super(exceptionHandler, serviceLayerDecorator);
  }

}
