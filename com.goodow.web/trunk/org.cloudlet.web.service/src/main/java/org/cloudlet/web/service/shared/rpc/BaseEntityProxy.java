package org.cloudlet.web.service.shared.rpc;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.server.jpa.BaseDomain;

@ProxyFor(value = BaseDomain.class, locator = NoLocator.class)
public interface BaseEntityProxy extends EntityProxy {

}
