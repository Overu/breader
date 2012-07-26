package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaResourceService;
import com.goodow.web.core.jpa.JpaWebService;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.Section;
import com.goodow.web.core.shared.SectionService;
import com.goodow.web.core.shared.WebEntity;

import com.google.inject.Inject;

import java.util.List;

import javax.persistence.TypedQuery;

public class JpaSectionService extends JpaWebService<Section> implements SectionService {
  @Inject
  JpaResourceService resourceService;

  @Override
  public List<Section> find(final WebEntity container) {
    String hsql =
        "select e from " + getEntityClass().getName()
            + " e where e.container=:container order by e.displayOrder asc";
    TypedQuery<Section> query = em.get().createQuery(hsql, getEntityClass());
    query.setParameter("container", container);
    return query.getResultList();
  }

  @Override
  public Section save(final Section entity) {
    Resource resource = entity.getResource();
    if (resource != null) {
      resourceService.save(resource);
    }
    return super.save(entity);
  }
}
