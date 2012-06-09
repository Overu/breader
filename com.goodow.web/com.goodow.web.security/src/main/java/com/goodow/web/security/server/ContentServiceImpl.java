package com.goodow.web.security.server;

import com.goodow.web.core.server.ServiceImpl;
import com.goodow.web.security.shared.Content;
import com.goodow.web.security.shared.ContentService;

import com.google.inject.persist.Transactional;

import java.util.List;

import javax.persistence.Query;

public class ContentServiceImpl<E extends Content> extends ServiceImpl<E> implements
    ContentService<E> {

  @Transactional
  public long count() {
    return ((Number) em.get().createQuery(
        "select count(d) from " + getEntityClass().getName() + " d").getSingleResult()).longValue();
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public List<E> find(final int start, final int length) {
    StringBuilder sb = new StringBuilder();
    sb.append("select d from ");
    sb.append(getEntityClass().getName());
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
    if (getEntityClass().isAssignableFrom(domain.getClass())) {
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
    if (getEntityClass().isAssignableFrom(domain.getClass())) {
      remove((E) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
