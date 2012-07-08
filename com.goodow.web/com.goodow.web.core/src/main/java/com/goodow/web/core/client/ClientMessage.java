package com.goodow.web.core.client;

import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.ObjectType;
import com.goodow.web.core.shared.WebEntity;

import com.google.inject.Singleton;

@Singleton
public class ClientMessage extends Message {

  @Override
  public WebEntity find(final ObjectType objectType, final String id) {
    return null;
  }

}
