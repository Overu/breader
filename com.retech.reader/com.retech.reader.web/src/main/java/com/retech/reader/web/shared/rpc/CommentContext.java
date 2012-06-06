package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.core.shared.rpc.BaseContext;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.CommentService;


@Service(value = CommentService.class, locator = RequestFactoryLocator.class)
public interface CommentContext extends BaseContext {

  Request<Void> put(final String mac, final String text, final long issueId);

}
