package com.goodow.web.core.shared;

import com.goodow.web.core.client.MyView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.Animation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MyPlace extends Place {

  @Inject
  private PlaceController placeController;

  private String path;

  private Animation animation;

  private String title;

  private MyPlace parent;

  private Map<String, MyPlace> children;

  private boolean paramitized;

  public String parameter;

  private IsWidget widget;

  private Provider<? extends IsWidget> widgetProvider;

  private AsyncProvider<? extends IsWidget> asyncWidgetProvider;

  private static final Logger logger = Logger.getLogger(MyPlace.class.getName());

  public void addChild(final MyPlace place) {
    if (children == null) {
      children = new HashMap<String, MyPlace>();
    }
    children.put(place.getPath(), place);
    place.parent = this;
  }

  public void display(final AcceptsOneWidget panel) {
    if (widget != null) {
      panel.setWidget(widget);
    } else if (widgetProvider != null) {
      panel.setWidget(widgetProvider.get());
    } else if (asyncWidgetProvider != null) {
      asyncWidgetProvider.get(new AsyncCallback<IsWidget>() {
        @Override
        public void onFailure(final Throwable caught) {
          displayChild(panel);
        }

        @Override
        public void onSuccess(final IsWidget result) {
          panel.setWidget(result);
        }
      });
    } else {
      displayChild(panel);
    }
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }
    if (other instanceof MyPlace) {
      MyPlace that = (MyPlace) other;
      return this.path.equals(that.path);
    }
    return false;
  }

  public MyPlace findChild(final String uri) {
    String[] segments = uri.split("/");
    MyPlace result = this;
    for (String path : segments) {
      if (result.isParamitized()) {
        result.setParameter(path);
        continue;
      }
      result = result.getChild(path);
      if (result == null) {
        break;
      }
    }
    return result;
  }

  public Animation getAnimation() {
    return animation;
  }

  public MyPlace getChild(final String path) {
    if (children == null) {
      return null;
    }
    return children.get(path);
  }

  public String getParameter() {
    return parameter;
  }

  public MyPlace getParent() {
    return parent;
  }

  public String getPath() {
    return path;
  }

  public String getTitle() {
    return title;
  }

  public String getUri() {
    if (parent == null) {
      return "";
    } else {
      return parent.getUri() + "/" + path;
    }
  }

  public boolean isParamitized() {
    return paramitized;
  }

  public void setAnimation(final Animation animation) {
    this.animation = animation;
  }

  public void setParameter(final String parameter) {
    this.parameter = parameter;
  }

  public void setParamitized(final boolean paramitized) {
    this.paramitized = paramitized;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setWidget(final AsyncProvider<? extends IsWidget> asyncWidgetProvider) {
    this.asyncWidgetProvider = asyncWidgetProvider;
  }

  public void setWidget(final IsWidget widget) {
    this.widget = widget;
  }

  public void setWidget(final Provider<? extends IsWidget> provider) {
    this.widgetProvider = provider;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getTitle()).append(" (").append(getUri()).append(")");
    return builder.toString();
  }

  private void displayChild(final AcceptsOneWidget panel) {
    if (children != null && !children.isEmpty()) {
      final MyPlace firstChild = children.values().iterator().next();
      Label label = new Label("Loading " + firstChild.getUri());
      panel.setWidget(label);

      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

        @Override
        public void execute() {
          panel.setWidget(null);
          placeController.goTo(firstChild);
        }
      });

    } else {
      String msg = "No widget found for " + this;
      MyView view = new MyView();
      view.setTitle(msg);
      panel.setWidget(view);
    }
  }
}
