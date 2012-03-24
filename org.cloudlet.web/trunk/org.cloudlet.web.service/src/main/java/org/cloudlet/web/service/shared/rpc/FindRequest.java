package org.cloudlet.web.service.shared.rpc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import org.cloudlet.web.service.shared.LocalStorage;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class FindRequest implements Request<BaseEntityProxy> {

  private final RequestFactory f;

  private final LocalStorage storage;

  private List<Request<? extends BaseEntityProxy>> reqs =
      new ArrayList<Request<? extends BaseEntityProxy>>();;

  private final Receiver<BaseEntityProxy> cacheReceiver = new Receiver<BaseEntityProxy>() {
    @Override
    public void onSuccess(final BaseEntityProxy response) {
      storage.put(response);
    }
  };

  @Inject
  FindRequest(final RequestFactory f, final LocalStorage storage) {
    this.f = f;
    this.storage = storage;
  }

  public FindRequest find(final EntityProxyId<? extends BaseEntityProxy> proxyId) {
    BaseEntityProxy proxy = storage.get(proxyId);
    if (proxy == null) {
      if (reqs.isEmpty()) {
        reqs.add(f.find(proxyId));
      } else {
        reqs.add(getRequestContext().find(proxyId));
      }
    }
    return this;
  }

  @Override
  public void fire() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void fire(final Receiver<? super BaseEntityProxy> receiver) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RequestContext getRequestContext() {
    return new RequestContext() {
      RequestContext ctx = reqs.isEmpty() ? null : reqs.get(0).getRequestContext();

      @Override
      public <T extends RequestContext> T append(final T other) {
        return ctx == null ? null : ctx.append(other);
      }

      @Override
      public <T extends BaseProxy> T create(final Class<T> clazz) {
        throw new UnsupportedOperationException();
      }

      @Override
      public <T extends BaseProxy> T edit(final T object) {
        throw new UnsupportedOperationException();
      }

      @Override
      public <P extends EntityProxy> Request<P> find(final EntityProxyId<P> proxyId) {
        return ctx == null ? null : ctx.find(proxyId);
      }

      @Override
      public void fire() {
        fire(null);
      }

      @Override
      public void fire(final Receiver<Void> receiver) {
        if (!reqs.isEmpty()) {
          for (Request<? extends BaseEntityProxy> req : reqs) {
            req.to(cacheReceiver);
          }
          ctx.fire(new Receiver<Void>() {

            @Override
            public void onSuccess(final Void response) {
              callback(receiver);
            }
          });
          reqs.clear();
        } else {
          callback(receiver);
        }
      }

      @Override
      public RequestFactory getRequestFactory() {
        return ctx == null ? null : ctx.getRequestFactory();
      }

      @Override
      public boolean isChanged() {
        throw new UnsupportedOperationException();
      }

      private void callback(final Receiver<Void> receiver) {
        if (receiver == null) {
          return;
        }
        receiver.onSuccess(null);
      }
    };
  }

  @Override
  public RequestContext to(final Receiver<? super BaseEntityProxy> receiver) {
    throw new UnsupportedOperationException();
  }

  @Override
  public FindRequest with(final String... propertyRefs) {
    if (!reqs.isEmpty()) {
      reqs.get(reqs.size() - 1).with(propertyRefs);
    }
    return this;
  }
}
