package com.goodow.wave.bootstrap.shared;

import com.google.gwt.inject.client.AsyncProvider;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class MapBinder<K, V> extends AbstractMap<K, V> implements LinkedBindingBuilder<V> {
  private Map<K, AsyncProvider<V>> asyncProviderMap;
  private Map<K, V> map;

  private final Logger logger = Logger.getLogger(getClass().getName());

  private K key;

  public LinkedBindingBuilder<V> addBinding(final K key) {
    if (this.key != null) {
      logger.warning("未绑定key=" + this.key);
    }
    this.key = key;
    return this;
  }

  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public V get(final Object key) {
    return map == null ? null : map.get(key);
  }

  public AsyncProvider<V> getAsyncProvider(final K key) {
    return asyncProviderMap == null ? null : asyncProviderMap.get(key);
  }

  @Override
  public void toAsyncProvider(final AsyncProvider<? extends V> provider) {
    if (asyncProviderMap == null) {
      asyncProviderMap = new HashMap<K, AsyncProvider<V>>();
    }
    if (null != asyncProviderMap.put(key, (AsyncProvider<V>) provider)) {
      logger.warning("绑定了重复的key=" + key);
    }
    key = null;
  }

  @Override
  public void toInstance(final V instance) {
    if (map == null) {
      map = new LinkedHashMap<K, V>();
    }
    if (null != map.put(key, instance)) {
      logger.warning("绑定了重复的key=" + key);
    }
    key = null;
  }

}
