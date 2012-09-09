package com.goodow.web.core.client;

import com.goodow.web.core.shared.WebPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.mvp.client.AnimatableDisplay;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationEndCallback;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public abstract class WebView<T extends Widget> extends Composite implements AcceptsOneWidget {

  protected T main;

  protected WebPlace place;

  private AnimatableDisplay display;

  private boolean currentIsFirst = false;

  private Animation animation = Animation.SLIDE;

  private IsWidget childWidget;

  public WebView() {
    main = createRoot();
    initWidget(main);
    // Execute after injection
    new Timer() {
      @Override
      public void run() {
        start();
      }
    }.schedule(1);
  }

  public WebPlace getPlace() {
    return place;
  }

  public boolean isScrollable() {
    return false;
  }

  public void refresh() {
  }

  public void setPlace(final WebPlace place) {
    this.place = place;
    refresh();
  }

  @Override
  public void setWidget(final IsWidget widget) {

    if (widget == childWidget) {
      return;
    }

    this.childWidget = widget;

    if (animation != null) {

      if (display == null) {
        display = GWT.create(AnimatableDisplay.class);
      }

      setChildWidget(display);

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
            // WebView view = (WebView) widget;
            // view.refresh();
          }
        }
      });
    } else {
      setChildWidget(widget);
    }
  }

  protected abstract T createRoot();

  /**
   * Override this method to accept child content view.
   */
  protected void setChildWidget(final IsWidget widget) {
    // do nothing by default;
  }

  protected abstract void start();

}
