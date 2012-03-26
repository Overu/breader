package org.cloudlet.web.dev.client.ui;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.cloudlet.web.mvp.shared.Default;
import org.cloudlet.web.mvp.shared.SimpleActivityMapper;

import java.util.logging.Logger;

public class ShellUiDev extends SimpleLayoutPanel {
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  ShellUiDev(@Default final SimpleActivityMapper centerActivityMapper, final EventBus eventBus) {
    logger.finest("init start");
    ActivityManager activityManager = new ActivityManager(centerActivityMapper, eventBus);
    this.ensureDebugId("root");
    getElement().getStyle().setMarginLeft(7, Unit.PX);
    getElement().getStyle().setMarginTop(7, Unit.PX);
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
    RootLayoutPanel.get().clear();
    RootLayoutPanel.get().add(this);
  }
}
