package com.retech.reader.web.jpa.ioc;

import com.google.inject.servlet.ServletModule;

import com.retech.reader.web.jpa.servlet.ResourceServlet;

public class ResourceServletModule extends ServletModule {

  @Override
  protected void configureServlets() {
    serve("/res").with(ResourceServlet.class);
  }

}
