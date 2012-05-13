package com.retech.reader.web.client.mobile.ui;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarButtonView.State;
import com.goodow.wave.client.widget.toolbar.buttons.ToolBarClickButton;
import com.goodow.wave.client.widget.toolbar.buttons.WaveToolBar;

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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
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
  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static final Logger logger = Logger.getLogger(IssueNews.class.getName());
  // private static Driver driver = GWT.create(Driver.class);
  private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
  private ReaderFactory f;
  private EntityProxyId<IssueProxy> issueId;
  private ToolBarClickButton addButton;
  private ToolBarClickButton downloadButton;
  private IssueProxy proxy;
  private final PlaceController placeController;
  private final LocalStorage storage;
  private final WaveToolBar waveToolbar;
  private final KeyUtil keyUtil;

  @Inject
  public IssueNews(final ReaderFactory f, final Provider<BasePlace> places,
      final PlaceController placeController, final LocalStorage storage, final TagsPanel tagsPanel,
      final UserStatus userStatus, final WaveToolBar waveToolbar, final KeyUtil keyUtil) {
    this.f = f;
    this.placeController = placeController;
    this.storage = storage;
    this.waveToolbar = waveToolbar;
    this.keyUtil = keyUtil;

    add(userStatus);
    add(waveToolbar);

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("7.1 书本详细内容展示"));
    toDo.add(new Label("7.2 在线阅读"));
    toDo.add(new Label("7.3 目录"));
    toDo.add(new Label("7.4 收藏状态"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("7.5 界面布局调整（中）"));
    toDo.add(new Label("7.6 下载（难）"));
    toDo.add(new Label("7.7 评论（难）"));
    toDo.add(new Label("7.8 浏览次数（易）"));
    toDo.add(new Label("7.9 该书读者用户在线状态及实时交流（难）"));
    toDo.add(new Label("7.10 标签展示（中）"));
    toDo.add(new Label("7.11 添加标签（难）"));
    add(toDo);

    this.setWaveContent(binder.createAndBindUi(this));
    add(tagsPanel);

    final ToolBarClickButton readButton = waveToolbar.addClickButton();
    readButton.setText("在线阅读");
    readButton.setVisualElement(createIcon(res.issueRead()));

    final ToolBarClickButton sectionButton = waveToolbar.addClickButton();
    sectionButton.setText("目录");
    sectionButton.setVisualElement(createIcon(res.issueSection()));

    addButton = waveToolbar.addClickButton();
    addButton.setText("收藏");
    addButton.setVisualElement(createIcon(res.issueAdd()));

    downloadButton = waveToolbar.addClickButton();
    downloadButton.setText("下载");
    downloadButton.setVisualElement(createIcon(res.issueDownload()));
    readButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {

        new BaseReceiver<IssueProxy>() {

          @Override
          public void onSuccessAndCached(final IssueProxy issueProxy) {

            new BaseReceiver<PageProxy>() {

              @Override
              public void onSuccessAndCached(final PageProxy pageProxy) {
                PageProxy lastPage =
                    storage.get(keyUtil.proxyKey(issueId, ContentEditor.LAST_PAGE));
                placeController.goTo(places.get().setPath(ContentEditor.class.getName())
                    .setParameter(lastPage == null ? pageProxy.stableId() : lastPage.stableId()));
              }

              @Override
              public Request<PageProxy> provideRequest() {
                return f.pageContext().findFirstPageByIssue(issueProxy).with(PageProxy.WITH);
              }
            }.setKeyForProxy(issueId, PageProxy.class.getName()).fire();
          }

          @Override
          public Request<IssueProxy> provideRequest() {
            return f.find(issueId);
          }
        }.setKeyForProxy(issueId).fire();

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
        List<IssueProxy> issueBook = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));
        if (issueBook == null) {
          issueBook = new ArrayList<IssueProxy>();
        }
        if (!issueBook.contains(proxy)) {
          issueBook.add(proxy);
          logger.info("已收藏");
          addButton.setState(State.DISABLED);
        }
        storage.put(keyUtil.listKey(IssueProxy.MY_ISSUES), issueBook);
      }
    });
    downloadButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        // logger.info("开发中");
        List<IssueProxy> issueDownload = storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN));
        if (issueDownload == null) {
          issueDownload = new ArrayList<IssueProxy>();
        }
        if (!issueDownload.contains(proxy)) {
          issueDownload.add(proxy);
          downloadButton.setState(State.DISABLED);
        }
        storage.put(keyUtil.listKey(IssueProxy.ISSUE_DOWN), issueDownload);
        placeController.goTo(places.get().setPath("/"));
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
        List<IssueProxy> issueBook = storage.get(keyUtil.listKey(IssueProxy.MY_ISSUES));
        if (issueBook == null) {
          issueBook = new ArrayList<IssueProxy>();
        }
        if (!issueBook.contains(proxy)) {
          addButton.setState(State.ENABLED);
        } else {
          addButton.setState(State.DISABLED);
        }

        List<IssueProxy> issueDownload = storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN));
        List<IssueProxy> issueDownloadFinish =
            storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN_FINISH));
        if (issueDownload == null) {
          issueDownload = new ArrayList<IssueProxy>();
        }
        if (issueDownloadFinish == null) {
          issueDownloadFinish = new ArrayList<IssueProxy>();
        }
        if (issueDownload.contains(proxy) || issueDownloadFinish.contains(proxy)) {
          downloadButton.setState(State.DISABLED);
        } else {
          downloadButton.setState(State.ENABLED);
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