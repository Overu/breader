package com.goodow.web.core.shared;

public interface ObjectReader<W extends WebObject, T> {

  public void readFrom(W obj, T source, Message message);

}
