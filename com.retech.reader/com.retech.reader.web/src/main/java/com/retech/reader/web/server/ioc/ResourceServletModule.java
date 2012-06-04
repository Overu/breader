package com.retech.reader.web.server.ioc;

import com.google.inject.servlet.ServletModule;

import com.retech.reader.web.server.servlet.ResourceServlet;

public class ResourceServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    serve("/res").with(ResourceServlet.class);
  }

}
