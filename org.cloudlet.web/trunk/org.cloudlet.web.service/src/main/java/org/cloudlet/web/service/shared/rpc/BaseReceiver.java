package org.cloudlet.web.service.shared.rpc;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.impl.AbstractRequestContext;

import org.cloudlet.web.service.shared.LocalStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public abstract class BaseReceiver<V> extends Receiver<V> {
  private static final Logger logger = Logger.getLogger(BaseReceiver.class.getName());
  private static final Set<AbstractRequestContext> toFires = new HashSet<AbstractRequestContext>();
  @Inject
  private static LocalStorage storage;
  @Inject
  private static RequestFactory f;

  private String key;

  public void fire() {
    fire(null);
  }

  public void fire(final Receiver<Void> receiver) {
    V value = storage.get(key);
    if (value != null) {
      onSuccessAndCached(value);
      return;
    }
    final AbstractRequestContext openContextImpl =
        (AbstractRequestContext) provideRequest().to(this);
    openContextImpl.setFireDisabled(true);
    toFires.add(openContextImpl);
    Scheduler.get().scheduleFinally(new ScheduledCommand() {
      @Override
      public void execute() {
        if (toFires.isEmpty()) {
          return;
        }
        for (AbstractRequestContext ctx : toFires) {
          ctx.setFireDisabled(false);
          ctx.fire();
        }
        toFires.clear();
      }
    });
    if (receiver != null) {
      // Queue a final callback receiver
      openContextImpl.fire(receiver);
    }
  }

  @Override
  public void onSuccess(final V response) {
    onSuccessAndCached(response);
    storage.put(key, response);
  }

  public abstract void onSuccessAndCached(final V response);

  public abstract Request<V> provideRequest();

  public BaseReceiver<V> setKeyForList(final EntityProxyId<?> parentId, final String listKey) {
    this.key = f.getHistoryToken(parentId) + LocalStorage.LIST_SEPARATOR + listKey;
    return this;
  }

  public BaseReceiver<V> setKeyForList(final String listKey) {
    this.key = LocalStorage.LIST_SEPARATOR + listKey;
    return this;
  };

  public BaseReceiver<V> setKeyForProxy(final EntityProxyId<?> proxyId) {
    this.key = f.getHistoryToken(proxyId);
    return this;
  }

  public BaseReceiver<V> setKeyForProxy(final EntityProxyId<?> parentId, final String key) {
    this.key = f.getHistoryToken(parentId) + LocalStorage.PROXY_SEPARATOR + key;
    return this;
  }

}
