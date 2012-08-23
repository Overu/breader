package com.goodow.web.reader.client.editgrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.collection.shared.LightArrayInt;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.CarouselCss;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.event.scroll.ScrollRefreshEvent;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EditGridCarousel extends Composite implements HasWidgets,
    HasSelectionHandlers<Integer> {

  public static interface CarouselImpl {

    void adjust(FlowPanel main, FlowPanel container);

  }

  public static class CarouselImplGecko implements CarouselImpl {

    @Override
    public void adjust(final FlowPanel main, final FlowPanel container) {
      int widgetCount = container.getWidgetCount();
      int offsetWidth = main.getOffsetWidth();

      container.setWidth(widgetCount * offsetWidth + "px");

      for (int i = 0; i < widgetCount; i++) {
        container.getWidget(i).setWidth(offsetWidth + "px");
      }

    }

  }

  public static class CarouselImplSafari implements CarouselImpl {

    @Override
    public void adjust(final FlowPanel main, final FlowPanel container) {
      int widgetCount = container.getWidgetCount();

      double sizeFactor = 100d / widgetCount;

      for (int i = 0; i < widgetCount; i++) {
        container.getWidget(i).setWidth(sizeFactor + "%");
      }

      container.setWidth((widgetCount * 100) + "%");

    }

  }

  interface Bundle extends ClientBundle {

    @Source("editgridcarouse.css")
    EditGridCarouseCss css();
  }

  interface EditGridCarouseCss extends CarouselCss {

  }

  private static class CarouselIndicator extends TouchWidget {
    private final CarouselCss css;

    public CarouselIndicator(final CarouselCss css) {
      this.css = css;
      setElement(DOM.createDiv());

      addStyleName(css.indicator());

    }

    public void setActive(final boolean active) {
      if (active) {
        addStyleName(css.indicatorActive());
      } else {
        removeStyleName(css.indicatorActive());
      }
    }

    public void setTitleText(final String text) {
      this.getElement().setInnerText(text);
    }
  }
  private static class CarouselIndicatorContainer extends Composite implements
      HasSelectionHandlers<Integer> {
    private FlowPanel main;
    private final CarouselCss css;
    private ArrayList<CarouselIndicator> indicators;
    private int selectedIndex;

    public CarouselIndicatorContainer(final CarouselCss css, final int numberOfPages) {
      if (numberOfPages < 0) {
        throw new IllegalArgumentException();
      }
      this.css = css;
      main = new FlowPanel();
      initWidget(main);

      main.addStyleName(this.css.indicatorContainer());

      indicators = new ArrayList<EditGridCarousel.CarouselIndicator>(numberOfPages);
      selectedIndex = 0;

      for (int i = 0; i < numberOfPages; i++) {
        final CarouselIndicator indicator = new CarouselIndicator(css);
        indicators.add(indicator);
        indicator.addDomHandler(new ClickHandler() {

          @Override
          public void onClick(final ClickEvent event) {
            SelectionEvent.fire(CarouselIndicatorContainer.this, indicators.indexOf(indicator));
          }
        }, ClickEvent.getType());
        main.add(indicator);
        if (i == 0) {
          indicator.setTitleText("选辑封面");
          continue;
        }
        if (i == numberOfPages - 1) {
          indicator.setTitleText("书签列表");
          continue;
        }
        indicator.setTitleText(String.valueOf(i));
      }

      setSelectedIndex(selectedIndex);
    }

    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
      return addHandler(handler, SelectionEvent.getType());
    }

    public void setSelectedIndex(final int index) {
      if (indicators.isEmpty()) {
        selectedIndex = -1;
        return;
      }
      if (selectedIndex != -1) {
        indicators.get(selectedIndex).setActive(false);
      }
      selectedIndex = index;
      if (selectedIndex != -1) {
        indicators.get(selectedIndex).setActive(true);

      }
    }
  }
  private static class WidgetHolder extends FlowPanel {

    public WidgetHolder(final CarouselCss css) {
      addStyleName(css.carouselHolder());
    }

    @Override
    public void add(final Widget w) {
      super.add(w);
      if (w instanceof ScrollPanel) {
        w.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss()
            .fillPanelExpandChild());
      }
    }

  }

  private static Bundle bundle = GWT.create(Bundle.class);

  private FlowPanel main;
  private final CarouselCss css;

  private ScrollPanel scrollPanel;

  private FlowPanel container;
  private CarouselIndicatorContainer carouselIndicatorContainer;

  private int currentPage;

  private Map<Widget, Widget> childToHolder;

  private com.google.web.bindery.event.shared.HandlerRegistration refreshHandler;

  private static final CarouselImpl IMPL = new CarouselImplSafari();

  private boolean isStackCover = false;
  private boolean isRefList = false;

  public EditGridCarousel() {
    this(bundle.css());
  }

  public EditGridCarousel(final CarouselCss css) {
    this.css = css;
    this.css.ensureInjected();

    childToHolder = new HashMap<Widget, Widget>();
    main = new FlowPanel();
    initWidget(main);

    main.addStyleName(css.carousel());

    scrollPanel = new ScrollPanel();
    scrollPanel.addStyleName(css.carouselScroller());

    main.add(scrollPanel);

    container = new FlowPanel();
    container.addStyleName(css.carouselContainer());

    scrollPanel.setWidget(container);

    scrollPanel.setSnap(true);
    scrollPanel.setMomentum(false);
    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);
    scrollPanel.setScrollingEnabledY(true);
    scrollPanel.setAutoHandleResize(false);

    currentPage = 0;

    scrollPanel.addScrollEndHandler(new ScrollEndEvent.Handler() {

      @Override
      public void onScrollEnd(final ScrollEndEvent event) {
        int page;

        page = scrollPanel.getCurrentPageX();

        carouselIndicatorContainer.setSelectedIndex(page);
        currentPage = page;
        SelectionEvent.fire(EditGridCarousel.this, currentPage);

      }
    });

    MGWT.addOrientationChangeHandler(new OrientationChangeHandler() {

      @Override
      public void onOrientationChanged(final OrientationChangeEvent event) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

          @Override
          public void execute() {
            refresh();

          }
        });

      }
    });

    addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(final SelectionEvent<Integer> event) {

        carouselIndicatorContainer.setSelectedIndex(currentPage);

      }
    });

    if (MGWT.getOsDetection().isDesktop()) {
      Window.addResizeHandler(new ResizeHandler() {

        @Override
        public void onResize(final ResizeEvent event) {
          Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
              refresh();

            }
          });

        }
      });
    }

  }

  @Override
  public void add(final Widget w) {
    container.insert(addBase(w), container.getWidgetCount() - 1);
    refresh();

  }

  public void addAndGoCurrentPage(final Widget w) {
    add(w);
    setSelectedPage(container.getWidgetCount() - 2);
  }

  @Override
  public HandlerRegistration addSelectionHandler(final SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  @Override
  public void clear() {
    container.clear();
    childToHolder.clear();

  }

  public Widget getCurrentWidget() {
    Widget current = container.getWidget(currentPage);
    Iterator<Entry<Widget, Widget>> iterator = childToHolder.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Widget, Widget> next = iterator.next();
      if (next.getValue() == current) {
        return next.getKey();
      }
    }
    return null;
  }

  @Override
  public Iterator<Widget> iterator() {
    Set<Widget> keySet = childToHolder.keySet();
    return keySet.iterator();
  }

  public void refresh() {

    IMPL.adjust(main, container);

    scrollPanel.setScrollingEnabledX(true);
    scrollPanel.setScrollingEnabledY(false);

    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);

    if (carouselIndicatorContainer != null) {
      carouselIndicatorContainer.removeFromParent();

    }

    int widgetCount = container.getWidgetCount();

    carouselIndicatorContainer = new CarouselIndicatorContainer(css, widgetCount);
    addCarouselIndicatorContainerSelectionHandler(carouselIndicatorContainer);

    main.add(carouselIndicatorContainer);

    if (currentPage >= widgetCount) {
      currentPage = widgetCount - 1;
    }

    carouselIndicatorContainer.setSelectedIndex(currentPage);

    scrollPanel.refresh();

    refreshHandler = scrollPanel.addScrollRefreshHandler(new ScrollRefreshEvent.Handler() {

      @Override
      public void onScrollRefresh(final ScrollRefreshEvent event) {
        refreshHandler.removeHandler();
        refreshHandler = null;

        scrollPanel.scrollToPage(currentPage, 0, 0);

      }
    });

  }

  @Override
  public boolean remove(final Widget w) {
    if (currentPage == 0 || currentPage == container.getWidgetCount() - 1) {
      return false;
    }
    Widget holder = childToHolder.remove(w);
    if (holder != null) {
      boolean remove = container.remove(holder);
      refresh();
      return remove;
    }
    return false;
  }

  public void setRefList(final Widget w) {
    if (isRefList) {
      return;
    }
    container.insert(addBase(w), container.getWidgetCount());
    isRefList = true;
  }

  public void setSelectedPage(final int index) {
    LightArrayInt pagesX = scrollPanel.getPagesX();
    if (index < 0 || index >= pagesX.length()) {
      throw new IllegalArgumentException("invalid value for index: " + index);
    }
    currentPage = index;
    scrollPanel.scrollToPage(index, 0, 300);
  }

  public void setStackCover(final Widget w) {
    if (isStackCover) {
      return;
    }
    container.insert(addBase(w), 0);
    isStackCover = true;
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    refresh();
  }

  private WidgetHolder addBase(final Widget w) {
    WidgetHolder widgetHolder = new WidgetHolder(css);
    widgetHolder.add(w);
    childToHolder.put(w, widgetHolder);
    return widgetHolder;
  }

  private void addCarouselIndicatorContainerSelectionHandler(
      final CarouselIndicatorContainer carouselIndicatorContainer) {
    carouselIndicatorContainer.addSelectionHandler(new SelectionHandler<Integer>() {

      @Override
      public void onSelection(final SelectionEvent<Integer> event) {
        Integer selectedItem = event.getSelectedItem();
        setSelectedPage(selectedItem);
      }
    });
  }

}
