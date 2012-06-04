package com.goodow.web.android.persist;

public interface Persistor {

  void clear();

  boolean containsKey(String key);

  String get(String key);

  boolean isEmpty();

  String put(String key, String value);

  String remove(String key);

  long size();

}
