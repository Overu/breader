package org.cloudlet.web.service.shared.rpc;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName(value = "com.retech.reader.web.server.domain.Resource", locator = "org.cloudlet.web.service.server.NoLocator")
public interface ResourceProxy extends BaseEntityProxy {

  String getDataString();

  String getFilename();

  MimeType getMimeType();

  String getName();

  Long getVersion();

  @Override
  EntityProxyId<ResourceProxy> stableId();
}
