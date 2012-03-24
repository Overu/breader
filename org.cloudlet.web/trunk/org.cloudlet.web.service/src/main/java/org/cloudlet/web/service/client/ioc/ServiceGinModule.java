package org.cloudlet.web.service.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;

import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.logging.Logger;

public final class ServiceGinModule extends AbstractGinModule {

  private static final Logger logger = Logger.getLogger(ServiceGinModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    requestStaticInjection(BaseReceiver.class);
  }
}
