package org.cloudlet.web.ui.client.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import org.cloudlet.web.ui.client.style.WebAppResources;

import java.util.logging.Logger;

@Singleton
public class KeyboardShortcuts extends Composite {
  interface KeyboardShortcutsUiBinder extends UiBinder<Widget, KeyboardShortcuts> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final DecoratedPopupPanel popup;

  private static KeyboardShortcutsUiBinder uiBinder = GWT.create(KeyboardShortcutsUiBinder.class);
  @UiField
  Label close;

  public KeyboardShortcuts() {
    popup = new DecoratedPopupPanel(true);
    popup.setStyleName(WebAppResources.styles().keyboardShortcuts());
    initWidget(uiBinder.createAndBindUi(this));

    setSize("800px", "600px");
    popup.setWidget(this);
  }

  public void toggleVisible() {
    if (popup.isShowing()) {
      logger.info("隐藏键盘快捷键帮助(?)");
      popup.hide();
    } else {
      popup.center();
      logger.info("显示键盘快捷键帮助(?)");
      popup.show();
    }
  }

  @UiHandler("close")
  void onCloseClick(ClickEvent event) {
    popup.hide();
  }

}
