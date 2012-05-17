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

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.widget.progress.ProgressWidget;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
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
import com.google.gwt.storage.client.Storage;
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
import com.retech.reader.web.shared.common.SQLConstant;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.PageContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

  private static final Logger logger = Logger.getLogger(MyDownLoadPanel.class.getName());

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
  private final Provider<ProgressWidget> progresses;

  // private List<IssueProxy> issueDownload;

  // private List<IssueProxy> issueDownloadFinish;

  @Inject
  MyDownLoadPanel(final ReaderFactory f, final Provider<BasePlace> places,
      final PlaceController placeController, final KeyUtil keyUtil, final LocalStorage storage,
      final Provider<ProgressWidget> progresses) {
    this.f = f;
    this.places = places;
    this.placeController = placeController;
    this.keyUtil = keyUtil;
    this.storage = storage;
    this.progresses = progresses;
    this.getWaveTitle().setText(IssueProxy.ISSUE_DOWN_NAME);
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

    new BaseReceiver<List<IssueProxy>>() {

      @Override
      public void onSuccessAndCached(final List<IssueProxy> helpIssue) {
        List<IssueProxy> issueDownloadFinish =
            storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN_FINISH));
        myDownLoadPanel.clear();

        if (issueDownloadFinish == null) {
          storage.put(keyUtil.listKey(IssueProxy.ISSUE_DOWN), helpIssue);
        } else {
          displayIssue(issueDownloadFinish, false);
        }

        List<IssueProxy> issueDownload = storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN));

        if (issueDownload != null) {
          displayIssue(issueDownload, true);
          // return;
        }

        HTMLPanel downLoad = new HTMLPanel("");
        HTMLPanel imagePanel =
            new HTMLPanel(AbstractImagePrototype.create(res.addIssue()).getHTML());
        // imagePanel.getElement().getStyle().setOpacity(0);
        downLoad.add(imagePanel);
        downLoad.add(new Label(IssueProxy.ISSUE_STATE_DOWN));
        // downLoad.getElement().getStyle().setOpacity(0);
        myDownLoadPanel.getElement().getStyle().setCursor(Cursor.POINTER);
        myDownLoadPanel.add(downLoad);
      }

      @Override
      public Request<List<IssueProxy>> provideRequest() {
        return f.issue().findHelpIssue(7);
      }
    }.setKey(keyUtil.listKey(IssueProxy.HELP_ISSUE)).fire();

  }

  private void displayIssue(final List<IssueProxy> proxys, final boolean isDownloadFinish) {
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

      if (isDownloadFinish) {
        ProgressWidget progressWidget = progresses.get();
        IssueDownloadMessage issueDownloadMessage = new IssueDownloadMessage();
        issueDownloadMessage.setProgress(progressWidget);
        issueDownloadMessage.setIssueProxy(issue);
        issuePanel.add(progressWidget);
        downLoadIssue(issueDownloadMessage, issue.stableId());
      } else if (issuePanel.getWidgetCount() == 3) {
        issuePanel.remove(2);
      }

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

  private void displayProgress(final IssueDownloadMessage issueDownloadMessage) {
    int pageCount = issueDownloadMessage.getPageCount();
    int nowPage = issueDownloadMessage.getNowPage() + 1;
    issueDownloadMessage.setNowPage(nowPage);
    // logger.info("now download:" + nowPage + "/" + pageCount);
    issueDownloadMessage.getProgress().setValue((double) nowPage / pageCount);
  }

  private void displayResource(final ResourceProxy resource, final HTMLPanel imagePanel) {
    SafeHtml safe =
        template.img(UriUtils.fromTrustedString("data:" + resource.getMimeType().getType()
            + ";base64," + resource.getDataString()));
    String asString = safe.asString();
    imagePanel.add(new HTML(asString));
  }

  private void downLoadIssue(final IssueDownloadMessage issueDownloadMessage,
      final EntityProxyId<IssueProxy> issueId) {
    new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy issueProxy) {

        new BaseReceiver<List<SectionProxy>>() {

          @Override
          public void onSuccessAndCached(final List<SectionProxy> sections) {
            firePageBySection(issueDownloadMessage, sections, issueProxy);
          }

          @Override
          public Request<List<SectionProxy>> provideRequest() {
            return f.section().findByBook(issueProxy, 0, SQLConstant.MAX_RESULTS_ALL).with(
                SectionProxy.WITH);
          }
        }.setKeyForList(issueId, SectionProxy.class.getName()).fire();

      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    }.setKeyForProxy(issueId).fire();
  }

  private void firePageBySection(final IssueDownloadMessage issueDownloadMessage,
      final List<SectionProxy> sections, final IssueProxy issueProxy) {
    final PageContext pageContext = f.pageContext();
    int pageCount = 0;
    for (final SectionProxy sectionProxy : sections) {
      pageCount += sectionProxy.getPageCount();
      issueDownloadMessage.setPageCount(pageCount + sections.size());
      new BaseReceiver<List<PageProxy>>() {

        @Override
        public void onSuccessAndCached(final List<PageProxy> pages) {
          displayProgress(issueDownloadMessage);
          fireReourceByPage(issueDownloadMessage, pages, issueProxy);
        }

        @Override
        public Request<List<PageProxy>> provideRequest() {
          return pageContext.findPagesBySection(sectionProxy).with(PageProxy.WITH);
        }
      }.setKeyForList(sectionProxy.stableId(), PageProxy.class.getName()).fire();
    }
  }

  private void fireReourceByPage(final IssueDownloadMessage issueDownloadMessage,
      final List<PageProxy> pages, final IssueProxy issueProxy) {
    for (final PageProxy pageProxy : pages) {
      new BaseReceiver<ResourceProxy>() {

        @Override
        public void onSuccessAndCached(final ResourceProxy response) {
          displayProgress(issueDownloadMessage);
          if (issueDownloadMessage.getNowPage() == issueDownloadMessage.getPageCount()) {
            List<IssueProxy> issueDownload = storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN));
            List<IssueProxy> issueDownloadFinish =
                storage.get(keyUtil.listKey(IssueProxy.ISSUE_DOWN_FINISH));
            if (issueDownload == null) {
              issueDownload = new ArrayList<IssueProxy>();
            }
            if (issueDownloadFinish == null) {
              issueDownloadFinish = new ArrayList<IssueProxy>();
            }
            if (issueDownload.contains(issueProxy)) {
              issueDownload.remove(issueProxy);
            }
            if (!issueDownloadFinish.contains(issueProxy)) {
              issueDownloadFinish.add(issueProxy);
            }
            if (issueDownload.size() == 0) {
              Storage.getLocalStorageIfSupported().removeItem(
                  keyUtil.listKey(IssueProxy.ISSUE_DOWN));
            } else {
              storage.put(keyUtil.listKey(IssueProxy.ISSUE_DOWN), issueDownload);
            }
            storage.put(keyUtil.listKey(IssueProxy.ISSUE_DOWN_FINISH), issueDownloadFinish);
            issueDownloadMessage.getProgress().getElement().getStyle().setDisplay(Display.NONE);
            // logger.info("download end!!");
          }
        }

        @Override
        public Request<ResourceProxy> provideRequest() {
          return f.resource().getResource(pageProxy);
        }
      }.setKeyForProxy(pageProxy.stableId(), ResourceProxy.class.getName()).fire();
    }
  }
}
