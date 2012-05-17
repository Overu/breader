package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Resource;

import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

@ProxyFor(value = Resource.class, locator = RfLocator.class)
public interface ResourceProxy extends BaseEntityProxy {

  String getDataString();

  String getFilename();

  @Deprecated
  Long getId();

  MimeType getMimeType();

  String getName();

  Long getVersion();

  boolean isImage();

  @Override
  EntityProxyId<ResourceProxy> stableId();

}
