package com.goodow.web.core.client;

import com.goodow.web.core.client.css.AppBundle;
import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.MyPlace;
import com.goodow.web.core.shared.MyPlaceMapper;
import com.goodow.web.core.shared.WebObject;
import com.goodow.web.core.shared.WebPlatform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort.DENSITY;
import com.googlecode.mgwt.ui.client.dialog.TabletPortraitOverlay;
import com.googlecode.mgwt.ui.client.layout.MasterRegionHandler;
import com.googlecode.mgwt.ui.client.layout.OrientationRegionHandler;

import java.util.logging.Logger;

public class CoreClientModule extends AbstractGinModule {
  @Singleton
  public static class Binder {
    @Inject
    public Binder(final AsyncProvider<TextArea> editView, final UIRegistry widgetRegistry,
        final AsyncProvider<Shell> shell, final GwtJSONObjectProvider<WebObject> provider) {
      CorePackage.WebObject.as().addReader(JSONObject.class, provider);
      CorePackage.WebObject.as().addWriter(JSONObject.class, provider);
      widgetRegistry.addBinding("view1").toInstance(new Label("视图1"));
      widgetRegistry.addBinding("view2").toAsyncProvider(editView);
      widgetRegistry.addBinding("main").toAsyncProvider(shell);
    }
  }

  @Singleton
  public static class Render {

    private final EventBus eventBus;
    private final PlaceController placeController;

    private final MGWTPlaceHistoryHandler historyHandler;
    private final Provider<TabletNavActivityMapper> navActivityMapper;
    private final MyAnimationMapper animationMapper;
    private final TabletMainActivityMapper tabletMainActivityMapper;

    @Inject
    public Render(final PlaceController placeController,
        final MGWTPlaceHistoryHandler historyHandler, final EventBus eventBus,
        final Provider<TabletNavActivityMapper> tabletNavActivityMapper,
        final MyAnimationMapper navAnimationMapper,
        final TabletMainActivityMapper tabletMainActivityMapper) {
      // SimplePanel panel = new SimplePanel();
      // RootPanel.get().add(panel);
      //
      // ActivityMapper activityMapper = new PageActivityMapper();
      // ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
      // activityManager.setDisplay(panel);
      //
      // historyHandler.handleCurrentHistory();

      this.placeController = placeController;
      this.historyHandler = historyHandler;
      this.eventBus = eventBus;
      this.navActivityMapper = tabletNavActivityMapper;
      this.animationMapper = navAnimationMapper;
      this.tabletMainActivityMapper = tabletMainActivityMapper;
      new Timer() {
        @Override
        public void run() {
          start();

        }
      }.schedule(1);

      alertSomeStuff();

    }

    private native void alertSomeStuff()/*-{

                                        //    $doc.addEventListener("scroll", (function() {
                                        //      alert('scroll');
                                        //    }), true);
                                        }-*/;

    private void createPhoneDisplay() {
      AnimatableDisplay display = GWT.create(AnimatableDisplay.class);

      PhoneActivityMapper appActivityMapper = new PhoneActivityMapper();

      AnimatingActivityManager activityManager =
          new AnimatingActivityManager(appActivityMapper, animationMapper, eventBus);

      activityManager.setDisplay(display);

      RootPanel.get().add(display);

    }

    private void createTabletDisplay() {
      SimplePanel navContainer = new SimplePanel();
      navContainer.getElement().setId("nav");
      navContainer.getElement().addClassName("landscapeonly");
      AnimatableDisplay navDisplay = GWT.create(AnimatableDisplay.class);

      final TabletPortraitOverlay tabletPortraitOverlay = new TabletPortraitOverlay();

      new OrientationRegionHandler(navContainer, tabletPortraitOverlay, navDisplay);
      new MasterRegionHandler(eventBus, "nav", tabletPortraitOverlay);

      AnimatingActivityManager navActivityManager =
          new AnimatingActivityManager(navActivityMapper.get(), animationMapper, eventBus);

      navActivityManager.setDisplay(navDisplay);

      RootPanel.get().add(navContainer);

      SimplePanel mainContainer = new SimplePanel();
      mainContainer.getElement().setId("main");
      AnimatableDisplay mainDisplay = GWT.create(AnimatableDisplay.class);

      AnimatingActivityManager mainActivityManager =
          new AnimatingActivityManager(tabletMainActivityMapper, animationMapper, eventBus);

      mainActivityManager.setDisplay(mainDisplay);
      mainContainer.setWidget(mainDisplay);

      RootPanel.get().add(mainContainer);

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

      if (MGWT.getOsDetection().isTablet()) {

        // very nasty workaround because GWT does not corretly support
        // @media
        StyleInjector.inject(AppBundle.INSTANCE.css().getText());

        createTabletDisplay();
      } else {

        createPhoneDisplay();

      }

      historyHandler.handleCurrentHistory();
    }
  }

  private static final Logger logger = Logger.getLogger(CoreClientModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("configure");
    requestStaticInjection(WebPlatform.class);
    bind(Message.class).to(ClientMessage.class);
    bind(PlaceHistoryMapper.class).to(MyPlaceMapper.class).in(Singleton.class);
    bind(Binder.class).asEagerSingleton();
    bind(Render.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  PhoneGap phoneGap() {
    PhoneGap gap = GWT.create(PhoneGap.class);
    return gap;
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
      final AppHistoryObserver historyObserver) {
    MGWTPlaceHistoryHandler placeHistoryHandler =
        new MGWTPlaceHistoryHandler(historyMapper, historyObserver);
    placeHistoryHandler
        .register(placeController, eventBus, new MyPlace(Animation.FADE, "", "home"));
    return placeHistoryHandler;
  }

}