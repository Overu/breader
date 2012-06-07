package com.goodow.web.core.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.mortbay.jetty.ResourceCache.Content;

@ProxyFor(value = Content.class, locator = RequestFactoryLocator.class)
public interface BaseEntityProxy extends EntityProxy {

}
