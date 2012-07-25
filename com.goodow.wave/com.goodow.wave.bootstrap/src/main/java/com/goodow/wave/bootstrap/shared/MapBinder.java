/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.wave.bootstrap.shared;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.inject.Provider;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class MapBinder<K, V> extends AbstractMap<K, V> implements LinkedBindingBuilder<V> {
  private Map<K, AsyncProvider<V>> asyncProviderMap;
  private Map<K, Provider<V>> providerMap;
  private Map<K, V> map;

  private static final Logger logger = Logger.getLogger(MapBinder.class.getName());

  private K key;

  public LinkedBindingBuilder<V> bind(final K key) {
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

  public Provider<V> getProvider(final K key) {
    return providerMap == null ? null : providerMap.get(key);
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

  @Override
  public void toProvider(final Provider<? extends V> provider) {
    if (providerMap == null) {
      providerMap = new HashMap<K, Provider<V>>();
    }
    if (null != providerMap.put(key, (Provider<V>) provider)) {
      logger.warning("绑定了重复的key=" + key);
    }
    key = null;
  }

}
