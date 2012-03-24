package org.cloudlet.web.security.server.ioc;

import com.google.inject.name.Names;

import org.apache.shiro.guice.web.ShiroWebModule;
import org.cloudlet.web.security.server.auth.JpaRealm;

import javax.servlet.ServletContext;

public class ShiroSecurityModule extends ShiroWebModule {
  public ShiroSecurityModule(final ServletContext sc) {
    super(sc);
  }

  @Override
  protected void configureShiroWeb() {
    addFilterChain("/public/**", ANON);
    addFilterChain("/stuff/allowed/**", AUTHC_BASIC, config(PERMS, "yes"));
    addFilterChain("/stuff/forbidden/**", AUTHC_BASIC, config(PERMS, "no"));
    // addFilterChain("/**", AUTHC_BASIC);

    bindRealm().to(JpaRealm.class);
    bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);
  }
}
