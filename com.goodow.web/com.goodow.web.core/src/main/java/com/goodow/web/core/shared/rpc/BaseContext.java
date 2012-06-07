package com.goodow.web.core.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.security.server.ServerContentService;

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
@Service(value = ServerContentService.class, locator = RequestFactoryLocator.class)
public interface BaseContext extends RequestContext {
  Request<Long> count();

  <T extends BaseEntityProxy> Request<Void> put(T proxy);

  <T extends BaseEntityProxy> Request<Void> remove(T proxy);

}
