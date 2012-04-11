/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.logging.server.servlet;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class ChannelPresenceSevlet extends HttpServlet {
  private Set<String> connectedClientIds = new LinkedHashSet<String>();
  private final Logger logger;

  @Inject
  ChannelPresenceSevlet(final Logger logger) {
    this.logger = logger;
  }

  public Set<String> getConnectedClientIds() {
    return connectedClientIds;
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {

    ChannelService channelService = ChannelServiceFactory.getChannelService();
    ChannelPresence presence = channelService.parsePresence(req);
    if (presence.isConnected()) {
      connectedClientIds.add(presence.clientId());
    } else {
      connectedClientIds.remove(presence.clientId());
    }
  }
}
