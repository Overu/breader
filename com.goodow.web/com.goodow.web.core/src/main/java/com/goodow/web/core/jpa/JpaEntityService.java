package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.WebEntityService;
import com.goodow.web.core.shared.WebEntity;

import com.google.inject.persist.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

public class JpaEntityService<E extends WebEntity> extends JpaWebService<E> implements
    WebEntityService<E> {

  private static final Logger logger = Logger.getLogger(JpaEntityService.class.getName());

  @Override
  public E getById(final String id) {
    return em.get().find(getObjectType(), id);
  }

  @Override
  public List<E> find(final WebEntity container) {
    String hsql = "select e from " + getObjectType().getName() + " e where e.container=:container";
    TypedQuery<E> query = em.get().createQuery(hsql, getObjectType());
    query.setParameter("container", container);
    return query.getResultList();
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
    }
    em.get().persist(entity);
    return entity;
  }

}
