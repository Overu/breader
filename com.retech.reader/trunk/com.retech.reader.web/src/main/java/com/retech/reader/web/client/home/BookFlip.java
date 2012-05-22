package com.retech.reader.web.client.home;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;
import com.goodow.web.feature.client.FeatureDetection;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.client.mobile.ui.IssueEditor;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class BookFlip extends WavePanel implements Activity {

  interface Binder extends UiBinder<Widget, BookFlip> {
  }

  // interface OnStartDefaultDeviceReady {
  // void deviceReady();
  // }

  interface Style extends CssResource {
    String coverflow_1();

    String coverflow_2();

    String coverflow_3();

    String coverflow0();

    String coverflow1();

    String coverflow2();

    String coverflow3();
  }

  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<img src=\"{0}\" width='100%' ><div>{1}</div>")
    SafeHtml img(SafeUri image, String title);
  }

  private static final Logger logger = Logger.getLogger(BookFlip.class.getName());

  private static final int DIV_NUMS = 7;
  public static final int ISSUE_NUMS = 7;

  private static Binder binder = GWT.create(Binder.class);
  private static Template template = GWT.create(Template.class);

  private final LocalStorage storage;

  private Map<Integer, String> cssMap;
  private final PlaceController placeController;
  private final ReaderFactory f;

  @UiField
  HTMLPanel bookFlip;
  @UiField
  HTMLPanel container;

  @UiField
  Style coverflow;

  private int proxyCount;

  private List<IssueProxy> proxys;

  private final Provider<BasePlace> place;

  @Inject
  BookFlip(final ReaderFactory f, final Provider<BasePlace> place, final LocalStorage storage,
      final PlaceController placeController) {
    this.f = f;
    this.place = place;
    this.placeController = placeController;
    // StyleInjector.injectAtEnd(coverflow.getText());
    this.storage = storage;
    getWaveTitle().setText("最热门");

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("1.1 3D特效的初步实现"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("1.2 手势滚动（中）"));
    toDo.add(new Label("1.3 完善3D特效（中）"));
    add(toDo);

    setWaveContent(binder.createAndBindUi(this));
    cssMap = new HashMap<Integer, String>();
    cssMap.put(0, coverflow.coverflow0());
    cssMap.put(1, coverflow.coverflow1());
    cssMap.put(2, coverflow.coverflow2());
    cssMap.put(3, coverflow.coverflow3());
    cssMap.put(-1, coverflow.coverflow_1());
    cssMap.put(-2, coverflow.coverflow_2());
    cssMap.put(-3, coverflow.coverflow_3());
    // bookFlip.getElement().setDraggable(Element.DRAGGABLE_TRUE);

  }

  public native void addEventListener(Command command)/*-{
                                                      $wnd.document.addEventListener("deviceready", function(e) {
                                                      command.@com.google.gwt.user.client.Command::execute()();
                                                      }, false);
                                                      }-*/;

  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
    container.getElement().getStyle().setDisplay(Display.NONE);
  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    bookFlipHeight();
    container.getElement().getStyle().setDisplay(Display.BLOCK);
    container.getParent().getElement().getStyle().setBackgroundColor("black");
    logger.config("clientWidth:" + Window.getClientWidth() + ";clientHeight:"
        + Window.getClientHeight());
    if (FeatureDetection.mobileNative()) {
      Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

        @Override
        public boolean execute() {
          onStartDefault();
          return false;
        }
      }, 500);
      // addEventListener(new OnStartDefaultDeviceReady() {
      //
      // @Override
      // public void deviceReady() {
      // onStartDefault();
      // }
      // });
    } else {
      onStartDefault();
    }
    // bookFlip.setHeight(String.valueOf(Window.getClientHeight()) + "px");
  }

  protected IssueContext provideRequestContext() {
    return f.issue();
  }

  protected UiBinder<?, ?> provideUiBinder() {
    return binder;
  }

  private void bookFlipHeight() {
    int clientWidth = Window.getClientWidth();
    if (Window.getClientWidth() >= 1024) {
      container.setHeight(String.valueOf(clientWidth * 0.33 * 2) + "px");
    } else {
      container.setHeight(String.valueOf(clientWidth * 0.33 * 2) + "px");
    }
  }

  private int computeIdx(final long offset, final int length) {
    if (offset >= 0) {
      return (int) (offset % length);
    } else {
      return (int) Math.abs((offset + length) % length);
    }
  }

  private void displayImages(final long offset) {
    for (int i = 0; i < DIV_NUMS; i++) {
      final int proxyIdx = computeIdx(offset + i, proxyCount);
      displayImg(proxys.get(proxyIdx), i, false);
    }
  }

  private void displayImg(final IssueProxy proxy, final int divIdx, final boolean isFlip) {
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        SafeHtml image =
            template.img(UriUtils.fromTrustedString("data:" + response.getMimeType().getType()
                + ";base64," + response.getDataString()), proxy.getTitle());
        Element div = Element.as(bookFlip.getElement().getChild(divIdx * 2));
        div.setInnerSafeHtml(image);
        if (!isFlip) {
          div.setClassName(cssMap.get(divIdx - DIV_NUMS / 2));
        } else {
          div.setClassName(cssMap.get(DIV_NUMS / 2));
        }

        DOM.sinkEvents((com.google.gwt.user.client.Element) div, Event.ONCLICK);
        DOM.setEventListener((com.google.gwt.user.client.Element) div, new EventListener() {

          @Override
          public void onBrowserEvent(final Event event) {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
              placeController.goTo(place.get().setPath(IssueEditor.class.getName()).setParameter(
                  proxy.stableId()));
            }
          }
        });
        bookFlipHeight();
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getImage(proxy);
      }

    }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
  }

  private void move(final long offset, final boolean endAdd) {
    int newlyProxyIdx = computeIdx(offset + DIV_NUMS - 1, proxyCount);
    int divIdx = computeIdx(offset - 1, DIV_NUMS);
    for (int i = 0; i < DIV_NUMS; i++) {
      int cssIdx = computeIdx((DIV_NUMS - (offset % DIV_NUMS) + i), DIV_NUMS);
      Element.as(bookFlip.getElement().getChild(i * 2)).setClassName(
          cssMap.get(cssIdx - DIV_NUMS / 2));
    }
    displayImg(proxys.get(newlyProxyIdx), divIdx, true);
  }

  private void onStartDefault() {
    final IssueContext ctx = provideRequestContext();
    // if (proxys == null) {
    // proxys = storage.get(IssueProxy.ISSUES, IssueProxy.class);
    // }
    // new BaseReceiver<Long>() {
    //
    // @Override
    // public void onSuccessAndCached(final Long response) {
    // proxyCount = response.intValue();
    //
    // }
    //
    // @Override
    // public Request<Long> provideRequest() {
    // return ctx.count();
    // }
    //
    // };
    // }.setKeyForList(IssueProxy.COUNT_ISSUE).fire();

    new BaseReceiver<List<IssueProxy>>() {

      @Override
      public void onSuccessAndCached(final List<IssueProxy> response) {
        proxys = response;
        if (proxyCount == 0) {
          proxyCount = proxys.size();
        }
        displayImages(0);
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
          long i = 7001;

          @Override
          public boolean execute() {
            if (!isAttached()) {
              return false;
            }
            move(i++, true);
            return true;
          }
        }, 5000);

        bookFlip.addDomHandler(new DragStartHandler() {
          int i = 7000;

          @Override
          public void onDragStart(final DragStartEvent event) {
            if (event.getNativeEvent().getClientX() < (Window.getClientWidth() / 2)) {
              move(++i, true);
            } else {
              move(--i, true);
            }
            if (i > 20000 || i < 0) {
              i = 7001;
            }
          }
        }, DragStartEvent.getType());

        // bookFlip.addDomHandler(new DragOverHandler() {
        // boolean isChange = true;
        // int i = 7000;
        //
        // @Override
        // public void onDragOver(final DragOverEvent event) {
        // if (event.getNativeEvent().getClientX() % 20 == 0 && isChange) {
        // isChange = false;
        // Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
        //
        // @Override
        // public boolean execute() {
        // if (!isAttached()) {
        // return false;
        // }
        // move(i++, true);
        // isChange = true;
        // return !isChange;
        // }
        // }, 500);
        // bookFlip.addDomHandler(new DragLeaveHandler() {
        //
        // @Override
        // public void onDragLeave(final DragLeaveEvent event) {
        // isChange = true;
        // }
        // }, DragLeaveEvent.getType());
        // }
        // }
        // }, DragOverEvent.getType());

        // bookFlip.addDomHandler(new TouchEndHandler() {
        // int i = 7000;
        //
        // @Override
        // public void onTouchEnd(final TouchEndEvent event) {
        // if (event.getNativeEvent().getClientX() < (Window.getClientWidth() / 2)) {
        // move(++i, true);
        // } else {
        // move(--i, true);
        // }
        // if (i > 20000 || i < 0) {
        // i = 7001;
        // }
        // }
        // }, TouchEndEvent.getType());
      }

      @Override
      public Request<List<IssueProxy>> provideRequest() {
        return ctx.find(0, ISSUE_NUMS);
      }
      // }.setKeyForList(IssueProxy.ISSUES);
    }.setKeyForList(IssueProxy.ISSUES).fire();

  }
}
