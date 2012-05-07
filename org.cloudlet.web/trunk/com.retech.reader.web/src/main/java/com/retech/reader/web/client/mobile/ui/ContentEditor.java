package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceController;
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

import org.cloudlet.web.boot.shared.MapBinder;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ContentEditor extends WavePanel implements Activity {

  public static final String LAST_PAGE = "now_reader";
  private static final Logger logger = Logger.getLogger(ContentEditor.class.getName());

  private final HTML html;
  private FlowPanel flowPanel;
  private SectionBrowserView sectionView;
  private final PlaceController placeContorller;
  private final ReaderFactory f;
  private List<PageProxy> pages;
  private List<SectionProxy> sections;
  private PageProxy proxy;
  private int sectionIndex;
  private int pageIndex;
  private final Provider<BasePlace> place;
  private final LocalStorage storage;
  private final KeyUtil keyUtil;
  private final MapBinder<String, IsWidget> isWidgetMapBinder;
  private boolean scheduledTwo = false;
  private boolean scheduledOne;
  private int startX1;
  private int startX2;

  @Inject
  public ContentEditor(final PlaceController placeContorller, final ReaderFactory f,
      final Provider<BasePlace> place, final LocalStorage storage, final KeyUtil keyUtil,
      final MapBinder<String, IsWidget> isWidgetMapBinder) {
    this.placeContorller = placeContorller;
    this.f = f;
    this.place = place;
    this.storage = storage;
    this.keyUtil = keyUtil;
    flowPanel = new FlowPanel();
    this.isWidgetMapBinder = isWidgetMapBinder;
    html = new HTML();
    html.getElement().getStyle().setFontSize(15, Unit.PX);
    flowPanel.add(html);
    this.setWaveContent(flowPanel);

    this.addDomHandler(new TouchStartHandler() {

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        JsArray<Touch> touchesStart = event.getTouches();
        switch (touchesStart.length()) {
          case 1:
            scheduledOne = false;
            Touch touch = touchesStart.get(0);
            startX1 = touch.getPageX();
            break;
          case 2:
            scheduledTwo = false;
            Touch touch1 = touchesStart.get(0);
            Touch touch2 = touchesStart.get(1);
            startX1 = touch1.getPageX();
            startX2 = touch2.getPageX();
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
        if (scheduledOne || scheduledTwo) {
          return;
        }
        JsArray<Touch> touchesMove = event.getTouches();
        switch (touchesMove.length()) {
          case 1:
            scheduledOne = true;
            Touch touch = touchesMove.get(0);
            int nowX = touch.getPageX();
            int subtractX = nowX - startX1;
            goTo(subtractX > 0 ? 1 : -1);
            break;
          case 2:
            scheduledTwo = true;
            Touch touch1 = touchesMove.get(0);
            Touch touch2 = touchesMove.get(1);
            int nowX1 = touch1.getPageX();
            int nowX2 = touch2.getPageX();
            int subtractX1 = nowX1 - startX1;
            int subtractX2 = nowX2 - startX2;
            if (subtractX1 > 0 && subtractX2 > 0) {
              sectionView.getElement().getStyle().setLeft(0, Unit.PX);
              return;
            } else if (subtractX1 < 0 && subtractX2 < 0) {
              sectionView.getElement().getStyle().setLeft(-html.getOffsetWidth(), Unit.PX);
              return;
            }
            break;

          default:
            break;
        }
      }
    }, TouchMoveEvent.getType());

    this.addDomHandler(new TouchEndHandler() {

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        JsArray<Touch> touchesMove = event.getTouches();
        logger.info("onTouchEnd fingers:" + touchesMove);
      }
    }, TouchEndEvent.getType());

    // html.getElement().setDraggable("true");
    // html.addDomHandler(new DragStartHandler() {
    //
    // @Override
    // public void onDragStart(final DragStartEvent event) {
    // isStart = true;
    // startX1 = event.getNativeEvent().getClientX();
    // }
    // }, DragStartEvent.getType());
    //
    // html.addDomHandler(new DragOverHandler() {
    //
    // @Override
    // public void onDragOver(final DragOverEvent event) {
    // if (isStart) {
    // int nowX = event.getNativeEvent().getClientX();
    // int subtractX = nowX - startX1;
    // if (subtractX > 80) {
    // goTo(1);
    // isStart = false;
    // return;
    // } else if (subtractX < -80) {
    // goTo(-1);
    // isStart = false;
    // return;
    // }
    // }
    // }
    // }, DragOverEvent.getType());
    //
    // html.addDomHandler(new DragEndHandler() {
    //
    // @Override
    // public void onDragEnd(final DragEndEvent event) {
    // isStart = false;
    // }
    // }, DragEndEvent.getType());

    // html.addClickHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(final ClickEvent event) {
    // int offsetWidth = event.getRelativeElement().getOffsetWidth();
    // int x = event.getX();
    // goTo(x > offsetWidth / 2 ? 1 : -1);
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

    // final EntityProxyId<IssueProxy> issueId =
    // ((BasePlace) placeContorller.getWhere()).getParam(IssueProxy.class);

    // if (issueId != null) {
    // PageProxy pageProxy = storage.get(keyUtil.proxyKey(issueId, LAST_PAGE));
    //
    // if (pageProxy != null) {
    // pageStart(pageProxy);
    // Storage.getLocalStorageIfSupported().removeItem(keyUtil.proxyKey(issueId, LAST_PAGE));
    // // placeContorller.goTo(place.get().setPath(ContentEditor.class.getName()).setParameter(
    // // pageProxy.stableId()));
    // return;
    // }

    // new BaseReceiver<IssueProxy>() {
    //
    // @Override
    // public void onSuccessAndCached(final IssueProxy issueProxy) {
    //
    // new BaseReceiver<PageProxy>() {
    //
    // @Override
    // public void onSuccessAndCached(final PageProxy pageProxy) {
    // pageStart(pageProxy);
    // }
    //
    // @Override
    // public Request<PageProxy> provideRequest() {
    // return f.pageContext().findFirstPageByIssue(issueProxy).with(PageProxy.WITH);
    // }
    // }.setKeyForProxy(issueId, PageProxy.class.getName()).fire();
    // }
    //
    // @Override
    // public Request<IssueProxy> provideRequest() {
    // return f.find(issueId);
    // }
    // }.setKeyForProxy(issueId).fire();
    // return;
    // }

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
            sectionView.getElement().getStyle().setLeft(-Window.getClientWidth() - 50, Unit.PX);
            sectionView.addStyleName(ReaderResources.INSTANCE().style().contentSectionView());
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
    this.sectionView.removeStyleName(ReaderResources.INSTANCE().style().contentSectionView());
  }

  private void changePageProxy(final PageProxy pageProxy) {
    placeContorller.goTo(place.get().setPath(ContentEditor.class.getName()).setParameter(
        pageProxy.stableId()));
  }

  private void display(final PageProxy pageProxy) {
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        ContentEditor.this.getWaveTitle().setText(proxy.getTitle());
        html.setHTML(response.getDataString());
        if (pageProxy.getPageNum() != 1) {
          storage.put(keyUtil.proxyKey(proxy.getSection().getIssue().stableId(), LAST_PAGE), proxy);
        }
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getResource(proxy);
      }
    }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
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
    pageIndex = pages.indexOf(proxy);
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

  private void nextSectionIndex(final int sectionIndex, final boolean isNextSection) {
    if (sectionIndex > sections.size() - 1 && pageIndex + 2 > pages.size()) {
      this.sectionIndex = sections.size() - 1;
      logger.info("最后一页");
      return;
    } else if (sectionIndex < 0) {
      this.sectionIndex = 0;
      logger.info("第一页");
      return;
    }
    findPages(sections.get(sectionIndex), true, isNextSection);
  }

  private void pageStart(final PageProxy pageProxy) {
    final SectionProxy sectionProxy = pageProxy.getSection();
    IssueProxy issue = sectionProxy.getIssue();
    this.proxy = pageProxy;
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
}
