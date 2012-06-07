package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.core.shared.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.gooodow.wave.shared.media.MimeType;
import com.retech.reader.web.shared.Resource;

@ProxyFor(value = Resource.class, locator = RequestFactoryLocator.class)
public interface ResourceProxy extends BaseEntityProxy {

  String getDataString();

  String getFilename();

  MimeType getMimeType();

  String getName();

  Long getVersion();

  boolean isImage();

  @Override
  EntityProxyId<ResourceProxy> stableId();

}
