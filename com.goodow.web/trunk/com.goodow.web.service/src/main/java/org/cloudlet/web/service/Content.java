package org.cloudlet.web.service;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(Content.class)
public interface Content extends EntityProxy {

  String getId();

  String getUri();

  Long getVersion();

  void setId(String value);

}
