package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.CommentService;

import org.cloudlet.web.service.shared.rpc.BaseContext;

@Service(value = CommentService.class, locator = RfLocator.class)
public interface CommentContext extends BaseContext {

  Request<Void> put(final String mac, final String text, final long issueId);

}
