package com.goodow.web.layout.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.goodow.web.layout.shared.Page;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class Presenter extends Place implements Activity {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private String path;

  private Presenter parent;

  private Map<String, Presenter> children;

  private final Provider<Presenter> provider;

  private Page page;

  private List<PageAware> list;

  private final WidgetRegistry registry;

  @Inject
  public Presenter(final Provider<Presenter> provider, final WidgetRegistry widgetRegistry) {
    this.provider = provider;
    this.registry = widgetRegistry;
  }

  public void addListener(final PageAware a) {
    list.add(a);
  }

  public Presenter getChild(final String path) {
    Presenter child = getChildren().get(path);
    if (child == null) {
      child = provider.get();
      child.setPath(path);
      child.setParent(this);
    }
    return child;
  }

  public Map<String, Presenter> getChildren() {
    if (children == null) {
      children = new HashMap<String, Presenter>();
    }
    return children;
  }

  public Page getPage() {
    return page;
  }

  public Presenter getParent() {
    return parent;
  }

  public String getPath() {
    return path;
  }

  public String getUri() {
    if (parent == null) {
      return "";
    } else {
      return parent.getUri() + "/" + path;
    }
  }

  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
  }

  public void setParent(final Presenter parent) {
    this.parent = parent;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    registry.showWidget(panel, "widgetId");
  }

  private void fire() {
    for (PageAware l : list) {
      l.onLoad(this);
    }
  }

}
