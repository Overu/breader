package com.goodow.web.service.server.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class BaseService<T extends BaseDomain> {
  @Inject
  protected transient Provider<EntityManager> em;
  protected Class<T> domainClass;

  @SuppressWarnings("unchecked")
  protected BaseService() {
    domainClass =
        (Class<T>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(BaseService.class,
            getClass().getGenericSuperclass()));
  }

  @Transactional
  public long count() {
    return ((Number) em.get().createQuery("select count(d) from " + domainClass.getName() + " d")
        .getSingleResult()).longValue();
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public List<T> find(final int start, final int length) {
    StringBuilder sb = new StringBuilder();
    sb.append("select d from ");
    sb.append(domainClass.getName());
    sb.append(" d ");

    Query query = em.get().createQuery(sb.toString());
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }

  /**
   * 该方法只能为public
   * 
   * @param domain
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public void put(final Object domain) {
    if (domainClass.isAssignableFrom(domain.getClass())) {
      put((T) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public void put(final T domain) {
    if (domain.getId() == null) {
      em.get().persist(domain);
    } else {
      em.get().merge(domain);
    }
  }

  /**
   * 该方法只能为public
   * 
   * @param domain
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public void remove(final Object domain) {
    if (domainClass.isAssignableFrom(domain.getClass())) {
      remove((T) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public void remove(final T domain) {
    em.get().remove(domain);
  }
}
