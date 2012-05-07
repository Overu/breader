/*
 * C Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.retech.reader.web.client.home;

import com.goodow.web.view.wave.client.panel.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
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
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class MyDownLoadPanel extends WavePanel implements Activity {

  interface Binder extends UiBinder<Widget, MyDownLoadPanel> {
  }

  interface Resources extends ClientBundle {
    ImageResource addIssue();
  }

  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<img src=\"{0}\"  width='100%' height='100%'>")
    SafeHtml img(SafeUri image);

    @SafeHtmlTemplates.Template("{0}")
    SafeHtml info(String text);
  }

  private static Resources res = GWT.create(Resources.class);
  private static Binder binder = GWT.create(Binder.class);
  private static Template template = GWT.create(Template.class);

  @UiField
  FlowPanel myDownLoadPanel;
  private final Provider<BasePlace> places;
  private final PlaceController placeController;
  private final ReaderFactory f;
  private final KeyUtil keyUtil;
  private final LocalStorage storage;

  @Inject
  MyDownLoadPanel(final ReaderFactory f, final Provider<BasePlace> places,
      final PlaceController placeController, final KeyUtil keyUtil, final LocalStorage storage) {
    this.f = f;
    this.places = places;
    this.placeController = placeController;
    this.keyUtil = keyUtil;
    this.storage = storage;
    this.getWaveTitle().setText("我的下载");
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
    List<IssueProxy> issueDownload = storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN));
    myDownLoadPanel.clear();

    if (issueDownload != null) {
      displayIssue(issueDownload, false);
      return;
    }

    HTMLPanel downLoad = new HTMLPanel("");
    HTMLPanel imagePanel = new HTMLPanel(AbstractImagePrototype.create(res.addIssue()).getHTML());
    imagePanel.getElement().getStyle().setOpacity(0);
    downLoad.add(imagePanel);
    downLoad.add(new Label("下载"));
    downLoad.getElement().getStyle().setOpacity(0);
    myDownLoadPanel.getElement().getStyle().setCursor(Cursor.POINTER);
    myDownLoadPanel.add(downLoad);
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
      myDownLoadPanel.add(issuePanel);

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
