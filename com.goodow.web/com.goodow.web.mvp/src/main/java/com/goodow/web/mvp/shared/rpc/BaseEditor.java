package com.goodow.web.mvp.shared.rpc;

import com.goodow.web.core.shared.LocalStorage;
import com.goodow.web.core.shared.rpc.BaseContext;
import com.goodow.web.core.shared.rpc.BaseEntityProxy;
import com.goodow.web.core.shared.rpc.BaseReceiver;
import com.goodow.web.mvp.shared.BasePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.Callback;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.impl.SimpleProxyId;


import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

public abstract class BaseEditor<T extends BaseEntityProxy> extends Composite implements Editor<T>,
    Activity {
  @Inject
  protected PlaceController placeController;
  @Inject
  protected LocalStorage storage;
  protected Logger logger = Logger.getLogger(getClass().getName());
  @Inject
  private DefaultValueSetter defaultValueSetter;
  protected T proxy;
  @Inject
  private RequestFactory f;

  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    // if (super.getWidget() == null) {
    // initEditor();
    // }
    final EntityProxyId<T> proxyId = ((BasePlace) placeController.getWhere()).getProxyId();
    if (proxyId == null) {
      create();
      return;
    }
    BaseReceiver<T> receiver = new BaseReceiver<T>() {

      @Override
      public void onConstraintViolation(final Set<ConstraintViolation<?>> violations) {
        provideEditorDriver().setConstraintViolations(violations);
      }

      @Override
      public void onSuccessAndCached(final T response) {
        edit(response, provideRequestContext());
      }

      @Override
      public Request<T> provideRequest() {
        return f.find(proxyId).with(provideEditorDriver().getPaths());
      }
    };
    receiver.setKeyForProxy(proxyId).fire();

  }

  protected void create() {
    BaseContext context = provideRequestContext();
    BasePlace basePlace = (BasePlace) placeController.getWhere();
    T proxy =
        defaultValueSetter.<T> setDefaultValue(context.<T> create(basePlace.<T> getProxyClass()));
    edit(proxy, context);
  }

  protected void edit(final T proxy, final BaseContext context) {
    provideEditorDriver().edit(proxy, context);
    provideEditorDriver().accept(new EditorVisitor() {
      @SuppressWarnings("unchecked")
      @Override
      public <P> boolean visit(final EditorContext<P> ctx) {
        BaseEditor.this.proxy = (T) ctx.getFromModel();
        return false;
      }
    });
  }

  protected void initEditor() {
    initWidget((Widget) provideUiBinder().createAndBindUi(this));
    provideEditorDriver().initialize(this);
  }

  protected abstract <E extends BaseEditor<? super T>> RequestFactoryEditorDriver<T, E> provideEditorDriver();

  protected abstract BaseContext provideRequestContext();

  protected abstract UiBinder provideUiBinder();

  protected void put(final Callback<Void, ServerFailure> callback) {
    final BaseContext context = (BaseContext) provideEditorDriver().flush();

    BaseReceiver<Void> receiver = new BaseReceiver<Void>() {

      @Override
      public void onConstraintViolation(final Set<ConstraintViolation<?>> violations) {
        provideEditorDriver().setConstraintViolations(violations);
      }

      @Override
      public void onFailure(final ServerFailure error) {
        if (callback == null) {
          super.onFailure(error);
        } else {
          callback.onFailure(error);
        }
      }

      @Override
      public void onSuccessAndCached(final Void response) {
        if (callback == null) {
          logger.info("已保存");
          edit(proxy, provideRequestContext());
        } else {
          callback.onSuccess(null);
        }
        storage.put(BaseEditor.this.proxy);
      }

      @Override
      public Request<Void> provideRequest() {
        return context.put(proxy);
      }
    };
    receiver.fire();
  }

  protected void remove(final Callback<Void, ServerFailure> callback) {
    if (((SimpleProxyId<?>) proxy.stableId()).isEphemeral()) {
      History.back();
      return;
    }

    BaseReceiver<Void> receiver = new BaseReceiver<Void>() {

      @Override
      public void onFailure(final ServerFailure error) {
        if (callback == null) {
          super.onFailure(error);
        } else {
          callback.onFailure(error);
        }
      }

      @Override
      public void onSuccessAndCached(final Void response) {
        if (callback == null) {
          logger.info("删除成功");
          History.back();
        } else {
          callback.onSuccess(null);
        }
        // storage.remove(BaseEditor.this.proxy.stableId());
      }

      @Override
      public Request<Void> provideRequest() {
        return provideRequestContext().remove(proxy);
      }
    };
    receiver.fire();
  }
}
