package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaWebService;
import com.goodow.web.core.shared.Section;
import com.goodow.web.core.shared.SectionService;
import com.goodow.web.core.shared.WebEntity;

import java.util.List;

import javax.persistence.TypedQuery;

public class JpaSectionService extends JpaWebService<Section> implements SectionService {
  @Override
  public List<Section> find(final WebEntity container) {
    String hsql =
        "select e from " + getEntityClass().getName()
            + " e where e.container=:container order by e.displayOrder asc";
    TypedQuery<Section> query = em.get().createQuery(hsql, getEntityClass());
    query.setParameter("container", container);
    return query.getResultList();
  }
}
