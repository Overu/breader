package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;
import java.util.logging.Logger;

@Singleton
public class ContentEditor extends WavePanel implements Activity {

  private static final Logger logger = Logger.getLogger(ContentEditor.class.getName());
  private final ReaderFactory f;
  private List<PageProxy> contents;
  private List<SectionProxy> sections;
  private int sectionIndex;
  private final PlaceController placeController;
  private PageProxy proxy;
  private final HTML html;

  private int leftX = 0;
  private int rightX = 0;
  private boolean isStart = false;
  private HTMLPanel sectionPanel;
  private int leftIndex = -1;
  JsArray<Touch> touches = null;
  private boolean scheduled;

  @Inject
  ContentEditor(final ReaderFactory f, final PlaceController placeController) {
    html = new HTML();
    this.f = f;
    this.placeController = placeController;
    sectionPanel = new HTMLPanel("sadfsadfsadfsadfsadfsadfsdafsadfsadf");
    final Style style = sectionPanel.getElement().getStyle();
    sectionPanel.setHeight("100%");
    style.setPosition(Position.ABSOLUTE);
    style.setBackgroundColor("white");
    style.setTop(0, Unit.PX);

    this.addDomHandler(new TouchStartHandler() {

      @Override
      public void onTouchStart(final TouchStartEvent event) {
        JsArray<Touch> toucheStart = event.getTouches();
        if (toucheStart.length() >= 2) {
          logger.info("touch start:" + toucheStart.length());
          isStart = true;
          Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

            @Override
            public boolean execute() {
              if (touches != null && isStart && !scheduled) {
                scheduled = true;
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                  @Override
                  public void execute() {
                    scheduled = false;
                    printLog(touches);
                  }
                });
              }
              if (!isStart) {
                logger.info("Scheduler end:" + isStart);
              }
              return isStart;
            }
          }, 15);
        }
      }
    }, TouchStartEvent.getType());

    this.addDomHandler(new TouchMoveHandler() {

      @Override
      public void onTouchMove(final TouchMoveEvent event) {
        touches = event.getTouches();
      }
    }, TouchMoveEvent.getType());

    this.addDomHandler(new TouchEndHandler() {

      @Override
      public void onTouchEnd(final TouchEndEvent event) {
        touches = null;
        logger.info("strat end:");
        isStart = false;
      }
    }, TouchEndEvent.getType());

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("10.1 单击左右翻页"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("10.2 界面排版效果（中）"));
    toDo.add(new Label("10.3 快速定位某一页（中）"));
    toDo.add(new Label("10.4 书签（难）"));
    toDo.add(new Label("10.5 书本翻页的动画效果（难）"));
    toDo.add(new Label("10.6 单指手势翻页（中）"));
    toDo.add(new Label("10.7 返回目录（易）"));
    toDo.add(new Label("10.8 横屏时分两栏显示（难）"));
    add(toDo);

    this.setWaveContent(html);
    this.getWidget(1).getElement().appendChild(sectionPanel.getElement());

    html.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        int offsetWidth = event.getRelativeElement().getOffsetWidth();
        int x = event.getX();
        goTo(x > offsetWidth / 2 ? 1 : -1);
      }
    });
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
    leftIndex = -sectionPanel.getOffsetWidth();
    sectionPanel.getElement().getStyle().setLeft(leftIndex, Unit.PX);
    html.setHTML("");
    final EntityProxyId<IssueProxy> issueEntityId =
        ((BasePlace) placeController.getWhere()).getParam(IssueProxy.class);

    if (issueEntityId != null) {
      findPageProxyByIssueProxy(issueEntityId);
      return;
    }

    final EntityProxyId<PageProxy> pageEntityId =
        ((BasePlace) placeController.getWhere()).getParam(PageProxy.class);

    findPageByPageProxy(pageEntityId);
  }

  private void display(final PageProxy proxy) {
    this.proxy = proxy;
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        ContentEditor.this.getWaveTitle().setText(proxy.getTitle());
        html.setHTML(response.getDataString());
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getResource(proxy);
      }
    }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
  }

  private void findContentBySectionProxy(final SectionProxy sectionProxy, final boolean isStart,
      final boolean isNextPage) {

    new BaseReceiver<List<PageProxy>>() {

      @Override
      public void onSuccessAndCached(final List<PageProxy> pageList) {
        sectionIndex = sectionProxy.getSequence() - 1;
        contents = pageList;
        if (!isStart) {
          if (isNextPage) {
            display(contents.get(0));
          } else {
            display(contents.get(contents.size() - 1));
          }
        }
      }

      @Override
      public Request<List<PageProxy>> provideRequest() {
        return f.pageContext().findPagesBySection(sectionProxy);
      }
    }.setKeyForList(sectionProxy.stableId(), PageProxy.class.getName()).fire();
  }

  private void findPageByPageProxy(final EntityProxyId<PageProxy> pageEntityId) {

    new BaseReceiver<PageProxy>() {

      @Override
      public void onSuccessAndCached(final PageProxy pageProxy) {
        display(pageProxy);

        // findSectionByPage(pageProxy, true);

        EntityProxyId<IssueProxy> issueId = pageProxy.getSection().getIssue().stableId();
        new BaseReceiver<List<SectionProxy>>() {

          @Override
          public void onSuccessAndCached(final List<SectionProxy> sectionProxyList) {
            sections = sectionProxyList;
            findContentBySectionProxy(pageProxy.getSection(), true, false);
          }

          @Override
          public Request<List<SectionProxy>> provideRequest() {
            return f.section().findSectionByPage(pageProxy);
          }
        }.setKeyForList(issueId, SectionProxy.class.getName()).fire();
      }

      @Override
      public Request<PageProxy> provideRequest() {
        return f.find(pageEntityId).with(PageProxy.WITH);
      }
    }.setKeyForProxy(pageEntityId).fire();
  }

  private void findPageProxyByIssueProxy(final EntityProxyId<IssueProxy> issueEntityId) {
    new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy issueProxy) {
        // History.addValueChangeHandler(new ValueChangeHandler<String>() {
        // boolean start = true;
        //
        // @Override
        // public void onValueChange(final ValueChangeEvent<String> event) {
        // if (start) {
        // start = false;
        // placeController.goTo(provider.get().setPath(IssueEditor.class.getName())
        // .setParameter(issueProxy.stableId()));
        // return;
        // }
        // }
        // });

        new BaseReceiver<PageProxy>() {

          @Override
          public void onSuccessAndCached(final PageProxy pageProxy) {
            findPageByPageProxy(pageProxy.stableId());
          }

          @Override
          public Request<PageProxy> provideRequest() {
            return f.pageContext().findFirstPageByIssue(issueProxy).with(PageProxy.WITH);
          }
        }.setKeyForProxy(issueProxy.stableId(), PageProxy.class.getName()).fire();
      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueEntityId);
      }
    }.setKeyForProxy(issueEntityId).fire();
  }

  private void goTo(final int offset) {
    int current = contents.indexOf(proxy);
    // PageProxy pageProxy = contents.get(offset + current);
    if ((current + offset + 1) > contents.size()) {
      nextPage(++sectionIndex, true);
      return;
    } else if ((current + offset - 1) < 0) {
      nextPage(--sectionIndex, false);
      return;
    }
    display(contents.get(current + offset));
  }

  private void nextPage(final int indexSection, final boolean isNextPage) {
    if (indexSection > sections.size() - 1 && (contents.indexOf(proxy) + 2) > contents.size()) {
      // findContentBySectionProxy(sections.get(0), false);
      sectionIndex = sections.size() - 1;
      logger.info("最后一页");
      return;
    } else if (indexSection < 0) {
      // findContentBySectionProxy(sections.get(sections.size() - 1), false);
      sectionIndex = 0;
      logger.info("第一页");
      return;
    }
    findContentBySectionProxy(sections.get(indexSection), false, isNextPage);
  }

  private void printLog(final JsArray<Touch> touches) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < touches.length(); i++) {
      Touch touch = touches.get(i);
      sb.append("{touch-id:" + touch.getIdentifier());
      sb.append(";touch-pageX:" + touch.getPageX());
      sb.append(";touch-pageY:" + touch.getPageY());
      sb.append(";touch-clientX:" + touch.getClientX());
      sb.append(";touch-clientY:" + touch.getClientY());
      sb.append(";touch-screenX:" + touch.getScreenX());
      sb.append(";touch-screenY:" + touch.getScreenY());
      sb.append("}");
    }
    logger.info(sb.toString());
  }
}
