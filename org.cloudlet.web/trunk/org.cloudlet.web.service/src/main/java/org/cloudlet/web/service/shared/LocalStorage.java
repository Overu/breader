package org.cloudlet.web.service.shared;

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

import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

import java.util.List;
import java.util.Set;

@Singleton
public class LocalStorage {
  private static final String ANY_SEPARATOR_PATTERN = "@[abc]@";
  public static final String PROXY_SEPARATOR = "@a@";
  public static final String LIST_SEPARATOR = "@b@";
  public static final String SET_SEPARATOR = "@c@";

  private static Splittable encode(final EntitySource source, Object value) {
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
          toReturn.append(encode(source, val).getPayload());
        }
      }
      toReturn.append(']');
      return StringQuoter.split(toReturn.toString());
    }

    if (value instanceof BaseProxy) {
      proxySerializer.serialize((BaseProxy) value);
      AutoBean<BaseProxy> autoBean = AutoBeanUtils.getAutoBean((BaseProxy) value);
      value = BaseProxyCategory.stableId(autoBean);
    }

    if (value instanceof SimpleProxyId<?>) {
      return source.getSerializedProxyId((SimpleProxyId<?>) value);
    }

    return ValueCodex.encode(value);
  }

  private final Provider<ProxySerializer> proxySerializers;
  private final ProxyStore proxyStore;

  // private final Map<String, Object> map;

  private final EntitySource entitySource;

  private static ProxySerializer proxySerializer;

  @Inject
  private RequestFactory f;

  @Inject
  LocalStorage(final Provider<ProxySerializer> proxySerializers, final ProxyStore proxyStore) {
    // map = new HashMap<String, Object>();
    this.proxySerializers = proxySerializers;
    this.proxyStore = proxyStore;
    proxySerializer = proxySerializers.get();
    this.entitySource = (EntitySource) proxySerializers.get();
  }

  public <T> List<T> get(final EntityProxyId<?> proxyId, final Class<T> valueType) {
    return get(f.getHistoryToken(proxyId), valueType);
  }

  public <T extends BaseEntityProxy> T get(final EntityProxyId<T> id) {
    String historyToken = f.getHistoryToken(id);
    T toReturn = null;
    // toReturn = (T) map.get(historyToken);
    // if (toReturn != null) {
    // return toReturn;
    // }
    toReturn = proxySerializers.get().deserialize(id);
    return cache(historyToken, toReturn);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(final String key) {
    if (key == null) {
      return null;
    }
    if (!needKey(key)) {
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

  @SuppressWarnings("unchecked")
  public <T> List<T> get(final String keyPrefix, final Class<T> valueType) {
    String valueTypeToken = null;
    try {
      valueTypeToken = f.getHistoryToken((Class<? extends BaseEntityProxy>) valueType);
    } catch (Exception e) {
      valueTypeToken = valueType.getName();
    }
    String key = keyPrefix + "@" + valueTypeToken + "@";
    List<T> toReturn = null;
    // toReturn = (List<T>) map.get(key);
    // if (toReturn != null) {
    // return toReturn;
    // }
    toReturn =
        (List<T>) EntityCodex.decode(entitySource, List.class, valueType, proxyStore.get(key));

    return cache(key, toReturn);
  }

  public String put(final BaseEntityProxy proxy) {
    // map.put(f.getHistoryToken(proxy.stableId()), proxy);
    return proxySerializers.get().serialize(proxy);
  }

  public <T> void put(final EntityProxyId<?> proxyId, final List<T> values) {
    put(f.getHistoryToken(proxyId), values);
  }

  public <T> void put(final String keyPrefix, final List<T> values) {
    String valueType = null;
    boolean isEntityProxy = false;
    for (T value : values) {
      if (valueType == null) {
        if (value instanceof BaseEntityProxy) {
          isEntityProxy = true;
          valueType = f.getHistoryToken(((BaseEntityProxy) value).stableId().getProxyClass());
        } else {
          valueType = value.getClass().getName();
        }
      }
      if (isEntityProxy) {
        proxySerializers.get().serialize((BaseEntityProxy) value);
      }
    }
    String key = keyPrefix + "@" + valueType + "@";
    // map.put(key, values);
    proxyStore.put(key, EntityCodex.encode(entitySource, values));
  }

  public String put(final String key, final Object value) {
    if (key == null) {
      return null;
    }
    if (!needKey(key)) {
      assert value instanceof BaseEntityProxy;
      BaseEntityProxy proxy = (BaseEntityProxy) value;
      // map.put(f.getHistoryToken(proxy.stableId()), proxy);
      return proxySerializers.get().serialize(proxy);
    }
    // map.put(key, values);
    proxyStore.put(key, encode(entitySource, value));
    return key;
  }

  public void remove(final EntityProxyId<? extends EntityProxy> id) {

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

  private boolean needKey(final String key) {
    return key.matches(".*" + ANY_SEPARATOR_PATTERN + ".*");
  }

}
