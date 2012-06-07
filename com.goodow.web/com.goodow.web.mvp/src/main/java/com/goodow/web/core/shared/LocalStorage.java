package com.goodow.web.core.shared;

import static com.goodow.web.core.shared.KeyUtil.LIST_SEPARATOR;
import static com.goodow.web.core.shared.KeyUtil.PROXY_SEPARATOR;
import static com.goodow.web.core.shared.KeyUtil.SET_SEPARATOR;

import com.goodow.web.core.shared.rpc.BaseEntityProxy;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.ValueCodex;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxySerializer;
import com.google.web.bindery.requestfactory.shared.ProxyStore;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityCodex;
import com.google.web.bindery.requestfactory.shared.impl.EntityCodex.EntitySource;
import com.google.web.bindery.requestfactory.shared.impl.MessageFactoryHolder;
import com.google.web.bindery.requestfactory.shared.impl.Poser;
import com.google.web.bindery.requestfactory.shared.impl.SimpleProxyId;
import com.google.web.bindery.requestfactory.shared.messages.IdMessage;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class LocalStorage {

  private final Provider<ProxySerializer> proxySerializers;

  private final ProxyStore proxyStore;
  private final EntitySource entitySource;

  // private final Map<String, Object> map;

  private ProxySerializer proxySerializer;

  @Inject
  private RequestFactory f;

  private final KeyUtil keyUtil;

  @Inject
  LocalStorage(final Provider<ProxySerializer> proxySerializers, final ProxyStore proxyStore,
      final KeyUtil keyUtil) {
    // map = new HashMap<String, Object>();
    this.proxySerializers = proxySerializers;
    this.proxyStore = proxyStore;
    this.keyUtil = keyUtil;
    proxySerializer = proxySerializers.get();
    this.entitySource = (EntitySource) proxySerializers.get();
  }

  @SuppressWarnings("unchecked")
  public <T> T get(final String key) {
    if (key == null) {
      return null;
    }
    if (!keyUtil.isEncodedKey(key)) {
      EntityProxyId<EntityProxy> proxyId = f.getProxyId(key);
      return (T) proxySerializers.get().deserialize(proxyId);
    }
    // valueTypeToken = f.getHistoryToken((Class<? extends BaseEntityProxy>) valueType);
    Splittable splittable = proxyStore.get(key);
    if (splittable == null) {
      return null;
    }
    if (key.contains(LIST_SEPARATOR)) {
      Class<? extends EntityProxy> proxyClass = getProxyClass(splittable.get(0));
      return (T) EntityCodex.decode(entitySource, List.class, proxyClass, splittable);
    }
    if (key.contains(SET_SEPARATOR)) {
      Class<? extends EntityProxy> proxyClass = getProxyClass(splittable.get(0));
      return (T) EntityCodex.decode(entitySource, Set.class, proxyClass, splittable);
    }
    if (key.contains(PROXY_SEPARATOR)) {
      Class<? extends EntityProxy> proxyClass = getProxyClass(splittable);
      return (T) EntityCodex.decode(entitySource, proxyClass, null, splittable);
    }
    // toReturn = (List<T>) map.get(key);
    // if (toReturn != null) {
    // return toReturn;
    // }

    return null;
  }

  public String put(final BaseEntityProxy proxy) {
    // map.put(f.getHistoryToken(proxy.stableId()), proxy);
    return proxySerializers.get().serialize(proxy);
  }

  // @SuppressWarnings("unchecked")
  // public <T> List<T> get(final String keyPrefix, final Class<T> valueType) {
  // String valueTypeToken = null;
  // try {
  // valueTypeToken = f.getHistoryToken((Class<? extends BaseEntityProxy>) valueType);
  // } catch (Exception e) {
  // valueTypeToken = valueType.getName();
  // }
  // String key = keyPrefix + "@" + valueTypeToken + "@";
  // List<T> toReturn = null;
  // // toReturn = (List<T>) map.get(key);
  // // if (toReturn != null) {
  // // return toReturn;
  // // }
  // toReturn =
  // (List<T>) EntityCodex.decode(entitySource, List.class, valueType, proxyStore.get(key));
  //
  // return cache(key, toReturn);
  // }

  public String put(final String key, final Object value) {
    if (key == null) {
      return null;
    }
    // if (!keyUtil.isEncodedKey(key)) {
    // assert value instanceof BaseEntityProxy;
    // BaseEntityProxy proxy = (BaseEntityProxy) value;
    // // map.put(f.getHistoryToken(proxy.stableId()), proxy);
    // return proxySerializers.get().serialize(proxy);
    // }
    // // map.put(key, values);
    LinkedHashSet<BaseProxy> toSerialize = new LinkedHashSet<BaseProxy>();
    Splittable encode = encode(value, toSerialize);
    for (BaseProxy proxy : toSerialize) {
      proxySerializer.serialize(proxy);
    }
    if (keyUtil.isEncodedKey(key)) {
      proxyStore.put(key, encode);
    }
    return key;
  }

  Splittable encode(Object value, final Set<BaseProxy> toSerialize) {
    if (value == null) {
      return Splittable.NULL;
    }

    if (value instanceof Poser<?>) {
      value = ((Poser<?>) value).getPosedValue();
    }

    if (value instanceof Iterable<?>) {
      StringBuffer toReturn = new StringBuffer();
      toReturn.append('[');
      boolean first = true;
      for (Object val : ((Iterable<?>) value)) {
        if (!first) {
          toReturn.append(',');
        } else {
          first = false;
        }
        if (val == null) {
          toReturn.append("null");
        } else {
          toReturn.append(encode(val, toSerialize).getPayload());
        }
      }
      toReturn.append(']');
      return StringQuoter.split(toReturn.toString());
    }

    if (value instanceof BaseProxy) {
      toSerialize.add((BaseProxy) value);
      AutoBean<BaseProxy> autoBean = AutoBeanUtils.getAutoBean((BaseProxy) value);
      value = BaseProxyCategory.stableId(autoBean);
    }

    if (value instanceof SimpleProxyId<?>) {
      return entitySource.getSerializedProxyId((SimpleProxyId<?>) value);
    }

    return ValueCodex.encode(value);
  }

  private <T> T cache(final String key, final T value) {
    if (value != null) {
      // map.put(key, value);
    }
    return value;
  }

  private Class<? extends EntityProxy> getProxyClass(final Splittable getProxyClass) {
    IdMessage idMessage =
        AutoBeanCodex.decode(MessageFactoryHolder.FACTORY, IdMessage.class, getProxyClass).as();
    return f.getProxyClass(idMessage.getTypeToken());
  }

  // private <T> void put(final String keyPrefix, final List<T> values) {
  // String valueType = null;
  // boolean isEntityProxy = false;
  // for (T value : values) {
  // if (valueType == null) {
  // if (value instanceof BaseEntityProxy) {
  // isEntityProxy = true;
  // valueType = f.getHistoryToken(((BaseEntityProxy) value).stableId().getProxyClass());
  // } else {
  // valueType = value.getClass().getName();
  // }
  // }
  // if (isEntityProxy) {
  // proxySerializers.get().serialize((BaseEntityProxy) value);
  // }
  // }
  // String key = keyPrefix + "@" + valueType + "@";
  // // map.put(key, values);
  // proxyStore.put(key, EntityCodex.encode(entitySource, values));
  // }

}
