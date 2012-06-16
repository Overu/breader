package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Content;
import com.goodow.web.core.shared.ContentService;

import com.google.inject.persist.Transactional;

public class JpaContentService<E extends Content> extends JpaService<E> implements
    ContentService<E> {
  @Override
  @Transactional
  public void put(final E domain) {
    if (domain.getId() == null) {
      em.get().persist(domain);
    } else {
      em.get().merge(domain);
    }
  }

  @Override
  @Transactional
  public void remove(final E domain) {
    em.get().remove(domain);
  }
}
