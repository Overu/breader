package com.goodow.web.core.shared;

public interface ObjectWriter<W extends WebObject, T> {

  public void writeTo(W obj, T target, Message message);

}
