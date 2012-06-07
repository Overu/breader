package com.goodow.web.mvp.shared.ioc;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.core.shared.LocalStorageProxyStore;
import com.goodow.web.mvp.shared.BaseActivity;
import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.mvp.shared.Default;
import com.goodow.web.mvp.shared.SimpleActivityMapper;
import com.goodow.web.mvp.shared.SimplePlaceHistoryMapper;
import com.goodow.web.mvp.shared.SimplePlaceTokenizer;
import com.goodow.web.security.shared.Auth;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryHandler.DefaultHistorian;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.ProxySerializer;
import com.google.web.bindery.requestfactory.shared.ProxyStore;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Logger;

public final class MvpGinModule extends AbstractGinModule {
  @Singleton
  public static class Binder {
  }

  @Singleton
  public static class BinderProvider implements Provider<Binder> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Inject
    private SimplePlaceHistoryMapper tokenizerMapBinder;
    @Inject
    private SimplePlaceTokenizer<BasePlace> simplePlaceTokenizer;
    @Inject
    @Default
    private SimpleActivityMapper simpleActivityMapper;
    @Inject
    private Provider<BaseActivity> simpleActivityProvider;

    @Override
    public Binder get() {
      logger.finest("EagerSingleton start");

      simpleActivityMapper.setName(Default.KEY);
      tokenizerMapBinder.addBinding(BasePlace.class, simplePlaceTokenizer, BasePlace.TOKEN_PREFIX);
      simpleActivityMapper.addBinding(BasePlace.class, simpleActivityProvider);

      logger.finest("EagerSingleton end");
      return null;
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();
    bind(PlaceHistoryMapper.class).to(SimplePlaceHistoryMapper.class).in(Singleton.class);
    bind(Historian.class).to(DefaultHistorian.class);

    bind(SimpleActivityMapper.class).annotatedWith(Default.class).to(SimpleActivityMapper.class)
        .in(Singleton.class);

    bind(new TypeLiteral<MapBinder<String, IsWidget>>() {
    }).in(Singleton.class);

    bind(ProxyStore.class).to(LocalStorageProxyStore.class).in(Singleton.class);
  }

  @Provides
  @Default
  @Singleton
  Place defaultPlaceProvider(final BasePlace place) {
    place.setPath(BasePlace.PATH_SEPARATOR);
    return place;
  }

  @Provides
  @Auth
  Place loginPlaceProvider(final BasePlace place) {
    place.setPath(Auth.PLACE_REQUEST_AUTH);
    return place;
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
      final com.google.gwt.place.shared.PlaceController placeController, final EventBus eventBus,
      @Default final Place place, final Historian historian) {
    PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(historyMapper, historian);
    placeHistoryHandler.register(placeController, eventBus, place);
    return placeHistoryHandler;
  }

  @Provides
  ProxySerializer proxySerializerProvider(final RequestFactory f, final ProxyStore proxyStore) {
    return f.getSerializer(proxyStore);
  }
}
