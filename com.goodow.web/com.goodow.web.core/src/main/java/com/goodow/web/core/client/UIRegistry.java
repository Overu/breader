package com.goodow.web.core.client;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.core.shared.Resource;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class UIRegistry extends MapBinder<String, IsWidget> {

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

  public void showResource(final AcceptsOneWidget panel, final IsWidget widget,
      final Resource resource) {
    panel.setWidget(widget);
    if (widget instanceof TakesValue) {
      TakesValue<Resource> takesValue = (TakesValue<Resource>) widget;
      takesValue.setValue(resource);
    }
  }

  public void showWidget(final AcceptsOneWidget panel, final Resource resource) {
    final String widgetId = resource.getMimeType();
    IsWidget widget = get(widgetId);
    if (widget == null && getProvider(widgetId) != null) {
      widget = getProvider(widgetId).get();
    }
    if (widget != null) {
      showResource(panel, widget, resource);
    } else {
      AsyncProvider<IsWidget> provider = getAsyncProvider(widgetId);
      if (provider != null) {
        provider.get(new AsyncCallback<IsWidget>() {
          @Override
          public void onFailure(final Throwable caught) {
            String msg = "Failed to load view by id=" + widgetId;
            logger.info(msg);
          }

          @Override
          public void onSuccess(final IsWidget result) {
            showResource(panel, result, resource);
          }
        });
      }
    }
  }

}
