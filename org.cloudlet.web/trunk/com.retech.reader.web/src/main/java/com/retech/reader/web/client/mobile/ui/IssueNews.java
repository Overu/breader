package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.ToolbarClickButton;
import com.goodow.web.view.wave.client.ToolbarClickButton.State;
import com.goodow.web.view.wave.client.WavePanel;
import com.goodow.web.view.wave.client.WaveToolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseEditor;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class IssueNews extends BaseEditor<IssueProxy> {
  interface Binder extends UiBinder<Widget, IssueNews> {

  }
  interface Driver extends RequestFactoryEditorDriver<IssueProxy, IssueNews> {

  }
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
  DivElement datetimas;
  @UiField
  DivElement viewcount;
  @UiField
  DivElement categorys;
  @UiField
  SimplePanel toolbar;

  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static Driver driver = GWT.create(Driver.class);
  private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
  private List<IssueProxy> issueBook;
  private ReaderFactory f;
  private final Provider<BasePlace> places;
  private final WavePanel wavePanel;

  @Inject
  public IssueNews(final ReaderFactory f, final Provider<BasePlace> places,
      final WavePanel wavePanel) {
    this.f = f;
    this.places = places;
    this.wavePanel = wavePanel;
    // super.initEditor();
    wavePanel.setContent(binder.createAndBindUi(this));
    initWidget(wavePanel);

  }

  /**
   * Creates an icon Element.
   */
  public Element createIcon(final ImageResource imageResource) {
    return AbstractImagePrototype.create(imageResource).createElement();
  }

  @Override
  public void onStart(final ActivityState state) {

    toolbar.clear();
    WaveToolbar waveToolbar = new WaveToolbar();
    final ToolbarClickButton readButton = waveToolbar.addClickButton();
    readButton.setText("在线阅读");
    readButton.setVisualElement(createIcon(res.issueRead()));

    final ToolbarClickButton sectionButton = waveToolbar.addClickButton();
    sectionButton.setText("目录");
    sectionButton.setVisualElement(createIcon(res.issueSection()));

    final ToolbarClickButton addButton = waveToolbar.addClickButton();
    addButton.setText("收藏");
    addButton.setVisualElement(createIcon(res.issueAdd()));

    final ToolbarClickButton downloadButton = waveToolbar.addClickButton();
    downloadButton.setText("下载");
    downloadButton.setVisualElement(createIcon(res.issueDownload()));
    toolbar.add(waveToolbar);

    BasePlace place = (BasePlace) placeController.getWhere();
    final EntityProxyId<IssueProxy> issueId = place.getParam(IssueProxy.class);
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
    downloadButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        logger.info("开发中");
      }
    });
    /**
     * 获取书籍详情
     */
    new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {
        datetimas.setInnerHTML(dateFormat.format(proxy.getCreateTime()));
        viewcount.setInnerHTML(String.valueOf(proxy.getViewCount()));
        wavePanel.title().setText(proxy.getTitle());
        detail.setInnerHTML(proxy.getDetail());

        issueBook = storage.get(IssueProxy.MY_ISSUES, IssueProxy.class);
        addButton.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(final ClickEvent event) {

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
            categorys.setInnerHTML(response.getTitle());
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

  @Override
  protected RequestFactoryEditorDriver<IssueProxy, IssueNews> provideEditorDriver() {
    return driver;
  }

  @Override
  protected IssueContext provideRequestContext() {
    return f.issue();
  }

  @Override
  protected UiBinder provideUiBinder() {
    return binder;
  }
}