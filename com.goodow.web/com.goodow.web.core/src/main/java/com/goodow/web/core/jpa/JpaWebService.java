package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Operation;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.core.shared.WebService;
import com.goodow.web.core.shared.Wrapper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

public class JpaWebService<E extends WebEntity> implements WebService<E> {
  private static final Logger logger = Logger.getLogger(JpaWebService.class.getName());
  @Inject
  protected transient Provider<EntityManager> em;

  private Class<E> domainClass;

  @SuppressWarnings("unchecked")
  protected JpaWebService() {
    Type genericSuperClass = getClass().getGenericSuperclass();
    domainClass =
        (Class<E>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(WebService.class,
            genericSuperClass));
  }

  @Override
  public E find(final String id) {
    return em.get().find(getEntityClass(), id);
  }

  @Override
  public Class<E> getEntityClass() {
    return domainClass;
  }

  public Method getJavaMethod(final Operation operation) {
    Method method = operation.getJavaMethod();
    if (method == null) {
      for (Method m : operation.getDeclaringType().getServiceClass().getDeclaredMethods()) {
        if (m.getName().equals(operation.getName())) {
          method = m;
          operation.setJavaMethod(method);
          break;
        }
      }
    }
    return method;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T2> T2 invoke(final Wrapper<Operation> operation, final Object... args) {
    Method method = getJavaMethod(operation.as());
    try {
      return (T2) method.invoke(this, args);
    } catch (InvocationTargetException e) {
      Throwable t = e.getCause();
      t.printStackTrace();
      logger.severe(t.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      logger.severe(e.getMessage());
    }
    return null;
  }

  @Override
  @Transactional
  public void remove(final E domain) {
    em.get().remove(domain);
  }

  @Override
  @Transactional
  public E save(final E entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID().toString());
      em.get().persist(entity);
    } else {
      em.get().merge(entity);
    }
    return entity;
  }

  protected EntityManager em() {
    return em.get();
  }

}
