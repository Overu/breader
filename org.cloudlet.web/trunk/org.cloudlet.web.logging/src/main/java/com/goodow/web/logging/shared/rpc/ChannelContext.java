package com.goodow.web.logging.shared.rpc;

import com.goodow.web.logging.server.service.ChannelService;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;


@Service(value = ChannelService.class)
public interface ChannelContext extends RequestContext {
  Request<String> getToken(String clientId);
}
