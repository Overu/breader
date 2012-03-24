package org.cloudlet.web.logging.shared.rpc;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import org.cloudlet.web.logging.server.service.ChannelService;

@Service(value = ChannelService.class)
public interface ChannelContext extends RequestContext {
  Request<String> getToken(String clientId);
}
