package com.goodow.web.reader.client.droppable;

import com.google.gwt.core.client.JavaScriptObject;

final public class JsMap<S, T> extends JavaScriptObject {

  public static <S, T> JsMap<S, T> create() {
    return createObject().cast();
  }

  protected JsMap() {
  }

  @SuppressWarnings("unchecked")
  public T get(final int hashCode) {
    return (T) c().get(hashCode);
  }

  public T get(final S key) {
    return get(key.hashCode());
  }

  public final String[] keys() {
    return c().keys();
  }

  public void put(final S key, final T val) {
    c().put(key.hashCode(), val);
  }

  private JsCache c() {
    return cast();
  }
}
