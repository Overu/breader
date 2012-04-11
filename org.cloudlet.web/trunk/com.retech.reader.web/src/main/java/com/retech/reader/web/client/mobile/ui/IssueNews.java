package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.toolbar.ToolBarClickButton;
import com.goodow.web.view.wave.client.toolbar.WaveToolBar;
import com.goodow.web.view.wave.client.toolbar.ToolBarClickButton.State;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class IssueNews extends WavePanel implements Activity {
  interface Binder extends UiBinder<Widget, IssueNews> {

  }
  // interface Driver extends RequestFactoryEditorDriver<IssueProxy, IssueNews> {
  //
  // }
  interface Resources extends ClientBundle {
    ImageResource issueAdd();

    ImageResource issueDownload();

    ImageResource issueRead();

    ImageResource issueSection();
  }

  @UiField
  Image image;
  @UiField
  DivElement detail;
  @UiField
  DivElement datetime;
  @UiField
  DivElement viewcount;
  @UiField
  DivElement category;
  @UiField
  WaveToolBar waveToolbar;

  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static final Logger logger = Logger.getLogger(IssueNews.class.getName());
  // private static Driver driver = GWT.create(Driver.class);
  private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
  private ReaderFactory f;
  private EntityProxyId<IssueProxy> issueId;
  private ToolBarClickButton addButton;
  private IssueProxy proxy;
  private final PlaceController placeController;
  private final LocalStorage storage;

  @Inject
  public IssueNews(final ReaderFactory f, final Provider<BasePlace> places,
      final PlaceController placeController, final LocalStorage storage) {
    this.f = f;
    this.placeController = placeController;
    this.storage = storage;

    this.setWaveContent(binder.createAndBindUi(this));

    final ToolBarClickButton readButton = waveToolbar.addClickButton();
    readButton.setText("在线阅读");
    readButton.setVisualElement(createIcon(res.issueRead()));

    final ToolBarClickButton sectionButton = waveToolbar.addClickButton();
    sectionButton.setText("目录");
    sectionButton.setVisualElement(createIcon(res.issueSection()));

    addButton = waveToolbar.addClickButton();
    addButton.setText("收藏");
    addButton.setVisualElement(createIcon(res.issueAdd()));

    final ToolBarClickButton downloadButton = waveToolbar.addClickButton();
    downloadButton.setText("下载");
    downloadButton.setVisualElement(createIcon(res.issueDownload()));
    readButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath(ContentEditor.class.getName()).setParameter(
            issueId));
      }
    });
    sectionButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        placeController.goTo(places.get().setPath(SectionBrowserView.class.getName()).setParameter(
            issueId));
      }
    });
    addButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        List<IssueProxy> issueBook = storage.get(IssueProxy.MY_ISSUES, IssueProxy.class);
        if (issueBook == null) {
          issueBook = new ArrayList<IssueProxy>();
        }
        if (!issueBook.contains(proxy)) {
          issueBook.add(proxy);
          logger.info("已收藏");
          addButton.setState(State.DISABLED);
        }
        storage.put(IssueProxy.MY_ISSUES, issueBook);
      }
    });
    downloadButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        logger.info("开发中");
      }
    });
  }

  /**
   * Creates an icon Element.
   */
  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
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

    // toolbar.add(waveToolbar);

    BasePlace place = (BasePlace) placeController.getWhere();
    issueId = place.getParam(IssueProxy.class);

    /**
     * 获取书籍详情
     */
    new BaseReceiver<IssueProxy>() {
      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {
        IssueNews.this.proxy = proxy;
        datetime.setInnerHTML(dateFormat.format(proxy.getCreateTime()));
        viewcount.setInnerHTML(String.valueOf(proxy.getViewCount()));
        getWaveTitle().setText(proxy.getTitle());
        detail.setInnerHTML(proxy.getDetail());
        List<IssueProxy> issueBook = storage.get(IssueProxy.MY_ISSUES, IssueProxy.class);
        if (issueBook == null) {
          issueBook = new ArrayList<IssueProxy>();
        }
        if (!issueBook.contains(proxy)) {
          addButton.setState(State.ENABLED);
        } else {
          addButton.setState(State.DISABLED);
        }

        new BaseReceiver<ResourceProxy>() {
          @Override
          public void onSuccessAndCached(final ResourceProxy response) {
            image.setUrl(UriUtils.fromTrustedString("data:" + response.getMimeType().getType()
                + ";base64," + response.getDataString()));
          }

          @Override
          public Request<ResourceProxy> provideRequest() {
            return f.resource().getImage(proxy);
          }
        }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
        /**
         * 分类
         */
        new BaseReceiver<CategoryProxy>() {
          @Override
          public void onSuccessAndCached(final CategoryProxy response) {
            category.setInnerHTML(response.getTitle());
          }

          @Override
          public Request<CategoryProxy> provideRequest() {
            return f.category().findCategoryByIssue(proxy);
          }
        }.setKeyForProxy(proxy.stableId(), CategoryProxy.class.getName()).fire();

      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    }.setKeyForProxy(issueId).fire();
  }
}