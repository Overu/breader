package com.goodow.web.security.server.ioc;

import com.goodow.web.security.server.auth.OpenIdAuthServlet;

import com.google.inject.servlet.ServletModule;

import org.apache.shiro.guice.web.GuiceShiroFilter;

public class SecurityServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    filter("/*").through(GuiceShiroFilter.class);
    serve("/_ah/login_required").with(OpenIdAuthServlet.class);

    install(new ShiroSecurityModule(getServletContext()));
  }
}
