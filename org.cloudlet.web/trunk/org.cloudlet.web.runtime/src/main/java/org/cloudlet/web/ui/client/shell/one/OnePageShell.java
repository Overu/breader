package org.cloudlet.web.ui.client.shell.one;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;
import org.cloudlet.web.mvp.shared.layout.Footer;
import org.cloudlet.web.mvp.shared.layout.Nav;
import org.cloudlet.web.mvp.shared.layout.Search;

import java.util.logging.Logger;

/**
 * Application shell.
 */
@Singleton
public final class OnePageShell extends Composite {

  interface ShellUiBinder extends UiBinder<Widget, OnePageShell> {
  }

  private static ShellUiBinder uiBinder = GWT.create(ShellUiBinder.class);

  private final Logger logger = Logger.getLogger(getClass().getName());

  @UiField
  HTMLPanel root;

  @UiField
  ActivityPanel center;

  @UiField
  ActivityPanel search;

  @UiField
  ActivityPanel nav;

  @UiField
  ActivityPanel footer;

  private final Provider<ActivityPanel> activityPanelProvider;

  @Inject
  public OnePageShell(Provider<ActivityPanel> activityPanelProvider,
      @Default SimpleActivityMapper centerActivityMapper,
      @Search SimpleActivityMapper searchActivityMapper,
      @Nav SimpleActivityMapper navActivityMapper,
      @Footer SimpleActivityMapper footerActivityMapper, EventBus eventBus) {
    this.activityPanelProvider = activityPanelProvider;
    logger.finest("init start");

    // Initialize the ui binder.
    uiBinder = GWT.create(ShellUiBinder.class);
    initWidget(uiBinder.createAndBindUi(this));

    ActivityManager searchActivityManager = new ActivityManager(searchActivityMapper, eventBus);
    search.ensureDebugId("search");
    searchActivityManager.setDisplay(search);

    ActivityManager leftNavActivityManager = new ActivityManager(navActivityMapper, eventBus);
    nav.ensureDebugId("leftNav");
    leftNavActivityManager.setDisplay(nav);

    ActivityManager footerActivityManager = new ActivityManager(footerActivityMapper, eventBus);
    footer.ensureDebugId("footer");
    footerActivityManager.setDisplay(footer);

    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    center.ensureDebugId("contentPanel");
    activityManager.setDisplay(center);

    logger.finest("init end");
  }

  @UiFactory
  ActivityPanel activityPanelProvider() {
    return activityPanelProvider.get();
  }

}
