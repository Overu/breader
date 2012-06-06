package com.goodow.web.core.server;

import com.goodow.web.core.shared.Message;
import com.google.inject.Inject;
import com.google.inject.Provider;


public class MessageService implements Provider<Message> {

  private final ThreadLocal<Message> message = new ThreadLocal<Message>();

  @Inject
  private Provider<ServerMessage> serverMessage;

  @Override
  public Message get() {
    Message result = message.get();
    if (result == null) {
      message.set(serverMessage.get());
    }
    return message.get();
  }
}