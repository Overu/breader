package com.retech.reader.web.shared.rpc;

import com.goodow.wave.server.rf.RfLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

import com.retech.reader.web.server.service.TalkService;

import org.cloudlet.web.service.shared.rpc.BaseContext;

@Service(value = TalkService.class, locator = RfLocator.class)
public interface TalkContext extends BaseContext {
  Request<Void> send(String content);
}
