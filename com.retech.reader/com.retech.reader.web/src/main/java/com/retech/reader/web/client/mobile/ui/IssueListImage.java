package com.retech.reader.web.client.mobile.ui;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.web.mvp.shared.BasePlace;
import com.goodow.web.mvp.shared.tree.rpc.BaseReceiver;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;


import java.util.List;

public class IssueListImage extends WavePanel implements Activity {
  interface Binder extends UiBinder<Widget, IssueListImage> {
  }

  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<img src=\"{0}\"  width='100%' height='100%'>")
    SafeHtml img(SafeUri image);
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Template template = GWT.create(Template.class);

  private ReaderFactory f;
  private final Provider<BasePlace> places;
  private final PlaceController placeController;

  @UiField
  HTMLPanel imageDiv;

  @Inject
  IssueListImage(final ReaderFactory f, final Provider<BasePlace> places,
      final PlaceController placeController) {
    this.f = f;
    this.places = places;
    this.placeController = placeController;
    this.getWaveTitle().setText("相关推荐");
    // FlowPanel toDo = new FlowPanel();
    // toDo.addStyleName(WavePanelResources.css().waveWarning());
    // toDo.add(new TEXT_HTML("<b>已完成：<b>"));
    // toDo.add(new Label("8.1 推荐同类别图书"));
    // toDo.add(new TEXT_HTML("<br>"));
    // toDo.add(new TEXT_HTML("<b>待实现：<b>"));
    // toDo.add(new Label("8.2 界面布局调整（易）"));
    // toDo.add(new Label("8.3 根据用户阅读行为进行推荐（难）"));
    // add(toDo);
    this.setWaveContent(binder.createAndBindUi(this));
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
    final IssueContext ctx = f.issue();
    BasePlace place = (BasePlace) placeController.getWhere();
    final EntityProxyId<IssueProxy> issueId = place.getParam(IssueProxy.class);
    /**
     * 获取书籍详情
     */
    new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {

        /**
         * 相关推荐
         */
        new BaseReceiver<List<IssueProxy>>() {
          @Override
          public void onSuccessAndCached(final List<IssueProxy> response) {
            imageDiv.clear();
            response.size();
            for (final IssueProxy issue : response) {
              new BaseReceiver<ResourceProxy>() {
                @Override
                public void onSuccessAndCached(final ResourceProxy resource) {

                  SafeHtml image =
                      template.img(UriUtils.fromTrustedString("data:"
                          + resource.getMimeType().getType() + ";base64,"
                          + resource.getDataString()));

                  HTMLPanel panelImage = new HTMLPanel(image);

                  panelImage.addDomHandler(new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                      placeController.goTo(places.get().setPath(IssueEditor.class.getName())
                          .setParameter(issue.stableId()));
                      imageDiv.clear();
                    }
                  }, ClickEvent.getType());
                  imageDiv.add(panelImage);
                }

                @Override
                public Request<ResourceProxy> provideRequest() {
                  return f.resource().getImage(issue);
                }
              }.setKeyForProxy(issue.stableId(), ResourceProxy.class.getName()).fire();
            }
          }

          @Override
          public Request<List<IssueProxy>> provideRequest() {
            return ctx.findRecommend(proxy, 0, 5, true);
          }
        }.fire();

      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    }.setKeyForProxy(issueId).fire();
  }
}
