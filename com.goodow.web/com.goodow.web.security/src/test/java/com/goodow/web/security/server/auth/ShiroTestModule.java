package com.goodow.web.security.server.auth;

import com.goodow.web.security.server.auth.JpaRealm;

import com.google.inject.name.Names;

import org.apache.shiro.guice.ShiroModule;

public class ShiroTestModule extends ShiroModule {

  @Override
  protected void configureShiro() {
    bindRealm().to(JpaRealm.class);
    bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);

    // bind(CredentialsMatcher.class).to(HashedCredentialsMatcher.class);
    // bind(HashedCredentialsMatcher.class);
    // bindConstant().annotatedWith(Names.named("shiro.hashAlgorithmName"))
    // .to(Sha1Hash.ALGORITHM_NAME);
  }

}
