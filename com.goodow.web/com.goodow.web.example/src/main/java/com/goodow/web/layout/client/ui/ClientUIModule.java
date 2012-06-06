package com.goodow.web.layout.client.ui;

import java.util.logging.Logger;

import com.google.gwt.inject.client.AbstractGinModule;

public final class ClientUIModule extends AbstractGinModule {

  // public static class Startup {
  // @Inject
  // public Startup(final WidgetRegistry widgetRegistry,
  // final AsyncProvider<RichTextEditor> editView, final PlaceHistoryHandler historyHandler,
  // final EventBus eventBus) {
  //
  // widgetRegistry.addBinding("view1").toInstance(new Label("Hello world!"));
  // widgetRegistry.addBinding("view2").toAsyncProvider(editView);
  //
  // SimplePanel panel = new SimplePanel();
  // RootPanel.get().add(panel);
  //
  // ActivityMapper activityMapper = new PageActivityMapper();
  // ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
  // activityManager.setDisplay(panel);
  //
  // historyHandler.handleCurrentHistory();
  // }
  // }

  private static final Logger logger = Logger.getLogger(ClientUIModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    // bind(Startup.class).asEagerSingleton();
    // bind(PlaceHistoryMapper.class).to(PageMapper.class).in(Singleton.class);

    // bind(new TypeLiteral<MapBinder<String, IsWidget>>() {
    // }).in(Singleton.class);
  }
  //
  // @Provides
  // @Singleton
  // PlaceController placeControllerProvider(final EventBus eventBus) {
  // PlaceController placeController = new PlaceController(eventBus);
  // return placeController;
  // }
  //
  // @Provides
  // @Singleton
  // PlaceHistoryHandler placeHistoryHandlerProvider(final PlaceHistoryMapper historyMapper,
  // final PlaceController placeController, final EventBus eventBus,
  // final Provider<Presenter> provider) {
  // PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
  // Presenter defaultPlace = provider.get();
  // defaultPlace.setPath("/");
  // placeHistoryHandler.register(placeController, eventBus, defaultPlace);
  // return placeHistoryHandler;
  // }
}
