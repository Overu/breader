package com.retech.reader.web.shared;

import com.goodow.web.mvp.shared.BaseService;

public interface ResourceService extends BaseService<Resource> {

  String getDataString(final Long id);

  Resource getImage(final Issue issue);

  Resource getResource(final Page page);

}
