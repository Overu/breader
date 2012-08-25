package com.goodow.web.core.client;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebEntity;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class UIManager extends MapBinder<String, IsWidget> {

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

  public void render(final AcceptsOneWidget panel, final WebEntity entity, final String widgetId,
      final AsyncCallback<AcceptsOneWidget> callback) {
    WebEntity container = entity.getContainer();
    if (container != null) {
      render(panel, container, "container", new AsyncCallback<AcceptsOneWidget>() {
        @Override
        public void onFailure(final Throwable caught) {
          logger.info("Failured to load parent widget. Show " + this + " directly.");
          show(panel, entity, widgetId, callback);
        }

        @Override
        public void onSuccess(final AcceptsOneWidget result) {
          show(panel, entity, widgetId, callback);
        }
      });
    } else {
      show(panel, entity, widgetId, callback);
    }
  }

  public boolean show(final AcceptsOneWidget panel, final Resource resource) {
    return show(panel, resource, resource.getMimeType(), null);
  }

  public boolean show(final AcceptsOneWidget panel, final WebEntity entity, final String widgetId,
      final AsyncCallback<AcceptsOneWidget> callback) {
    IsWidget widget = get(widgetId);
    if (widget == null) {
      Provider<IsWidget> provider = getProvider(widgetId);
      if (provider != null) {
        widget = provider.get();
      }
    }

    if (widget == null) {
      AsyncProvider<IsWidget> provider = getAsyncProvider(widgetId);
      if (provider != null) {
        provider.get(new AsyncCallback<IsWidget>() {
          @Override
          public void onFailure(final Throwable caught) {
            String msg = "Failed to load view by id=" + widgetId;
            logger.info(msg);
            if (callback != null) {
              callback.onFailure(caught);
            }
          }

          @Override
          public void onSuccess(final IsWidget result) {
            appendWidget(panel, result, entity, callback);
          }
        });
        return true;
      } else {
        return false;
      }
    } else {
      appendWidget(panel, widget, entity, callback);
      return true;
    }
  }

  private void appendWidget(final AcceptsOneWidget panel, final IsWidget widget,
      final WebEntity entity, final AsyncCallback<AcceptsOneWidget> callback) {
    panel.setWidget(widget);
    if (widget instanceof TakesValue) {
      TakesValue<WebEntity> takesValue = (TakesValue<WebEntity>) widget;
      takesValue.setValue(entity);
    }
    if (callback != null && widget instanceof AcceptsOneWidget) {
      callback.onSuccess((AcceptsOneWidget) widget);
    }
  }

}
