package com.goodow.web.core.shared;

public interface Receiver<T> {
  void onSuccess(T result);
}
