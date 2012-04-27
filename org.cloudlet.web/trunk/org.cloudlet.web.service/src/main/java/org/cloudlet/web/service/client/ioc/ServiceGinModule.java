package org.cloudlet.web.service.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

import org.cloudlet.web.service.client.rpc.BaseRequestTransport;
import org.cloudlet.web.service.shared.FileProxyStore;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.logging.Logger;

public final class ServiceGinModule extends AbstractGinModule {

  private static final Logger logger = Logger.getLogger(ServiceGinModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("config start");

    bind(RequestTransport.class).to(BaseRequestTransport.class).in(Singleton.class);
    bind(FileProxyStore.class).asEagerSingleton();
    requestStaticInjection(BaseReceiver.class);
  }
}
