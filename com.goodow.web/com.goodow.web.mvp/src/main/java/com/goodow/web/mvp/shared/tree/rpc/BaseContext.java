package com.goodow.web.mvp.shared.tree.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.jpa.JpaBaseService;
import com.goodow.web.mvp.shared.tree.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

/**
 * 该类不能为泛型, 参见
 * {@link com.google.web.bindery.requestfactory.server.ResolverServiceLayer#resolveRequestContextMethod(String)}
 * 
 * @author larry
 * 
 */
@Service(value = JpaBaseService.class, locator = RequestFactoryLocator.class)
public interface BaseContext extends RequestContext {

  Request<Long> count();

  <T extends BaseEntityProxy> Request<Void> put(T proxy);

  <T extends BaseEntityProxy> Request<Void> remove(T proxy);

}
