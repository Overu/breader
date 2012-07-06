package com.goodow.web.core.client;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.core.shared.MyPlace;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

@Singleton
public class UIRegistry extends MapBinder<String, IsWidget> {
  @Inject
  PlaceController placeController;

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

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
            String msg = "Failed to load view by id=" + widgetId;
            logger.info(msg);

            MyView view = new MyView();

            MyPlace place = (MyPlace) placeController.getWhere();

            view.setTitle(place.getTitle());
            panel.setWidget(view);

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
