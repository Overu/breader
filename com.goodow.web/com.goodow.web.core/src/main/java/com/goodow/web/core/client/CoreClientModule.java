package com.goodow.web.core.client;

import com.goodow.web.core.client.css.AppBundle;
import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.core.shared.WebPlaceMapper;
import com.goodow.web.core.shared.WebPlatform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort.DENSITY;

import java.util.logging.Logger;

public class CoreClientModule extends AbstractGinModule {
  @Singleton
  public static class Binder {
    @Inject
    public Binder(final GwtJSONObjectProvider<WebObject> provider) {
      CorePackage.WebObject.as().addReader(JSONObject.class, provider);
      CorePackage.WebObject.as().addWriter(JSONObject.class, provider);
    }
  }

  @Singleton
  public static class Render implements PlaceChangeEvent.Handler {
    @Inject
    @HomePlace
    WebPlace homePlace;

    @Inject
    EventBus eventBus;

    @Inject
    PlaceController placeController;

    @Inject
    MGWTPlaceHistoryHandler historyHandler;

    SimplePanel main;

    @Inject
    public Render() {
      new Timer() {
        @Override
        public void run() {
          start();
        }
      }.schedule(1);
    }

    @Override
    public void onPlaceChange(final PlaceChangeEvent event) {
      WebPlace place = (WebPlace) event.getNewPlace();
      if (place.getWelcomePlace() != null) {
        gotoPlace(place.getWelcomePlace());
      } else {
        place.render(main);
      }
    }

    private void gotoPlace(final WebPlace place) {
      logger.info("Loading " + place.getUri());
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          placeController.goTo(place);
        }
      });
    }

    private void start() {
      ViewPort viewPort = new MGWTSettings.ViewPort();
      viewPort.setTargetDensity(DENSITY.MEDIUM);
      viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(
          1.0);

      MGWTSettings settings = new MGWTSettings();
      settings.setViewPort(viewPort);
      settings.setIconUrl("logo.png");
      settings.setAddGlosToIcon(true);
      settings.setFullscreen(true);
      settings.setPreventScrolling(true);

      MGWT.applySettings(settings);

      // very nasty workaround because GWT does not correctly support @resource
      StyleInjector.inject(AppBundle.INSTANCE.css().getText());

      main = new SimplePanel();
      main.getElement().setId("main");
      RootPanel.get().add(main);

      eventBus.addHandler(PlaceChangeEvent.TYPE, this);

      historyHandler.handleCurrentHistory();
    }
  }

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("configure");
    requestStaticInjection(WebPlatform.class);
    bind(Message.class).to(ClientMessage.class);
    bind(PlaceHistoryMapper.class).to(WebPlaceMapper.class).in(Singleton.class);
    bind(Binder.class).asEagerSingleton();
    bind(Render.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  PhoneGap phoneGap() {
    PhoneGap phoneGap = GWT.create(PhoneGap.class);
    phoneGap.initializePhoneGap();
    return phoneGap;
  }

  @Provides
  @Singleton
  PlaceController placeControllerProvider(final EventBus eventBus) {
    PlaceController placeController = new PlaceController(eventBus);
    return placeController;
  }

  @Provides
  @Singleton
  MGWTPlaceHistoryHandler placeHistoryHandlerProvider(final PlaceHistoryMapper historyMapper,
      final PlaceController placeController, final EventBus eventBus,
      final AppHistoryObserver historyObserver, @HomePlace final WebPlace homePlace) {
    MGWTPlaceHistoryHandler placeHistoryHandler =
        new MGWTPlaceHistoryHandler(historyMapper, historyObserver);
    placeHistoryHandler.register(placeController, eventBus, homePlace);
    return placeHistoryHandler;
  }

}