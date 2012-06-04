package com.retech.reader.web.server.service;

import com.goodow.web.logging.server.servlet.ChannelPresenceSevlet;
import com.goodow.web.service.server.jpa.BaseDomain;
import com.goodow.web.service.server.jpa.BaseService;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;


public class TalkService extends BaseService<BaseDomain> {
  private final ChannelPresenceSevlet channelPresenceSevlet;
  ChannelService channelService = ChannelServiceFactory.getChannelService();

  @Inject
  TalkService(final ChannelPresenceSevlet channelPresenceSevlet) {
    this.channelPresenceSevlet = channelPresenceSevlet;
  }

  public void send(final String content) {
    for (String clientId : channelPresenceSevlet.getConnectedClientIds()) {
      channelService.sendMessage(new ChannelMessage(clientId, content));
    }
  }

}
