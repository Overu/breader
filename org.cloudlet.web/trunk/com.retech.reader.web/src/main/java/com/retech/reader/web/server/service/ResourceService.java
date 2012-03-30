package com.retech.reader.web.server.service;

import com.retech.reader.web.server.domain.Issue;
import com.retech.reader.web.server.domain.Page;
import com.retech.reader.web.server.domain.Resource;

import org.cloudlet.web.service.server.jpa.BaseService;

public class ResourceService extends BaseService<Resource> {

  public String getDataString(final Long id) {
    return em.get().find(Resource.class, id).getDataString();
  }

  public Resource getImage(final Issue issue) {
    return issue.getImage();
  }

  public Resource getResource(final Page page) {
    return page.getResources().get(0);
  }

}
