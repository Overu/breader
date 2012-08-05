package com.goodow.web.core.jpa;

import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceService;
import com.goodow.web.core.shared.WebException;

import com.google.inject.persist.Transactional;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class JpaResourceService extends JpaEntityService<Resource> implements ResourceService {

  @Override
  @Transactional
  public Resource save(final Resource entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID().toString());
    }
    if (entity.getTextContent() != null) {
      byte[] bytes = null;
      try {
        bytes = entity.getTextContent().getBytes("UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new WebException(e.getMessage());
      }
      ServletMessage.saveResource(entity, new ByteArrayInputStream(bytes));
    }
    em.get().persist(entity);
    return entity;
  }

}
