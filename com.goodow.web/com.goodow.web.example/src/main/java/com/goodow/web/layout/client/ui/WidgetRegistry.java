package com.goodow.web.layout.client.ui;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Singleton;

@Singleton
public class WidgetRegistry extends MapBinder<String, IsWidget> {

  public void showWidget(final AcceptsOneWidget panel, final String widgetId) {
    IsWidget widget = get(widgetId);
    if (widget != null) {
      panel.setWidget(widget);
    } else {
      AsyncProvider<IsWidget> provider = getAsyncProvider(widgetId);
      if (provider != null) {
        provider.get(new AsyncCallback<IsWidget>() {
          @Override
          public void onFailure(final Throwable caught) {
            panel.setWidget(new Label("Failed to load view by id=" + widgetId));
          }

          @Override
          public void onSuccess(final IsWidget result) {
            panel.setWidget(result);
          }
        });
      }
    }
  }
}
