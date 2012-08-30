package com.goodow.web.reader.client.droppable;

import com.google.gwt.core.client.JavaScriptObject;

public final class JsObjectArray<T> extends JavaScriptObject {
  public static <T> JsObjectArray<T> create() {
    return JavaScriptObject.createArray().cast();
  }

  protected JsObjectArray() {
  }

  public void add(final int i, final T val) {
    c().put(i, val);
  }

  public void add(final T... vals) {
    for (T t : vals) {
      c().put(length(), t);
    }
  }

  public void concat(final JsObjectArray<T> ary) {
    c().concat(ary);
  }

  @SuppressWarnings("unchecked")
  public T get(final int index) {
    return (T) c().get(index);
  }

  public int length() {
    return c().length();
  }

  public void pushAll(final JavaScriptObject prevElem) {
    c().pushAll(prevElem);
  }

  public void set(final int i, final T val) {
    c().put(i, val);
  }

  private JsCache c() {
    return cast();
  }
}
