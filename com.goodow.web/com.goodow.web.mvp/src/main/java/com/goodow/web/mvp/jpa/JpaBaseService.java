package com.goodow.web.mvp.jpa;

import com.goodow.web.core.jpa.JpaWebContentService;
import com.goodow.web.core.shared.WebContent;
import com.goodow.web.mvp.shared.BaseService;

import com.google.inject.persist.Transactional;

import java.util.List;

import javax.persistence.Query;

public class JpaBaseService<E extends WebContent> extends JpaWebContentService<E> implements BaseService<E> {

  @Override
  @Transactional
  public long count() {
    return ((Number) em.get().createQuery(
        "select count(d) from " + getObjectType().getName() + " d").getSingleResult()).longValue();
  }

  @Override
  @SuppressWarnings("unchecked")
  @Transactional
  public List<E> find(final int start, final int length) {
    StringBuilder sb = new StringBuilder();
    sb.append("select d from ");
    sb.append(getObjectType().getName());
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
    if (getObjectType().isAssignableFrom(domain.getClass())) {
      save((E) domain);
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
    if (getObjectType().isAssignableFrom(domain.getClass())) {
      remove((E) domain);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
