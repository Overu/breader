package org.cloudlet.web.boot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
final class Boot implements EntryPoint {
  @Override
  public void onModuleLoad() {
    BootInjector injector = GWT.create(BootInjector.class);
    injector.get();
  }
}
