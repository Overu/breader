package org.cloudlet.web.boot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = {BootGinModule.class}, properties = {
    "shared.ioc.ginModules", "client.ioc.ginModules"})
public interface BootInjector extends Ginjector {

  GWT get();

}
