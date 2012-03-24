package org.cloudlet.web.mvp.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BasePlace extends Place {
  public static final BasePlace NOWHERE = new BasePlace(null, null);
  private Map<String, String[]> params;;
  private Map<Class<?>, EntityProxyId<?>> proxyClassToIds;

  public static final String TOKEN_PREFIX = "";
  private static final String ID = "id";

  private String path;

  public static final String PATH_SEPARATOR = "/";

  private final PlaceHistoryMapper placeHistoryMapper;
  private final RequestFactory f;

  @Inject
  protected BasePlace(final PlaceHistoryMapper placeHistoryMapper, final RequestFactory f) {
    this.placeHistoryMapper = placeHistoryMapper;
    this.f = f;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BasePlace other = (BasePlace) obj;
    if (getPath() == null) {
      if (other.getPath() != null) {
        return false;
      }
    } else if (!getPath().equals(other.getPath())) {
      return false;
    }
    if (!equalsWithArray(params, other.params)) {
      return false;
    }
    return true;
  }

  public <T extends EntityProxy> EntityProxyId<T> getParam(final Class<T> proxyClass) {
    return proxyClassToIds == null ? null : (EntityProxyId<T>) proxyClassToIds.get(proxyClass);
  }

  public String[] getParam(final String key) {
    return params == null ? null : params.get(key);
  }

  /**
   * inject @RequestParameters Provider<Map<String, String[]>> paramsProvider directly.
   * 
   * @return
   */
  public Map<String, String[]> getParams() {
    ensureParamsMap();
    return params;
  }

  public String getPath() {
    return path;
  }

  public <T extends EntityProxy> Class<T> getProxyClass() {
    try {
      return (Class<T>) f.getProxyClass(getPath());
    } catch (RuntimeException e) {
      if (proxyClassToIds == null || proxyClassToIds.isEmpty()) {
        return null;
      }
      for (Class<?> clazz : proxyClassToIds.keySet()) {
        return (Class<T>) clazz;
      }
      throw new AssertionError();
    }
  }

  public <T extends EntityProxy> EntityProxyId<T> getProxyId() {
    try {
      return f.getProxyId(getPath());
    } catch (RuntimeException e) {
      if (proxyClassToIds == null || proxyClassToIds.isEmpty()) {
        return null;
      }
      for (EntityProxyId<?> id : proxyClassToIds.values()) {
        return (EntityProxyId<T>) id;
      }
      throw new AssertionError();
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((params == null) ? 0 : params.hashCode());
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  /**
   * Remove a query parameter from the map.
   * 
   * @param key the parameter name
   */
  public BasePlace removeParameter(final String key) {
    if (params != null) {
      params.remove(key);
    }
    return this;
  }

  public BasePlace setParameter(final EntityProxyId<?>... proxyIds) {
    assertNotNull(proxyIds, "ProxyIds cannot null. Try using removeParameter instead.");
    if (proxyIds.length == 0) {
      throw new IllegalArgumentException(
          "Values cannot be empty.  Try using removeParameter instead.");
    }
    List<String> values = new ArrayList<String>();
    for (EntityProxyId<?> id : proxyIds) {
      values.add(f.getHistoryToken(id));
    }
    setParameter(ID, values.toArray(new String[0]));
    return this;
  }

  /**
   * <p>
   * Set a query parameter to a list of values. Each value in the list will be added as its own
   * key/value pair.
   * 
   * <p>
   * <h3>Example Output</h3>
   * <code>#path?mykey=value0&mykey=value1&mykey=value2</code>
   * </p>
   * 
   * @param key the key
   * @param values the list of values
   */
  public BasePlace setParameter(final String key, final String... values) {
    ensureParamsMap();
    assertNotNullOrEmpty(key, "Key cannot be null or empty", false);
    assertNotNull(values, "Values cannot null. Try using removeParameter instead.");
    if (values.length == 0) {
      throw new IllegalArgumentException(
          "Values cannot be empty.  Try using removeParameter instead.");
    }
    params.put(key, values);
    if (ID.equals(key)) {
      ensureProxyClassToIdsMap();
    }
    return this;
  }

  public BasePlace setPath(final String path) {
    this.path = path;
    return this;
  }

  @Override
  public String toString() {
    return placeHistoryMapper.getToken(this);
  }

  /**
   * Assert that the value is not null.
   * 
   * @param value the value
   * @param message the message to include with any exceptions
   * @throws IllegalArgumentException if value is null
   */
  private void assertNotNull(final Object value, final String message)
      throws IllegalArgumentException {
    if (value == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Assert that the value is not null or empty.
   * 
   * @param value the value
   * @param message the message to include with any exceptions
   * @param isState if true, throw a state exception instead
   * @throws IllegalArgumentException if value is null
   * @throws IllegalStateException if value is null and isState is true
   */
  private void assertNotNullOrEmpty(final String value, final String message, final boolean isState)
      throws IllegalArgumentException {
    if (value == null || value.length() == 0) {
      if (isState) {
        throw new IllegalStateException(message);
      } else {
        throw new IllegalArgumentException(message);
      }
    }
  }

  private void ensureParamsMap() {
    if (params == null) {
      params = new HashMap<String, String[]>();
    }
  }

  private void ensureProxyClassToIdsMap() {
    if (proxyClassToIds == null) {
      proxyClassToIds = new LinkedHashMap<Class<?>, EntityProxyId<?>>();
    }
    String[] tokens = params.get(ID);
    if (tokens == null) {
      return;
    }
    for (String token : tokens) {
      EntityProxyId<EntityProxy> proxyId = f.getProxyId(token);
      proxyClassToIds.put(proxyId.getProxyClass(), proxyId);
    }
  }

  private <K, V> boolean equalsWithArray(final Map<K, V[]> o1, final Map<K, V[]> o2) {
    if (o1 == o2) {
      return true;
    }
    if (o1 == null) {
      if (o2 != null) {
        return false;
      }
    }
    if (o1.size() != o2.size()) {
      return false;
    }

    try {
      Iterator<Entry<K, V[]>> i = o1.entrySet().iterator();
      while (i.hasNext()) {
        Entry<K, V[]> e = i.next();
        K key = e.getKey();
        V[] value = e.getValue();
        if (value == null) {
          if (!(o2.get(key) == null && o2.containsKey(key))) {
            return false;
          }
        } else {
          if (!Arrays.deepEquals(value, o2.get(key))) {
            return false;
          }
        }
      }
    } catch (ClassCastException unused) {
      return false;
    } catch (NullPointerException unused) {
      return false;
    }

    return true;
  }

}
