package com.retech.reader.web.server;

import com.goodow.web.security.server.ContentServiceImpl;

import com.retech.reader.web.shared.Issue;
import com.retech.reader.web.shared.Page;
import com.retech.reader.web.shared.Resource;
import com.retech.reader.web.shared.ResourceService;

public class JpaResourceService extends ContentServiceImpl<Resource> implements
    ResourceService {

  @Override
  public String getDataString(final Long id) {
    return em.get().find(Resource.class, id).getDataString();
  }

  @Override
  public Resource getImage(final Issue issue) {
    return issue.getImage();
  }

  @Override
  public Resource getResource(final Page page) {
    return page.getResources().get(0);
  }

}
