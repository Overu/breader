package com.goodow.web.service.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.service.server.jpa.BaseDomain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value = BaseDomain.class, locator = RequestFactoryLocator.class)
public interface BaseEntityProxy extends EntityProxy {

}
