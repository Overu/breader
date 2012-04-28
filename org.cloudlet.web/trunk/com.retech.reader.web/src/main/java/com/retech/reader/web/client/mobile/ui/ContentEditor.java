package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;
import java.util.logging.Logger;

@Singleton
public class ContentEditor extends WavePanel implements Activity {

  private static final Logger logger = Logger.getLogger(ContentEditor.class.getName());
  private static final String LAST_PAGE = "now_reader";
  private final HTML html;
  private final PlaceController placeContorller;
  private final ReaderFactory f;
  private List<PageProxy> pages;
  private List<SectionProxy> sections;
  private PageProxy proxy;
  private int sectionIndex;
  private int pageIndex;
  private final Provider<BasePlace> place;
  private final LocalStorage storage;
  private final KeyUtil keyUtil;

  @Inject
  public ContentEditor(final PlaceController placeContorller, final ReaderFactory f,
      final Provider<BasePlace> place, final LocalStorage storage, final KeyUtil keyUtil) {
    this.placeContorller = placeContorller;
    this.f = f;
    this.place = place;
    this.storage = storage;
    this.keyUtil = keyUtil;
    html = new HTML();
    this.setWaveContent(html);

    html.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        int offsetWidth = event.getRelativeElement().getOffsetWidth();
        int x = event.getX();
        goTo(x > offsetWidth / 2 ? 1 : -1);
      }

    });
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
    final EntityProxyId<IssueProxy> issueId =
        ((BasePlace) placeContorller.getWhere()).getParam(IssueProxy.class);

    if (issueId != null) {
      PageProxy pageProxy = storage.get(keyUtil.proxyKey(issueId, LAST_PAGE));

      if (pageProxy != null) {
        placeContorller.goTo(place.get().setPath(ContentEditor.class.getName()).setParameter(
            pageProxy.stableId()));
        return;
      }

      new BaseReceiver<IssueProxy>() {

        @Override
        public void onSuccessAndCached(final IssueProxy issueProxy) {

          new BaseReceiver<PageProxy>() {

            @Override
            public void onSuccessAndCached(final PageProxy pageProxy) {
              pageStart(pageProxy);
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
      return;
    }

    final EntityProxyId<PageProxy> pageId =
        ((BasePlace) placeContorller.getWhere()).getParam(PageProxy.class);

    new BaseReceiver<PageProxy>() {

      @Override
      public void onSuccessAndCached(final PageProxy pageProxy) {
        pageStart(pageProxy);
      }

      @Override
      public Request<PageProxy> provideRequest() {
        return f.find(pageId).with(PageProxy.WITH);
      }
    }.setKeyForProxy(pageId).fire();
  }

  private void changePageProxy(final PageProxy pageProxy) {
    placeContorller.goTo(place.get().setPath(ContentEditor.class.getName()).setParameter(
        pageProxy.stableId()));
  }

  private void display(final PageProxy pageProxy) {
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        ContentEditor.this.getWaveTitle().setText(proxy.getTitle());
        html.setHTML(response.getDataString());
        storage.put(keyUtil.proxyKey(proxy.getSection().getIssue().stableId(), LAST_PAGE), proxy);
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getResource(proxy);
      }
    }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
  }

  private void findPages(final SectionProxy sectionProxy, final boolean isNextPage,
      final boolean isNextSection) {
    new BaseReceiver<List<PageProxy>>() {

      @Override
      public void onSuccessAndCached(final List<PageProxy> pages) {
        ContentEditor.this.pages = pages;
        if (isNextPage) {
          if (isNextSection) {
            changePageProxy(pages.get(0));
          } else {
            changePageProxy(pages.get(pages.size() - 1));
          }
        }
      }

      @Override
      public Request<List<PageProxy>> provideRequest() {
        return f.pageContext().findPagesBySection(sectionProxy).with(PageProxy.WITH);
      }
    }.setKeyForList(sectionProxy.stableId(), PageProxy.class.getName()).fire();
  }

  private void goTo(final int offset) {
    pageIndex = pages.indexOf(proxy);
    int index = pageIndex + offset;
    if (index < 0) {
      nextSectionIndex(--sectionIndex, false);
      return;
    } else if (index + 1 > pages.size()) {
      nextSectionIndex(++sectionIndex, true);
      return;
    }
    PageProxy pageProxy = pages.get(index);
    changePageProxy(pageProxy);
  }

  private void nextSectionIndex(int sectionIndex, final boolean isNextSection) {
    if (sectionIndex > sections.size() - 1 && pageIndex + 2 > pages.size()) {
      sectionIndex = sections.size() - 1;
      logger.info("最后一页");
      return;
    } else if (sectionIndex < 0) {
      sectionIndex = 0;
      logger.info("第一页");
      return;
    }
    findPages(sections.get(sectionIndex), true, isNextSection);
  }

  private void pageStart(final PageProxy pageProxy) {
    final SectionProxy sectionProxy = pageProxy.getSection();
    IssueProxy issue = sectionProxy.getIssue();
    this.proxy = pageProxy;
    display(pageProxy);

    new BaseReceiver<List<SectionProxy>>() {

      @Override
      public void onSuccessAndCached(final List<SectionProxy> sections) {
        ContentEditor.this.sections = sections;
        sectionIndex = sectionProxy.getSequence() - 1;
      }

      @Override
      public Request<List<SectionProxy>> provideRequest() {
        return f.section().findSectionByPage(pageProxy).with(SectionProxy.WITH);
      }
    }.setKeyForList(issue.stableId(), SectionProxy.class.getName()).fire();

    findPages(sectionProxy, false, false);
  }
}
