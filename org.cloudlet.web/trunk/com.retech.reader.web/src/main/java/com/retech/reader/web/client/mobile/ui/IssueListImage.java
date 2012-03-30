package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.IssueContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseEditor;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

public class IssueListImage extends BaseEditor<IssueProxy> {
  interface Binder extends UiBinder<Widget, IssueListImage> {
  }

  interface Driver extends RequestFactoryEditorDriver<IssueProxy, IssueListImage> {
  }
  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<img src=\"{0}\"  width='100%' height='100%'>")
    SafeHtml img(SafeUri image);
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Driver driver = GWT.create(Driver.class);
  private static Template template = GWT.create(Template.class);

  private ReaderFactory f;
  private final Provider<BasePlace> places;

  @UiField
  HTMLPanel imageDiv;

  @Inject
  IssueListImage(final ReaderFactory f, final Provider<BasePlace> places, final WavePanel wavePanel) {
    this.f = f;
    this.places = places;
    wavePanel.setContent(binder.createAndBindUi(this));
    initWidget(wavePanel);
    wavePanel.title().setText("相关推荐");
  }

  @Override
  public void onStart(final ActivityState state) {
    final IssueContext ctx = provideRequestContext();
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

  @Override
  protected RequestFactoryEditorDriver<IssueProxy, IssueListImage> provideEditorDriver() {
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
