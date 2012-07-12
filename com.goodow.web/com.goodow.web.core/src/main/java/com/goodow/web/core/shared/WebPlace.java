package com.goodow.web.core.shared;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.ui.client.MGWTStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WebPlace extends Place {

  @Inject
  private PlaceController placeController;

  private String path;

  private Animation animation;

  private String title;

  private WebPlace parent;

  private List<WebPlace> children;

  private boolean paramitized;

  private WebPlace welcomePlace;

  public String parameter;

  private IsWidget widget;

  private ImageResource buttonImage;

  private String buttonText;

  private Provider<? extends IsWidget> widgetProvider;

  private AsyncProvider<? extends IsWidget> asyncWidgetProvider;

  private static final Logger logger = Logger.getLogger(WebPlace.class.getName());

  public void addChild(final WebPlace place) {
    if (children == null) {
      children = new ArrayList<WebPlace>();
    }
    children.add(place);
    place.parent = this;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }
    if (other instanceof WebPlace) {
      WebPlace that = (WebPlace) other;
      return this.path.equals(that.path);
    }
    return false;
  }

  public WebPlace findChild(final String uri) {
    String[] segments = uri.split("/");
    WebPlace result = this;
    for (String path : segments) {
      if (path.length() == 0) {
        continue;
      }
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

  /**
   * @return the buttonImage
   */
  public ImageResource getButtonImage() {
    if (buttonImage == null) {
      buttonImage = MGWTStyle.getTheme().getMGWTClientBundle().tabBarFavoritesImage();
    }
    return buttonImage;
  }

  /**
   * @return the buttonText
   */
  public String getButtonText() {
    return buttonText;
  }

  public WebPlace getChild(final int index) {
    return children.get(index);
  }

  public WebPlace getChild(final String path) {
    for (WebPlace p : children) {
      if (path.equals(p.getPath())) {
        return p;
      }
    }
    return null;
  }

  public List<WebPlace> getChildren() {
    return children;
  }

  public String getParameter() {
    return parameter;
  }

  public WebPlace getParent() {
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

  public WebPlace getWelcomePlace() {
    return welcomePlace;
  }

  public boolean isParamitized() {
    return paramitized;
  }

  public void render(final AcceptsOneWidget panel) {
    if (welcomePlace != null) {
      gotoPlace(welcomePlace, panel);
    } else {
      render(panel, null);
    }
  }

  public void setAnimation(final Animation animation) {
    this.animation = animation;
  }

  /**
   * @param buttonImage the buttonImage to set
   */
  public void setButtonImage(final ImageResource buttonImage) {
    this.buttonImage = buttonImage;
  }

  /**
   * @param buttonText the buttonText to set
   */
  public void setButtonText(final String buttonText) {
    this.buttonText = buttonText;
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

  public void setWelcomePlace(final WebPlace place) {
    this.welcomePlace = place;
    addChild(place);
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getTitle()).append(" (").append(getUri()).append(")");
    return builder.toString();
  }

  private void append(final AcceptsOneWidget panel, final AsyncCallback<AcceptsOneWidget> callback) {
    if (widget != null) {
      panel.setWidget(widget);
      if (callback != null) {
        callback.onSuccess((AcceptsOneWidget) widget);
      }
    } else if (widgetProvider != null) {
      IsWidget w = widgetProvider.get();
      panel.setWidget(w);
      if (callback != null) {
        callback.onSuccess((AcceptsOneWidget) w);
      }
    } else if (asyncWidgetProvider != null) {
      asyncWidgetProvider.get(new AsyncCallback<IsWidget>() {
        @Override
        public void onFailure(final Throwable caught) {
          String msg =
              "Network connection cloud be lost. Failed to load widget for " + this
                  + " asynchronously.\r\n" + caught.getMessage();
          logger.severe(msg);
          showError(panel, msg);
          if (callback != null) {
            callback.onFailure(caught);
          }
        }

        @Override
        public void onSuccess(final IsWidget result) {
          panel.setWidget(result);
          if (callback != null) {
            callback.onSuccess((AcceptsOneWidget) result);
          }
        }
      });
    } else if (placeController.getWhere() != this) {
      if (callback != null) {
        callback.onSuccess(panel);
      }
    } else if (!children.isEmpty()) {
      gotoPlace(getChild(0), panel);
    } else {
      showError(panel, "No widget is bound to " + this);
    }
  }

  private void gotoPlace(final WebPlace place, final AcceptsOneWidget panel) {
    final WebPlace firstChild = children.get(0);
    showInfo(panel, "Loading " + firstChild.getUri());
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        placeController.goTo(place);
      }
    });
  }

  private void render(final AcceptsOneWidget panel, final AsyncCallback<AcceptsOneWidget> callback) {
    if (parent != null) {
      parent.render(panel, new AsyncCallback<AcceptsOneWidget>() {
        @Override
        public void onFailure(final Throwable caught) {
          logger.info("Failured to load parent widget. Show " + this + " directly.");
          append(panel, callback);
        }

        @Override
        public void onSuccess(final AcceptsOneWidget result) {
          append(result, callback);
        }
      });
    } else {
      append(panel, callback);
    }
  }

  private void showError(final AcceptsOneWidget panel, final String message) {
    Label label = new Label(message);
    panel.setWidget(label);
  }

  private void showInfo(final AcceptsOneWidget panel, final String message) {
    Label label = new Label(message);
    panel.setWidget(label);
  }

}
