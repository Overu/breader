package org.cloudlet.web.security.server.ioc;

import com.google.inject.servlet.ServletModule;

import org.apache.shiro.guice.web.GuiceShiroFilter;
import org.cloudlet.web.security.server.auth.OpenIdAuthServlet;

public class SecurityServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    filter("/*").through(GuiceShiroFilter.class);
    serve("/_ah/login_required").with(OpenIdAuthServlet.class);

    install(new ShiroSecurityModule(getServletContext()));
  }
}
