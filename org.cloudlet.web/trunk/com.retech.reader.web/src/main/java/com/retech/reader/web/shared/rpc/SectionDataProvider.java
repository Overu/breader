package com.retech.reader.web.shared.rpc;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.common.SQLConstant;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class SectionDataProvider extends AsyncDataProvider<SectionProxy> {

  private final ReaderFactory f;

  private final LocalStorage storage;

  private final PlaceController placeController;

  @Inject
  SectionDataProvider(final ReaderFactory f, final LocalStorage storage,
      final PlaceController placeController) {
    this.f = f;
    this.storage = storage;
    this.placeController = placeController;
  }

  @Override
  protected void onRangeChanged(final HasData<SectionProxy> display) {
    BasePlace place = (BasePlace) placeController.getWhere();
    final EntityProxyId<IssueProxy> issueId = place.getParam(IssueProxy.class);

    final Range range = display.getVisibleRange();
    new BaseReceiver<IssueProxy>() {
      @Override
      public void onSuccessAndCached(final IssueProxy proxy) {

        new BaseReceiver<List<SectionProxy>>() {
          @Override
          public void onSuccessAndCached(final List<SectionProxy> response) {
            updateRowData(range.getStart(), response);
            updateRowCount(response.size(), true);
          }

          @Override
          public Request<List<SectionProxy>> provideRequest() {
            return f.section().findByBook(proxy, 0, SQLConstant.MAX_RESULTS_ALL);
          }
        }.setKeyForList(issueId, SectionProxy.class.getName()).fire();
      }

      @Override
      public Request<IssueProxy> provideRequest() {
        return f.find(issueId);
      }
    }.setKeyForProxy(issueId).fire();
  }

}
