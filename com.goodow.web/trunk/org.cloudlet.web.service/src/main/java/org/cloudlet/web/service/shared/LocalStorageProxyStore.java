package org.cloudlet.web.service.shared;

import com.google.gwt.storage.client.Storage;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.google.web.bindery.requestfactory.shared.DefaultProxyStore;
import com.google.web.bindery.requestfactory.shared.ProxyStore;

import java.util.logging.Logger;

@Singleton
public class LocalStorageProxyStore implements ProxyStore {
  @Inject
  private DefaultProxyStore cache;
  private Storage storage;
  String versionKey = "operationMessage.version";
  private final static Logger logger = Logger.getLogger(LocalStorageProxyStore.class.getName());

  private static final String EXPECTED_VERSION = "5";

  @Inject
  LocalStorageProxyStore() {
    storage = Storage.getLocalStorageIfSupported();

    String version = storage.getItem(versionKey);
    if (version != null && !EXPECTED_VERSION.equals(version)) {
      logger.warning("本地数据不兼容于新版本,将被清除");
      storage.clear();
    }
    storage.setItem(versionKey, EXPECTED_VERSION);
  }

  @Override
  public Splittable get(final String key) {
    Splittable toReturn = null;
    // Splittable splittable = cache.get(key);
    // if (splittable != null) {
    // return splittable;
    // }
    String item = storage.getItem(key);
    if (item != null) {
      toReturn = StringQuoter.split(item);
    }
    return toReturn;
  }

  @Override
  public int nextId() {
    return storage.getLength();
  }

  @Override
  public void put(final String key, final Splittable value) {
    // cache.put(key, value);
    storage.setItem(key, value.getPayload());
  }
}
