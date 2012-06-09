package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;
import com.goodow.web.mvp.shared.tree.rpc.BaseContext;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.CommentServiceImpl;


@Service(value = CommentServiceImpl.class, locator = RequestFactoryLocator.class)
public interface CommentContext extends BaseContext {

  Request<Void> put(final String mac, final String text, final long issueId);

}
