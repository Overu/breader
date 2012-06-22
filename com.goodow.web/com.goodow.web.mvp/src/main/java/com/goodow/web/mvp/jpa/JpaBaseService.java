package com.goodow.web.mvp.jpa;

import com.goodow.web.core.jpa.JpaContentService;
import com.goodow.web.core.shared.WebEntity;
import com.goodow.web.mvp.shared.BaseService;

import com.google.inject.persist.Transactional;

import java.util.List;

import javax.persistence.Query;

public class JpaBaseService<E extends WebEntity> extends JpaContentService<E> implements
    BaseService<E> {

  @Override
  @Transactional
  public long count() {
    return ((Number) em.get().createQuery(
        "select count(d) from " + getEntityClass().getName() + " d").getSingleResult()).longValue();
  }

  @Override
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

  /**
   * 该方法只能为public
   * 
   * @param domain
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  public void put(final Object domain) {
    if (getEntityClass().isAssignableFrom(domain.getClass())) {
      put((E) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * 该方法只能为public
   * 
   * @param domain
   */
  @Override
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
