package com.goodow.web.dev.client.ui;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;

import java.util.logging.Logger;

public class ShellUiDev extends SimplePanel {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  ShellUiDev(@Default final SimpleActivityMapper centerActivityMapper, final EventBus eventBus) {
    logger.finest("init start");
    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    this.ensureDebugId("root");
    getElement().getStyle().setOverflowY(Overflow.SCROLL);
    activityManager.setDisplay(this);
    logger.finest("init end");
  }

  @Override
  public void setWidget(final IsWidget w) {
    if (w != null) {
      ensureAttached();
    }
    super.setWidget(w);
  }

  private void ensureAttached() {
    if (this.isAttached()) {
      return;
    }
    Element loading = Document.get().getElementById("loading");
    loading.removeFromParent();
    RootPanel.get().clear();
    RootPanel.get().add(this);
  }
}
