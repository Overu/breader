package com.retech.reader.web.client.home;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

import com.retech.reader.web.client.mobile.ui.CategoryListEditor;
import com.retech.reader.web.client.mobile.ui.IssueNews;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
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

  @Inject
  LibraryView(final ReaderFactory f, final Provider<BasePlace> places, final LocalStorage storage,
      final PlaceController placeController) {
    this.f = f;
    this.places = places;
    this.storage = storage;
    this.placeController = placeController;
    getWaveTitle().setText("我的藏书");

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("3.1 展示藏书列表"));
    toDo.add(new Label("3.2 左右自动适应换行"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("3.3 列表布局调整（易）"));
    toDo.add(new Label("3.4 长按书本可以删除该书（易）"));
    add(toDo);
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    add(toDo);

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

    List<IssueProxy> myIssues = storage.get(IssueProxy.MY_ISSUES, IssueProxy.class);

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
    libPanel.getElement().getStyle().setCursor(Cursor.POINTER);
    issuePanel.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath(CategoryListEditor.class.getName()));
      }
    }, ClickEvent.getType());
    libPanel.add(issuePanel);
  }

  protected IssueContext provideRequestContext() {
    return f.issue();
  }

  private void displayIssue(final List<IssueProxy> proxys, final boolean isIssue) {
    for (final IssueProxy issue : proxys) {
      HTMLPanel issuePanel = new HTMLPanel("");
      final HTMLPanel imagePanel = new HTMLPanel("");
      issuePanel.add(imagePanel);
      issuePanel.add(new Label(issue.getTitle()));
      issuePanel.addDomHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent event) {
          EntityProxyId<IssueProxy> stableId = issue.stableId();
          placeController.goTo(places.get().setPath(IssueNews.class.getName()).setParameter(
              stableId));
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
