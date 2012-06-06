package com.goodow.web.core.server;

import com.goodow.web.security.shared.Content;
import com.goodow.web.security.shared.ContentService;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.google.web.bindery.autobean.vm.impl.TypeUtils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class ServerContentService<E extends Content> extends ServerService<E> implements
    ContentService<E> {

  @Inject
  protected transient Provider<EntityManager> em;

  protected Class<E> domainClass;

  @SuppressWarnings("unchecked")
  protected ServerContentService() {
    domainClass =
        (Class<E>) TypeUtils.ensureBaseType(TypeUtils.getSingleParameterization(
            ServerContentService.class, getClass().getGenericSuperclass()));
  }

  @Transactional
  public long count() {
    return ((Number) em.get().createQuery("select count(d) from " + domainClass.getName() + " d")
        .getSingleResult()).longValue();
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public List<E> find(final int start, final int length) {
    StringBuilder sb = new StringBuilder();
    sb.append("select d from ");
    sb.append(domainClass.getName());
    sb.append(" d ");

    Query query = em.get().createQuery(sb.toString());
    query.setFirstResult(start);
    query.setMaxResults(length);
    return query.getResultList();
  }

  @Transactional
  public void put(final E domain) {
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
  public void put(final Object domain) {
    if (domainClass.isAssignableFrom(domain.getClass())) {
      put((E) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Transactional
  public void remove(final E domain) {
    em.get().remove(domain);
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
      remove((E) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
