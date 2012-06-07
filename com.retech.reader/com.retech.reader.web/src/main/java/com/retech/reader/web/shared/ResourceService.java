package com.retech.reader.web.shared;

import com.goodow.web.security.shared.ContentService;

public interface ResourceService extends ContentService<Resource> {

  String getDataString(final Long id);

  Resource getImage(final Issue issue);

  Resource getResource(final Page page);

}
