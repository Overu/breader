package org.cloudlet.web.service.shared.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.cloudlet.web.service.server.jpa.BaseDomain;

@ProxyFor(value = BaseDomain.class, locator = RfLocator.class)
public interface BaseEntityProxy extends EntityProxy {

}
