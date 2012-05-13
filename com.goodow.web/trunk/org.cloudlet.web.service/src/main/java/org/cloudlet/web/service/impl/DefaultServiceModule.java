package org.cloudlet.web.service.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import org.cloudlet.web.service.Repository;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class DefaultServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ResourceSet.class).to(ResourceSetImpl.class).in(Singleton.class);
    bind(Repository.class).to(RepositoryImpl.class).asEagerSingleton();
  }

}
