package com.goodow.web.view.wave.client.shell;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;

import java.util.logging.Logger;

@Singleton
public class WaveShell extends Composite {
  interface WaveShellUiBinder extends UiBinder<Widget, WaveShell> {
  }

  private final static Logger logger = Logger.getLogger(WaveShell.class.getName());

  private static WaveShellUiBinder uiBinder = GWT.create(WaveShellUiBinder.class);
  @UiField
  SimplePanel content;
  static {
    WaveBundle.INSTANCE.clean().ensureInjected();
  }

  @Inject
  WaveShell(@Default final SimpleActivityMapper centerActivityMapper, final EventBus eventBus) {
    logger.finest("init start");
    initWidget(uiBinder.createAndBindUi(this));
    Style style = getElement().getStyle();
    style.setOverflowY(Overflow.AUTO);
    style.setMarginTop(2, Unit.PX);
    style.setMarginLeft(7, Unit.PX);
    Element loading = Document.get().getElementById("loading");
    if (loading != null) {
      loading.removeFromParent();
    }
    RootPanel.get().add(this);

    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    activityManager.setDisplay(content);

    logger.finest("init end");
  }

}
