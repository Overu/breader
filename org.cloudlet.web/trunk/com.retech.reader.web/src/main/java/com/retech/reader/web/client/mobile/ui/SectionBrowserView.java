package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.panel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

@Singleton
public class SectionBrowserView extends WavePanel implements Activity {
  interface Binder extends UiBinder<Widget, SectionBrowserView> {
  }

  /**
   * The UiBinder used to generate the view.
   */
  @Inject
  protected PlaceController placeController;
  private final ReaderFactory f;
  private final SectionTreeViewModel sectionTreeViewModel;

  @Inject
  SectionBrowserView(final SectionTreeViewModel sectionTreeViewModel, final ReaderFactory f) {
    this.sectionTreeViewModel = sectionTreeViewModel;
    this.f = f;
    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("9.1 目录及各页树状结构的展示"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("9.2 整行可点击（中）"));
    toDo.add(new Label("9.3 界面调整（中）"));
    add(toDo);
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
    BasePlace place = (BasePlace) placeController.getWhere();
    final EntityProxyId<IssueProxy> issueId = place.getParam(IssueProxy.class);
    CellTree cellTree = new CellTree(sectionTreeViewModel, null);
    cellTree.setAnimationEnabled(true);
    setWaveContent(cellTree);

    BaseReceiver<IssueProxy> baseReceiver = new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {
        getWaveTitle().setText(proxy.getTitle());
      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    };
    baseReceiver.setKeyForProxy(issueId).fire();
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    this.remove(2);
  }
}
