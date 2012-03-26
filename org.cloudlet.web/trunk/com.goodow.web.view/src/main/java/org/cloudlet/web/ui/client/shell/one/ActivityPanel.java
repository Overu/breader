package org.cloudlet.web.ui.client.shell.one;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActivityPanel extends SimplePanel {

  private String side;
  private final Provider<OnePageShell> shell;

  @Inject
  ActivityPanel(Provider<OnePageShell> shell) {
    this.shell = shell;
  }

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
    getElement().getStyle().setFloat(Style.Float.valueOf(side.toUpperCase()));
  }

  @Override
  public void setWidget(IsWidget w) {
    if (w == null) {
      setVisible(false);
    } else {
      if (!shell.get().isAttached()) {
        // RootLayoutPanel.get().clear();
        RootPanel.get().clear();
        RootPanel.get().add(shell.get());
      }
      setVisible(true);
    }
    super.setWidget(w);

    if (side != null && getParent() instanceof ComplexPanel) {
      ComplexPanel parent = (ComplexPanel) getParent();
      int index = parent.getWidgetIndex(this);

      if (parent.getWidgetCount() > index + 1) {
        Widget centerWidget = parent.getWidget(index + 1);

        int margin = w == null ? 10 : getOffsetWidth();

        if ("left".equals(side)) {
          centerWidget.getElement().getStyle().setMarginLeft(margin, Unit.PX);
        } else if ("right".equals(side)) {
          centerWidget.getElement().getStyle().setMarginRight(margin, Unit.PX);
        }
      }
    }
  }
}
