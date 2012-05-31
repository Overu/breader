package com.retech.reader.web.client.mobile.ui;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.wave.client.shell.WaveShell;
import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarButtonView.State;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarClickButton;
import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBar;
import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.service.shared.KeyUtil;
import com.goodow.web.service.shared.LocalStorage;
import com.goodow.web.service.shared.rpc.BaseReceiver;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.client.style.ReaderResources;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ContentEditor extends WavePanel implements Activity {

  private static final Logger logger = Logger.getLogger(ContentEditor.class.getName());
  public static final String LAST_PAGE = "now_reader";
  private static final int TOPBAR_TOP = -40;

  public static native void addEventListener(Command command)/*-{
                                                              var resizeTimer = null;
                                                              $wnd.onresize = function(){  
                                                              if(resizeTimer) clearTimeout(resizeTimer);  
                                                              resizeTimer = setTimeout(function(e) {
                                                              command.@com.google.gwt.user.client.Command::execute()();
                                                              },300);  
                                                              }
                                                              }-*/;

  private final HTML html;
  private FlowPanel flowPanel;
  private SectionBrowserView sectionView;
  private final PlaceController placeContorller;
  private final ReaderFactory f;
  private List<PageProxy> pages;
  private List<SectionProxy> sections;
  private PageProxy pageProxy;
  private IssueProxy issueProxy;
  private int sectionIndex;
  private int pageIndex;
  private int contentWidth;
  private final Provider<BasePlace> place;
  private final LocalStorage storage;
  private final KeyUtil keyUtil;
  private final MapBinder<String, IsWidget> isWidgetMapBinder;
  private final WaveShell waveShell;
  private final WaveToolBar contentButtomBar;
  private int columnCount;
  private int columnIndex = 1;
  private int fingerCount = 0;
  private int startX1;
  private int startX2;
  private double changeScale;
  private double nowScale;
  private double fontSize = 1.5;
  private boolean resizeCmdScheduled = false;

  private final ScheduledCommand resizeCmd = new ScheduledCommand() {
    @Override
    public void execute() {
      resizeCmdScheduled = false;
      onDeviceorientation();
    }
  };

  private ToolBarClickButton collect;

  @Inject
  public ContentEditor(final PlaceController placeContorller, final ReaderFactory f,
      final Provider<BasePlace> place, final LocalStorage storage, final KeyUtil keyUtil,
      final MapBinder<String, IsWidget> isWidgetMapBinder, final WaveShell waveShell,
      final WaveToolBar contentButtomBar) {
    this.placeContorller = placeContorller;
    this.f = f;
    this.place = place;
    this.storage = storage;
    this.keyUtil = keyUtil;
    this.waveShell = waveShell;
    this.isWidgetMapBinder = isWidgetMapBinder;
    this.contentButtomBar = contentButtomBar;

    flowPanel = new FlowPanel();
    html = new HTML();

    final Style htmlStyle = html.getElement().getStyle();
    this.getElement().getStyle().setMarginTop(1, Unit.EM);
    htmlStyle.setFontSize(fontSize, Unit.EM);
    html.addStyleName(ReaderResources.INSTANCE().style().contentHtmlPanel());
    contentButtomBar.addStyleName(ReaderResources.INSTANCE().style().contentButtomBar());

    collect = contentButtomBar.addClickButton();
    collect.setText(IssueProxy.ISSUE_STATE_COLLECT);

    ToolBarClickButton contact = contentButtomBar.addClickButton();
    contact.setText("分享");

    ToolBarClickButton comment = contentButtomBar.addClickButton();
    comment.setText("评论");

    ToolBarClickButton summary = contentButtomBar.addClickButton();
    summary.setText("摘要");

    collect.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        List<IssueProxy> issueBook = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));
        if (issueBook == null) {
          issueBook = new ArrayList<IssueProxy>();
        }
        if (!issueBook.contains(issueProxy)) {
          issueBook.add(issueProxy);
          // logger.info(IssueProxy.MY_ISSUE_STATE_COLLECTED);
          collect.setText(IssueProxy.ISSUE_STATE_COLLECTED);
          collect.setState(State.DISABLED);
        }
        storage.put(keyUtil.listKey(IssueProxy.MY_ISSUES), issueBook);
      }
    });

    flowPanel.add(html);
    flowPanel.add(contentButtomBar);
    this.setWaveContent(flowPanel);
    // this.add(buttomBar);

    this.addDomHandler(new TouchStartHandler() {

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        JsArray<Touch> touchesStart = event.getTouches();
        switch (touchesStart.length()) {
          case 2:
            // logger.info("touches: 2 ");
            fingerCount = 2;
            Touch touch1 = touchesStart.get(0);
            Touch touch2 = touchesStart.get(1);
            startX1 = touch1.getPageX();
            startX2 = touch2.getPageX();
            break;
          case 1:
            // logger.info("touches: 1 ");
            fingerCount = 1;
            Touch touch = touchesStart.get(0);
            startX1 = touch.getPageX();
            columnCount = html.getElement().getScrollWidth() / contentWidth;
            // logger.info("columnCount:" + columnCount);
            break;

          default:
            break;
        }
      }
    }, TouchStartEvent.getType());

    this.addDomHandler(new TouchMoveHandler() {

      @Override
      public void onTouchMove(final TouchMoveEvent event) {
        event.preventDefault();
        JsArray<Touch> touchesMove = event.getTouches();
        switch (touchesMove.length()) {
          case 2:
            if (fingerCount == 0) {
              return;
            }
            // logger.info("touch 2 move");
            Touch touch1 = touchesMove.get(0);
            Touch touch2 = touchesMove.get(1);
            int nowX1 = touch1.getPageX();
            int nowX2 = touch2.getPageX();
            int subtractX1 = nowX1 - startX1;
            int subtractX2 = nowX2 - startX2;
            if ((subtractX1 > 0 && subtractX2 < 0) || (subtractX1 < 0 && subtractX2 > 0)) {
              nowScale = fontSize + (changeScale - 1.0);
              htmlStyle.setFontSize(nowScale, Unit.EM);
              // logger.info("scale:" + changeScale + ";fontSize:" + fontSize + (changeScale -
              // 1.0));
              return;
            }
            if (subtractX1 > 0 && subtractX2 > 0) {
              fingerCount = 0;
              sectionView.getElement().getStyle().setWidth(100, Unit.PCT);
              return;
            } else if (subtractX1 < 0 && subtractX2 < 0) {
              fingerCount = 0;
              sectionView.getElement().getStyle().setWidth(0, Unit.PX);
              return;
            }
            // else if (fingerCount == 2 && Math.abs(changeScale - 1.0) >= 0.15) {
            // }
            break;
          case 1:
            if (fingerCount == 0) {
              return;
            }
            fingerCount = 0;
            // logger.info("touch 1 move");
            Touch touch = touchesMove.get(0);
            int nowX = touch.getPageX();
            int subtractX = nowX - startX1;
            gotoNextPageAndScrollNext(subtractX);
            break;

          default:
            break;
        }
      }

    }, TouchMoveEvent.getType());

    this.addDomHandler(new TouchEndHandler() {

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        // JsArray<Touch> touchesMove = event.getTouches();
        // logger.info("onTouchEnd fingers:" + touchesMove.length());
        fontSize = nowScale;
        // scheduledOne = false;
        // scheduledTwo = false;
        fingerCount = 0;
        // scheduledGesture = false;
      }
    }, TouchEndEvent.getType());

    this.addDomHandler(new GestureChangeHandler() {

      @Override
      public void onGestureChange(final GestureChangeEvent event) {
        changeScale = event.getScale();
        // logger.info("scale:" + event.getScale());
      }
    }, GestureChangeEvent.getType());

    html.getElement().setDraggable("true");
    html.addDomHandler(new DragStartHandler() {

      @Override
      public void onDragStart(final DragStartEvent event) {
        fingerCount = 1;
        // scheduledOne = false;
        startX1 = event.getNativeEvent().getClientX();
        columnCount = html.getElement().getScrollWidth() / contentWidth;
      }
    }, DragStartEvent.getType());

    html.addDomHandler(new DragOverHandler() {

      @Override
      public void onDragOver(final DragOverEvent event) {
        if (fingerCount == 0) {
          return;
        }
        // scheduledOne = true;
        fingerCount = 0;
        int nowX = event.getNativeEvent().getClientX();
        int subtractX = nowX - startX1;
        gotoNextPageAndScrollNext(subtractX);
      }
    }, DragOverEvent.getType());

    html.addDomHandler(new DragEndHandler() {

      @Override
      public void onDragEnd(final DragEndEvent event) {
        // scheduledOne = false;
        fingerCount = 0;
      }
    }, DragEndEvent.getType());

    flowPanel.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        int offsetHeight = html.getOffsetHeight();
        int offsetWidth = html.getOffsetWidth();
        int clientX = event.getClientX();
        int clientY = event.getClientY();
        if (clientY >= offsetHeight * 0.25 && clientY <= offsetHeight * 0.75
            && clientX >= offsetWidth * 0.25 && clientX <= offsetWidth * 0.75) {
          Style waveShellStyle = waveShell.getTopBar().getElement().getStyle();
          Style contentButtomBarStyle = contentButtomBar.getElement().getStyle();
          if (waveShellStyle.getTop().equals(TOPBAR_TOP + Unit.PX.getType())) {
            waveShellStyle.clearTop();
            waveShellStyle.setOpacity(1);
            contentButtomBarStyle.setOpacity(1);
            contentButtomBarStyle.clearBottom();
          } else {
            waveShellStyle.setTop(TOPBAR_TOP, Unit.PX);
            waveShellStyle.clearOpacity();
            contentButtomBarStyle.setBottom(TOPBAR_TOP, Unit.PX);
            contentButtomBarStyle.clearOpacity();
          }
        }

      }
    }, ClickEvent.getType());

    // html.addClickHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(final ClickEvent event) {
    // int offsetWidth = event.getRelativeElement().getOffsetWidth();
    // int x = event.getX();
    // goTo(x > offsetWidth / 2 ? 1 : -1);
    // logger.info(html.getElement().getScrollWidth() + "");
    // logger.info((html.getElement().getScrollWidth() - contentWidth) / contentWidth + 1 + "");
    // }
    //
    // });
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

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    Window.addResizeHandler(new ResizeHandler() {

      @Override
      public void onResize(final ResizeEvent event) {
        scheduleResize();
      }
    });

    waveShell.getTopBar().getElement().getStyle().setTop(TOPBAR_TOP, Unit.PX);
    waveShell.getTopBar().addStyleName(ReaderResources.INSTANCE().style().contentTopBar());
    contentButtomBar.getElement().getStyle().setBottom(TOPBAR_TOP, Unit.PX);
    onDeviceorientation();

    List<IssueProxy> issueBook = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));
    if (issueBook == null) {
      issueBook = new ArrayList<IssueProxy>();
    }
    if (!issueBook.contains(issueProxy)) {
      collect.setState(State.ENABLED);
      collect.setText(IssueProxy.ISSUE_STATE_COLLECT);
    } else {
      collect.setState(State.DISABLED);
      collect.setText(IssueProxy.ISSUE_STATE_COLLECTED);
    }

    final EntityProxyId<PageProxy> pageId =
        ((BasePlace) placeContorller.getWhere()).getParam(PageProxy.class);

    new BaseReceiver<PageProxy>() {

      @Override
      public void onSuccessAndCached(final PageProxy pageProxy) {

        AsyncProvider<IsWidget> sectionBrowserView =
            isWidgetMapBinder.getAsyncProvider(SectionBrowserView.class.getName());
        sectionBrowserView.get(new AsyncCallback<IsWidget>() {

          @Override
          public void onFailure(final Throwable caught) {
            if (LogConfiguration.loggingIsEnabled()) {
              logger.log(Level.WARNING, "加载" + SectionBrowserView.class.getName()
                  + "失败. 请检查网络连接, 并刷新后重试", caught);
            }
          }

          @Override
          public void onSuccess(final IsWidget result) {
            sectionView = (SectionBrowserView) result.asWidget();
            sectionView.addStyleName(ReaderResources.INSTANCE().style().contentSectionView());
            sectionView.getElement().getStyle().setWidth(0, Unit.PX);
            sectionView.setIssueId(pageProxy.getSection().getIssue().stableId());
            sectionView.start(panel, eventBus);
            flowPanel.add(sectionView);
          }
        });

        pageStart(pageProxy);
      }

      @Override
      public Request<PageProxy> provideRequest() {
        return f.find(pageId).with(PageProxy.WITH);
      }
    }.setKeyForProxy(pageId).fire();
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    Style waveShellStyle = waveShell.getTopBar().getElement().getStyle();
    waveShellStyle.clearTop();
    waveShellStyle.clearOpacity();
    this.waveShell.getTopBar().removeStyleName(ReaderResources.INSTANCE().style().contentTopBar());
    this.sectionView.removeStyleName(ReaderResources.INSTANCE().style().contentSectionView());
    this.sectionView.getElement().getStyle().clearWidth();
    this.html.getElement().getStyle().clearLeft();
  }

  private void changePageProxy(final PageProxy pageProxy) {
    placeContorller.goTo(place.get().setPath(ContentEditor.class.getName()).setParameter(
        pageProxy.stableId()));
  }

  private void display(final PageProxy pageProxy) {
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        // ContentEditor.this.getWaveTitle().setText(proxy.getTitle());
        html.setHTML(response.getDataString());
        if (pageProxy.getPageNum() != 1) {
          storage.put(keyUtil.proxyAndKey(pageProxy.getSection().getIssue().stableId(), LAST_PAGE),
              pageProxy);
        }
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getResource(pageProxy);
      }
    }.setKeyForProxy(pageProxy.stableId(), ResourceProxy.class.getName()).fire();
  }

  private void findPages(final SectionProxy sectionProxy, final boolean isNextPage,
      final boolean isNextSection) {
    new BaseReceiver<List<PageProxy>>() {

      @Override
      public void onSuccessAndCached(final List<PageProxy> pages) {
        ContentEditor.this.pages = pages;
        if (isNextPage) {
          if (isNextSection) {
            changePageProxy(pages.get(0));
          } else {
            changePageProxy(pages.get(pages.size() - 1));
          }
        }
      }

      @Override
      public Request<List<PageProxy>> provideRequest() {
        return f.pageContext().findPagesBySection(sectionProxy).with(PageProxy.WITH);
      }
    }.setKeyForList(sectionProxy.stableId(), PageProxy.class.getName()).fire();
  }

  private void goTo(final int offset) {
    pageIndex = pages.indexOf(pageProxy);
    int index = pageIndex + offset;
    if (index < 0) {
      nextSectionIndex(--sectionIndex, false);
      return;
    } else if (index + 1 > pages.size()) {
      nextSectionIndex(++sectionIndex, true);
      return;
    }
    PageProxy pageProxy = pages.get(index);
    changePageProxy(pageProxy);
  }

  private void gotoNextPageAndScrollNext(final int subtractX) {
    if (subtractX > 0 && columnIndex <= 1) {
      goTo(-1);
      return;
    } else if (subtractX < 0 && columnIndex >= columnCount) {
      goTo(1);
      columnIndex = 1;
      return;
    }
    html.getElement().getStyle().setLeft(
        -100 * ((subtractX > 0 ? --columnIndex : ++columnIndex) - 1), Unit.PCT);
  }

  private void nextSectionIndex(final int sectionIndex, final boolean isNextSection) {
    if (sectionIndex > sections.size() - 1 && pageIndex + 2 > pages.size()) {
      this.sectionIndex = sections.size() - 1;
      // logger.info("最后一页");
      return;
    } else if (sectionIndex < 0) {
      this.sectionIndex = 0;
      // logger.info("第一页");
      return;
    }
    findPages(sections.get(sectionIndex), true, isNextSection);
  }

  private void onDeviceorientation() {
    // Window.alert("onDeviceorientation");
    int contentHeight = Window.getClientHeight() - 25;
    contentWidth = Window.getClientWidth() - 14;
    Style htmlStyle = html.getElement().getStyle();
    htmlStyle.setProperty("webkitColumnWidth", contentWidth + "px");
    htmlStyle.setHeight(contentHeight, Unit.PX);
    flowPanel.getElement().getParentElement().getStyle().setHeight(contentHeight, Unit.PX);
  }

  private void pageStart(final PageProxy pageProxy) {
    final SectionProxy sectionProxy = pageProxy.getSection();
    IssueProxy issue = sectionProxy.getIssue();
    this.pageProxy = pageProxy;
    this.issueProxy = issue;
    display(pageProxy);

    new BaseReceiver<List<SectionProxy>>() {

      @Override
      public void onSuccessAndCached(final List<SectionProxy> sections) {
        ContentEditor.this.sections = sections;
        sectionIndex = sectionProxy.getSequence() - 1;
      }

      @Override
      public Request<List<SectionProxy>> provideRequest() {
        return f.section().findSectionByPage(pageProxy).with(SectionProxy.WITH);
      }
    }.setKeyForList(issue.stableId(), SectionProxy.class.getName()).fire();

    findPages(sectionProxy, false, false);
  }

  /**
   * Schedule a resize handler. We schedule the event so the DOM has time to update the offset
   * sizes, and to avoid duplicate resize events from both a height and width resize.
   */
  private void scheduleResize() {
    if (isAttached() && !resizeCmdScheduled) {
      resizeCmdScheduled = true;
      Scheduler.get().scheduleDeferred(resizeCmd);
    }
  }
}
