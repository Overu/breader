package com.goodow.web.workbench.client;

import java.util.logging.Logger;

import com.goodow.web.layout.client.ui.FlexPanel;
import com.goodow.web.layout.client.ui.PageActivityMapper;
import com.goodow.web.layout.client.ui.PagePresenterMapper;
import com.goodow.web.layout.client.ui.Presenter;
import com.goodow.web.layout.client.ui.WidgetRegistry;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

public class ClientWorkbenchGinModule extends AbstractGinModule {

  @Singleton
  public static class Binder {
    @Inject
    public Binder(final AsyncProvider<TextArea> editView, final WidgetRegistry widgetRegistry) {
      widgetRegistry.addBinding("view1").toInstance(new Label("视图1"));
      widgetRegistry.addBinding("view2").toAsyncProvider(editView);
    }
  }

  @Singleton
  public static class Render {
    @Inject
    public Render(final PlaceHistoryHandler historyHandler,
        final Provider<FlexPanel> panelProvider, final EventBus eventBus) {
      SimplePanel panel = new SimplePanel();
      RootPanel.get().add(panel);

      ActivityMapper activityMapper = new PageActivityMapper();
      ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
      activityManager.setDisplay(panel);

      historyHandler.handleCurrentHistory();

      //
      // Label loading = new Label("Loading");
      // RootPanel.get().add(loading);
    }
  }

  private static final Logger logger = Logger.getLogger(ClientWorkbenchGinModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("configure");
    bind(PlaceHistoryMapper.class).to(PagePresenterMapper.class).in(Singleton.class);
    // bind(LayoutRequestFactory.class).in(Singleton.class);
    bind(Binder.class).asEagerSingleton();
    bind(Render.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  PlaceController placeControllerProvider(final EventBus eventBus) {
    PlaceController placeController = new PlaceController(eventBus);
    return placeController;
  }

  @Provides
  @Singleton
  PlaceHistoryHandler placeHistoryHandlerProvider(final PlaceHistoryMapper historyMapper,
      final PlaceController placeController, final EventBus eventBus,
      final Provider<Presenter> provider) {
    PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
    Presenter p = provider.get();
    // p.setUri("");
    placeHistoryHandler.register(placeController, eventBus, p);
    return placeHistoryHandler;
  }
}