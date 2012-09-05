package com.goodow.web.core.shared;

import com.goodow.web.core.client.ScrollView;
import com.goodow.web.core.client.UIManager;
import com.goodow.web.core.client.WebView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.place.shared.Place;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationEndCallback;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WebPlace extends Place {

  private class ProtectedDisplay implements AcceptsOneWidget {

    public ProtectedDisplay() {
    }

    @Override
    public void setWidget(final IsWidget widget) {

      if (getStartedChild() != null && getStartedChild().getWidget() == widget) {
        return;
      }

      IsWidget target;
      Widget w = widget.asWidget();
      if (w instanceof ScrollPanel || w instanceof ScrollView) {
        w.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
            .fillPanelExpandChild());
        SimplePanel fillPanel = new SimplePanel();
        fillPanel.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
            .fillPanel());
        fillPanel.setWidget(w);
        target = fillPanel;
      } else {
        target = widget;
      }

      currentIsFirst = !currentIsFirst;
      if (currentIsFirst) {
        display.setFirstWidget(target);
      } else {
        display.setSecondWidget(target);
      }
      display.animate(animation, currentIsFirst, new AnimationEndCallback() {
        @Override
        public void onAnimationEnd() {
          if (widget instanceof WebView) {
            WebView view = (WebView) widget;
            view.refresh();
          }
        }
      });
    }
  }

  @Inject
  Provider<WebPlace> placeProvider;

  @Inject
  Provider<WebPlaceMapper> placeManager;

  private String path;

  private Animation animation;

  private String title;

  private WebPlace parent;

  private List<WebPlace> children = new ArrayList<WebPlace>();

  private WebPlace welcomePlace;

  private IsWidget widget;

  private ImageResource buttonImage;

  private String buttonText;

  private Provider<? extends IsWidget> widgetProvider;

  private AsyncProvider<? extends IsWidget> asyncWidgetProvider;

  private static final Logger logger = Logger.getLogger(WebPlace.class.getName());

  private AnimatableDisplay display;

  private ProtectedDisplay protectedDisplay;

  private boolean currentIsFirst = false;

  private WebPlace startedChild;

  private boolean feed;

  private WebContent content;

  private ObjectType objectType;

  private ViewType viewType;

  private Property property;

  @Inject
  private UIManager ui;

  public void addChild(final WebPlace place) {
    children.add(place);
    place.parent = this;
  }

  public WebPlace findChild(final String uri) {
    String[] pair = uri.split("\\?");
    String viewType = pair.length > 1 ? pair[1] : null;
    String[] segments = pair[0].split("/");
    WebPlace result = this;
    for (String path : segments) {
      if (path.length() == 0) {
        continue;
      }
      result = result.getChild(path);
    }

    if (result != null && viewType != null) {
      for (WebPlace child : result.getViewerPlaces()) {
        if (viewType.equals(child.getViewType().getName())) {
          result = child;
          break;
        }
      }
    }
    return result;
  }

  public Animation getAnimation() {
    return animation;
  }

  public ImageResource getButtonImage() {
    if (buttonImage == null) {
      buttonImage = MGWTStyle.getTheme().getMGWTClientBundle().tabBarFavoritesImage();
    }
    return buttonImage;
  }

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
    WebPlace result = null;
    Property property = getObjectType().getProperty(path);
    if (property != null) {
      result = placeProvider.get();
      result.setObjectType((ObjectType) property.getType());
    } else if (isFeed()) {
      result = placeProvider.get();
      result.setObjectType((ObjectType) getProperty().getType());
    }
    if (result != null) {
      result.setPath(path);
      addChild(result);
    }
    return result;
  }

  public List<WebPlace> getChildren() {
    return children;
  }

  public WebContent getContent() {
    return content;
  }

  public ObjectType getObjectType() {
    if (objectType == null && viewType != null) {
      objectType = getParent().getObjectType();
    }
    return objectType;
  }

  public WebPlace getParent() {
    return parent;
  }

  public String getPath() {
    return path;
  }

  public Property getProperty() {
    if (property == null && parent != null) {
      property = parent.getObjectType().getProperty(path);
    }
    return property;
  }

  public WebPlace getStartedChild() {
    return startedChild;
  }

  public String getTitle() {
    return title;
  }

  public String getUri() {
    return getUriBuilder().toString();
  }

  public StringBuilder getUriBuilder() {
    if (parent == null) {
      return new StringBuilder();
    } else {
      StringBuilder builder = parent.getUriBuilder();
      if (path != null) {
        builder.append("/").append(path);
      } else if (viewType != null) {
        builder.append("?view=").append(viewType.getName());
      }
      return builder;
    }
  }

  public List<WebPlace> getViewerPlaces() {
    List<WebPlace> result = new ArrayList<WebPlace>();
    for (ViewType viewType : getObjectType().getViewers()) {
      WebPlace place = placeProvider.get();
      place.setViewType(viewType);
      place.setTitle(viewType.getTitle());
      place.parent = this;
      result.add(place);
    }
    return result;

  }

  public ViewType getViewType() {
    return viewType;
  }

  public WebPlace getWelcomePlace() {
    return welcomePlace;
  }

  public IsWidget getWidget() {
    return widget;
  }

  public boolean isChildOf(final WebPlace place) {
    if (place == this) {
      return true;
    } else if (parent != null) {
      return parent.isChildOf(place);
    } else {
      return false;

    }
  }

  public boolean isFeed() {
    return parent != null && getProperty().isMany();
  }

  public void render(final AcceptsOneWidget panel) {
    render(panel, null);
  }

  public void setAnimation(final Animation animation) {
    this.animation = animation;
  }

  public void setButtonImage(final ImageResource buttonImage) {
    this.buttonImage = buttonImage;
  }

  public void setButtonText(final String buttonText) {
    this.buttonText = buttonText;
  }

  public void setContent(final WebContent content) {
    this.content = content;
  }

  public void setFeed(final boolean feed) {
    this.feed = feed;
  }

  public void setObjectType(final ObjectType objectType) {
    this.objectType = objectType;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public void setProperty(final Property property) {
    this.property = property;
  }

  public void setStartedChild(final WebPlace startedChild) {
    this.startedChild = startedChild;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public void setViewType(final ViewType viewType) {
    this.viewType = viewType;
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
    if (widget == null && widgetProvider == null && asyncWidgetProvider == null) {
      Object view = null;
      if (this.viewType != null) {
        view = getObjectType().getNativeViewer(this.viewType);
      } else if (isFeed()) {
        view = getObjectType().getNativeViewer(ContainerViewer.FEED);
      } else {
        view = getObjectType().getNativeViewer(ContainerViewer.ENTRY);
      }
      if (view != null) {
        if (view instanceof AsyncProvider) {
          asyncWidgetProvider = (AsyncProvider<IsWidget>) view;
        } else if (view instanceof Provider) {
          widgetProvider = (Provider<IsWidget>) view;
        } else if (view instanceof IsWidget) {
          widget = (IsWidget) view;
        }
      }
    }
    if (widget == null && widgetProvider == null && asyncWidgetProvider == null) {
      widget = new SimplePanel();
    }
    if (widget != null) {
      showWidget(panel, callback);
    } else if (widgetProvider != null) {
      widget = widgetProvider.get();
      showWidget(panel, callback);
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
          widget = result;
          showWidget(panel, callback);
        }
      });
    }
  };

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

  private void showWidget(final AcceptsOneWidget panel,
      final AsyncCallback<AcceptsOneWidget> callback) {
    panel.setWidget(widget);
    if (widget instanceof WebView) {
      WebView view = (WebView) widget;
      view.setPlace(this);
    }
    if (widget instanceof AcceptsOneWidget) {
      if (protectedDisplay == null) {
        display = GWT.create(AnimatableDisplay.class);
        protectedDisplay = new ProtectedDisplay();
        widget.asWidget().getElement().setId("widget-" + getUri());
        display.asWidget().getElement().setId("display-" + getUri());
      }
      AcceptsOneWidget container = (AcceptsOneWidget) widget;
      container.setWidget(display);
    }
    if (parent != null) {
      parent.setStartedChild(this);
    }
    if (callback != null && protectedDisplay != null) {
      callback.onSuccess(protectedDisplay);
    }
  }
}
