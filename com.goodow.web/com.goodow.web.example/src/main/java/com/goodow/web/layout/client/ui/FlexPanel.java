package com.goodow.web.layout.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.goodow.web.layout.shared.Direction;
import com.goodow.web.layout.shared.Layer;
import com.goodow.web.layout.shared.LayoutStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class FlexPanel extends ComplexPanel {

  interface ShellBinder extends UiBinder<Widget, FlexPanel> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static ShellBinder uiBinder = GWT.create(ShellBinder.class);

  private Layer layer;

  @UiField
  HTMLPanel titleBar;

  @UiField
  HTMLPanel toolBar;

  @UiField
  HTMLPanel container;

  private final Provider<FlexPanel> panelProvider;

  private final WidgetRegistry widgetRegistry;

  private boolean renderred = false;

  @Inject
  public FlexPanel(final Provider<FlexPanel> panelProvider, final WidgetRegistry widgetRegistry) {
    this.panelProvider = panelProvider;
    this.widgetRegistry = widgetRegistry;
    Widget widget = uiBinder.createAndBindUi(this);
    add(widget);
  }

  @Override
  public void add(final Widget w) {
    add(w, container.getElement());

    if (w instanceof FlexPanel) {
      Layer childLayer = ((FlexPanel) w).getLayer();
      childLayer.setParent(getLayer());
      List<Layer> children = getLayer().getChildren();
      if (children == null) {
        children = new ArrayList<Layer>();
        getLayer().setChildren(children);
      }
      if (!children.contains(childLayer)) {
        children.add(childLayer);
      }
    }

  }

  public Layer getLayer() {
    return layer;
  }

  public void setDirection(final Direction direction) {
    getLayer().setDirection(direction);
  }

  public void setLayer(final Layer layer) {
    this.layer = layer;
  }

  public void setStyle(final LayoutStyle style) {
    getLayer().setStyle(style);
  }

  @Override
  public void setTitle(final String title) {
    getLayer().setTitle(title);
  }

  public void setWidgetId(final String id) {
    getLayer().setWidgetId(id);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (renderred) {
      return;
    }
    renderred = true;

    String title = getLayer().getTitle();
    titleBar.add(new HTML(title == null ? "无标题" : title));
    // TODO uncomment below code
    Layer parent = getLayer().getParent();
    if (parent != null && parent.getDirection() == Direction.HORIZONTAL) {
      getElement().getStyle().setFloat(Style.Float.LEFT);
    } else {
      getElement().getStyle().setFloat(Style.Float.NONE);
    }
    if (!showLayers()) {
      showWidgets();
    }
  }

  @UiHandler("closeBtn")
  void handleCloseHide(final ClickEvent e) {
    setVisible(false);
  }

  private boolean showLayers() {
    List<Layer> children = getLayer().getChildren();
    if (children != null && !children.isEmpty()) {
      for (Layer layer : children) {
        FlexPanel w = null;// layer.getWidget();
        if (w == null) {
          w = panelProvider.get();
          w.setLayer(layer);
        }
        add(w);
      }
      return true;
    }
    return false;
  }

  private boolean showWidgets() {
    final String widgetId = getLayer().getWidgetId();
    if (widgetId == null) {
      return false;
    }
    IsWidget widget = widgetRegistry.get(widgetId);
    if (widget != null) {
      add(widget);
      return true;
    }

    AsyncProvider<IsWidget> provider = widgetRegistry.getAsyncProvider(widgetId);
    if (provider != null) {
      provider.get(new AsyncCallback<IsWidget>() {
        @Override
        public void onFailure(final Throwable caught) {
          // TODO Auto-generated method stub
          add(new Label("Failed to load " + widgetId));
        }

        @Override
        public void onSuccess(final IsWidget result) {
          add(result);
        }
      });
      return true;
    }
    return false;
  }
}
