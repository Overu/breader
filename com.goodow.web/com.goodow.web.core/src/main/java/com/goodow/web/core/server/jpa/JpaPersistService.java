package com.goodow.web.core.server.jpa;

import java.util.Properties;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Singleton
class JpaPersistService implements Provider<EntityManager>, UnitOfWork, PersistService {
  @Singleton
  public static class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    private final JpaPersistService emProvider;

    @Inject
    public EntityManagerFactoryProvider(final JpaPersistService emProvider) {
      this.emProvider = emProvider;
    }

    @Override
    public EntityManagerFactory get() {
      assert null != emProvider.emFactory;
      return emProvider.emFactory;
    }
  }

  private final ThreadLocal<EntityManager> entityManager = new ThreadLocal<EntityManager>();
  private final String persistenceUnitName;

  private final Properties persistenceProperties;

  private volatile EntityManagerFactory emFactory;

  @Inject
  public JpaPersistService(@Jpa final String persistenceUnitName,
      @Nullable @Jpa final Properties persistenceProperties) {
    this.persistenceUnitName = persistenceUnitName;
    this.persistenceProperties = persistenceProperties;
  }

  @Override
  public void begin() {
    Preconditions.checkState(null == entityManager.get(),
        "Work already begun on this thread. Looks like you have called UnitOfWork.begin() twice"
            + " without a balancing call to end() in between.");

    entityManager.set(emFactory.createEntityManager());
  }

  @Override
  public void end() {
    EntityManager em = entityManager.get();

    // Let's not penalize users for calling end() multiple times.
    if (null == em) {
      return;
    }

    em.close();
    entityManager.remove();
  }

  @Override
  public EntityManager get() {
    if (!isWorking()) {
      begin();
    }

    EntityManager em = entityManager.get();
    Preconditions.checkState(null != em, "Requested EntityManager outside work unit. "
        + "Try calling UnitOfWork.begin() first, or use a PersistFilter if you "
        + "are inside a servlet environment.");

    return em;
  }

  public boolean isWorking() {
    return entityManager.get() != null;
  }

  @Override
  public synchronized void start() {
    Preconditions.checkState(null == emFactory, "Persistence service was already initialized.");

    if (null != persistenceProperties) {
      this.emFactory =
          Persistence.createEntityManagerFactory(persistenceUnitName, persistenceProperties);
    } else {
      this.emFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }
  }

  @Override
  public synchronized void stop() {
    Preconditions.checkState(emFactory.isOpen(), "Persistence service was already shut down.");
    emFactory.close();
  }
}
