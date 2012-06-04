package com.goodow.web.service.impl;

import com.goodow.web.service.Repository;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class DefaultServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ResourceSet.class).to(ResourceSetImpl.class).in(Singleton.class);
    bind(Repository.class).to(RepositoryImpl.class).asEagerSingleton();
  }

}
