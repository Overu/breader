package com.retech.reader.web.client.home;

import com.goodow.wave.client.wavepanel.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.Shared;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.client.mobile.ui.IssueNews;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class LibraryView extends WavePanel implements Activity {

  interface Binder extends UiBinder<Widget, LibraryView> {
  }
  interface Bundle extends ClientBundle {
    @Source("BookListPanel.portrait.css")
    Style portrait();
  }
  interface Resources extends ClientBundle {
    ImageResource addIssue();
  }

  @Shared
  interface Style extends CssResource {
    String root();
  }
  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<img src=\"{0}\"  width='100%' height='100%'>")
    SafeHtml img(SafeUri image);

    @SafeHtmlTemplates.Template("{0}")
    SafeHtml info(String text);
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Template template = GWT.create(Template.class);

  private ReaderFactory f;
  @UiField
  FlowPanel libPanel;

  private final Provider<BasePlace> places;
  private final LocalStorage storage;
  private final PlaceController placeController;
  private static final Resources res = GWT.create(Resources.class);
  private final KeyUtil keyUtil;

  private boolean isStart = true;

  @Inject
  LibraryView(final ReaderFactory f, final Provider<BasePlace> places, final LocalStorage storage,
      final PlaceController placeController, final KeyUtil keyUtil) {
    this.f = f;
    this.keyUtil = keyUtil;
    this.places = places;
    this.storage = storage;
    this.placeController = placeController;
    // getWaveTitle().setText(IssueProxy.MY_ISSUES_NAME);

    // FlowPanel toDo = new FlowPanel();
    // toDo.addStyleName(WavePanelResources.css().waveWarning());
    // toDo.add(new TEXT_HTML("<b>已完成：<b>"));
    // toDo.add(new Label("3.1 展示藏书列表"));
    // toDo.add(new Label("3.2 左右自动适应换行"));
    // toDo.add(new TEXT_HTML("<br>"));
    // toDo.add(new TEXT_HTML("<b>待实现：<b>"));
    // toDo.add(new Label("3.3 列表布局调整（易）"));
    // toDo.add(new Label("3.4 长按书本可以删除该书（易）"));
    // toDo.addStyleName(WavePanelResources.css().waveWarning());
    // add(toDo);

    setWaveContent(binder.createAndBindUi(this));

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

    List<IssueProxy> myIssues = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));

    libPanel.clear();
    if (myIssues != null) {
      displayIssue(myIssues, false);
    }
    HTMLPanel issuePanel = new HTMLPanel("");
    HTMLPanel imagePanel = new HTMLPanel("");
    Image add = new Image(res.addIssue());
    imagePanel.add(add);
    issuePanel.add(imagePanel);
    issuePanel.add(new Label("添加"));
    issuePanel.getElement().getStyle().setOpacity(0);
    // issuePanel.getElement().getStyle().setCursor(Cursor.POINTER);
    // issuePanel.addDomHandler(new ClickHandler() {
    //
    // @Override
    // public void onClick(final ClickEvent event) {
    // placeController.goTo(places.get().setPath(CategoryListEditor.class.getName()));
    // }
    // }, ClickEvent.getType());
    libPanel.add(issuePanel);
  }

  protected IssueContext provideRequestContext() {
    return f.issue();
  }

  private void displayIssue(final List<IssueProxy> proxys, final boolean isIssue) {
    for (final IssueProxy issue : proxys) {
      final HTMLPanel issuePanel = new HTMLPanel("");
      final HTMLPanel imagePanel = new HTMLPanel("");
      issuePanel.add(imagePanel);
      issuePanel.add(new Label(issue.getTitle()));

      final String libPanelStyleName = MyDownLoadPanel.getRes().mydownloadStyle().myDownLoadPanel();
      final Timer timer = new Timer() {

        @Override
        public void run() {
          isStart = false;
          if (libPanel.getStyleName().contains(libPanelStyleName)) {
            libPanel.removeStyleName(libPanelStyleName);
          } else {
            libPanel.addStyleName(libPanelStyleName);
          }
        }

      };

      issuePanel.addDomHandler(new TouchStartHandler() {

        @Override
        public void onTouchStart(final TouchStartEvent event) {
          timer.schedule(700);

        }
      }, TouchStartEvent.getType());

      issuePanel.addDomHandler(new TouchEndHandler() {

        @Override
        public void onTouchEnd(final TouchEndEvent event) {
          timer.cancel();
        }

      }, TouchEndEvent.getType());

      issuePanel.addDomHandler(new MouseDownHandler() {

        @Override
        public void onMouseDown(final MouseDownEvent event) {
          timer.schedule(700);
        }

      }, MouseDownEvent.getType());

      issuePanel.addDomHandler(new MouseUpHandler() {

        @Override
        public void onMouseUp(final MouseUpEvent event) {
          timer.cancel();
        }
      }, MouseUpEvent.getType());

      issuePanel.addDomHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
          if (libPanel.getStyleName().contains(libPanelStyleName)) {
            if (event.getX() < 12 && event.getY() < 12) {
              List<IssueProxy> issueBook = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));
              if (issueBook == null) {
                return;
              }
              if (issueBook.contains(issue)) {
                issueBook.remove(issue);
                libPanel.remove(issuePanel);
              }
              if (issueBook.size() == 0) {
                Storage.getLocalStorageIfSupported().removeItem(
                    keyUtil.listKey(IssueProxy.MY_ISSUES));
                return;
              }
              storage.put(keyUtil.listKey(IssueProxy.MY_ISSUES), issueBook);
            }
            return;
          }
          if (isStart) {
            EntityProxyId<IssueProxy> stableId = issue.stableId();
            placeController.goTo(places.get().setPath(IssueNews.class.getName()).setParameter(
                stableId));
          }
          isStart = true;
        }
      }, ClickEvent.getType());

      libPanel.add(issuePanel);

      new BaseReceiver<ResourceProxy>() {
        @Override
        public void onSuccessAndCached(final ResourceProxy response) {
          displayResource(response, imagePanel);
        }

        @Override
        public Request<ResourceProxy> provideRequest() {
          return f.resource().getImage(issue);
        }
      }.setKeyForProxy(issue.stableId(), ResourceProxy.class.getName()).fire();
    }
  }

  private void displayResource(final ResourceProxy resource, final HTMLPanel imagePanel) {
    SafeHtml safe =
        template.img(UriUtils.fromTrustedString("data:" + resource.getMimeType().getType()
            + ";base64," + resource.getDataString()));
    String asString = safe.asString();
    imagePanel.add(new HTML(asString));
  }
}
