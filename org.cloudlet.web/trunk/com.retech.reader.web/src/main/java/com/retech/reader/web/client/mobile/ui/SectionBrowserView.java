package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseContext;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

@Singleton
public class SectionBrowserView extends WavePanel implements ActivityAware {
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
  }

  @Override
  public void onStart(final ActivityState state) {
    BasePlace place = (BasePlace) placeController.getWhere();
    final EntityProxyId<IssueProxy> issueId = place.getParam(IssueProxy.class);
    CellBrowser cellBrowser = new CellBrowser(sectionTreeViewModel, null);
    cellBrowser.setDefaultColumnWidth(300);
    cellBrowser.setAnimationEnabled(true);
    cellBrowser.getElement().getStyle().setPosition(Position.STATIC);
    setContent(cellBrowser);

    BaseReceiver<IssueProxy> baseReceiver = new BaseReceiver<IssueProxy>() {

      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {
        title().setText(proxy.getTitle());
      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    };
    baseReceiver.setKeyForProxy(issueId).fire();
  }

  protected BaseContext provideRequestContext() {
    return f.issue();
  }

}
