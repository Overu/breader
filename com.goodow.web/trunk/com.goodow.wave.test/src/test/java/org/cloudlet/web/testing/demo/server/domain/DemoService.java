package org.cloudlet.web.testing.demo.server.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.finder.Finder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class DemoService {
  private final Provider<EntityManager> em;

  @Inject
  DemoService(final Provider<EntityManager> em) {
    this.em = em;
  }

  @Finder(query = "select d from DemoDomain d", returnAs = ArrayList.class)
  @Transactional
  public List<DemoDomain> findAll() {
    throw new AssertionError();
  }

  public void put(final DemoDomain domain) {
    if (domain.getId() == null) {
      em.get().persist(domain);
    } else {
      em.get().merge(domain);
    }
  }
}
