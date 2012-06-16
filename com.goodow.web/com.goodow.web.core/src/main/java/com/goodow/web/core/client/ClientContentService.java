package com.goodow.web.core.client;

import com.goodow.web.core.shared.*;
public class ClientContentService<E extends Content> extends ClientService<E> {
  public Request<Void> put(E content) {
    return invoke(CorePackage.ContentService_put, content);
  }
  
  public Request<Void> remove(E content) {
    return invoke(CorePackage.ContentService_remove, content);
  }
}
