package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.PageContext;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseEditor;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class ContentEditor extends BaseEditor<PageProxy> {
  interface Binder extends UiBinder<Widget, ContentEditor> {
  }
  interface Driver extends RequestFactoryEditorDriver<PageProxy, ContentEditor> {
  }

  private static Binder binder = GWT.create(Binder.class);
  private static Driver driver = GWT.create(Driver.class);
  @UiField
  @Ignore
  HTML html;

  private final ReaderFactory f;

  private List<PageProxy> contents;
  private List<SectionProxy> sections;

  private int sectionIndex;

  @Inject
  ContentEditor(final ReaderFactory f, final WavePanel wavePanel) {
    this.f = f;
    // super.initEditor();
    Widget createAndBindUi = binder.createAndBindUi(this);
    wavePanel.setContent(createAndBindUi);
    wavePanel.title();
    createAndBindUi.getParent().getElement().getStyle().setOverflow(Overflow.VISIBLE);
    createAndBindUi.getParent().getParent().getElement().getStyle().setOverflow(Overflow.AUTO);
    initWidget(wavePanel);

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
  public void onStart(final ActivityState state) {

    html.setHTML("");
    final EntityProxyId<IssueProxy> issueEntityId =
        ((BasePlace) placeController.getWhere()).getParam(IssueProxy.class);

    if (issueEntityId != null) {
      findPageProxyByIssueProxy(issueEntityId);
      return;
    }

    final EntityProxyId<PageProxy> pageEntityId =
        ((BasePlace) placeController.getWhere()).getParam(PageProxy.class);

    findPageByPageProxy(pageEntityId);
  }

  @Override
  protected RequestFactoryEditorDriver<PageProxy, ContentEditor> provideEditorDriver() {
    return driver;
  }

  @Override
  protected PageContext provideRequestContext() {
    return f.pageContext();
  }

  @Override
  protected UiBinder provideUiBinder() {
    return binder;
  }

  private void display(final PageProxy proxy) {
    this.proxy = proxy;
    // provideEditorDriver().display(proxy);
    new BaseReceiver<ResourceProxy>() {

      @Override
      public void onSuccessAndCached(final ResourceProxy response) {
        html.setHTML(response.getDataString());
      }

      @Override
      public Request<ResourceProxy> provideRequest() {
        return f.resource().getResource(proxy);
      }
    }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();
  }

  private void findContentBySectionProxy(final SectionProxy sectionProxy, final boolean isStart,
      final boolean isNextPage) {

    new BaseReceiver<List<PageProxy>>() {

      @Override
      public void onSuccessAndCached(final List<PageProxy> pageList) {
        sectionIndex = sectionProxy.getSequence() - 1;
        contents = pageList;
        if (!isStart) {
          if (isNextPage) {
            display(contents.get(0));
          } else {
            display(contents.get(contents.size() - 1));
          }
        }
      }

      @Override
      public Request<List<PageProxy>> provideRequest() {
        return provideRequestContext().findPagesBySection(sectionProxy);
      }
    }.setKeyForList(sectionProxy.stableId(), PageProxy.class.getName()).fire();
  }

  private void findPageByPageProxy(final EntityProxyId<PageProxy> pageEntityId) {

    new BaseReceiver<PageProxy>() {

      @Override
      public void onSuccessAndCached(final PageProxy pageProxy) {
        display(pageProxy);

        // findSectionByPage(pageProxy, true);

        EntityProxyId<IssueProxy> issueId = pageProxy.getSection().getIssue().stableId();
        new BaseReceiver<List<SectionProxy>>() {

          @Override
          public void onSuccessAndCached(final List<SectionProxy> sectionProxyList) {
            sections = sectionProxyList;
            findContentBySectionProxy(pageProxy.getSection(), true, false);
          }

          @Override
          public Request<List<SectionProxy>> provideRequest() {
            return f.section().findSectionByPage(pageProxy);
          }
        }.setKeyForList(issueId, SectionProxy.class.getName()).fire();
      }

      @Override
      public Request<PageProxy> provideRequest() {
        return f.find(pageEntityId).with(PageProxy.WITH);
      }
    }.setKeyForProxy(pageEntityId).fire();
  }

  private void findPageProxyByIssueProxy(final EntityProxyId<IssueProxy> issueEntityId) {
    new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy issueProxy) {
        // History.addValueChangeHandler(new ValueChangeHandler<String>() {
        // boolean start = true;
        //
        // @Override
        // public void onValueChange(final ValueChangeEvent<String> event) {
        // if (start) {
        // start = false;
        // placeController.goTo(provider.get().setPath(IssueEditor.class.getName())
        // .setParameter(issueProxy.stableId()));
        // return;
        // }
        // }
        // });

        new BaseReceiver<PageProxy>() {

          @Override
          public void onSuccessAndCached(final PageProxy pageProxy) {
            findPageByPageProxy(pageProxy.stableId());
          }

          @Override
          public Request<PageProxy> provideRequest() {
            return provideRequestContext().findFirstPageByIssue(issueProxy).with(PageProxy.WITH);
          }
        }.setKeyForProxy(issueProxy.stableId(), PageProxy.class.getName()).fire();
      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueEntityId);
      }
    }.setKeyForProxy(issueEntityId).fire();
  }

  private void goTo(final int offset) {
    int current = contents.indexOf(proxy);
    // PageProxy pageProxy = contents.get(offset + current);
    if ((current + offset + 1) > contents.size()) {
      nextPage(++sectionIndex, true);
      return;
    } else if ((current + offset - 1) < 0) {
      nextPage(--sectionIndex, false);
      return;
    }
    display(contents.get(current + offset));
  }

  private void nextPage(final int indexSection, final boolean isNextPage) {
    if (indexSection > sections.size() - 1 && (contents.indexOf(proxy) + 2) > contents.size()) {
      // findContentBySectionProxy(sections.get(0), false);
      sectionIndex = sections.size() - 1;
      logger.info("最后一页");
      return;
    } else if (indexSection < 0) {
      // findContentBySectionProxy(sections.get(sections.size() - 1), false);
      sectionIndex = 0;
      logger.info("第一页");
      return;
    }
    findContentBySectionProxy(sections.get(indexSection), false, isNextPage);
  }
}