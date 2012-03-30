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
import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class BookDataProvider extends AsyncDataProvider<IssueProxy> {

  private final ReaderFactory f;
  private final PlaceController placeControllr;

  @Inject
  BookDataProvider(final ReaderFactory f, final PlaceController placeControllr) {
    this.f = f;
    this.placeControllr = placeControllr;
  }

  @Override
  protected void onRangeChanged(final HasData<IssueProxy> display) {
    BasePlace basePlace = (BasePlace) placeControllr.getWhere();
    final EntityProxyId<CategoryProxy> categoryId = basePlace.getParam(CategoryProxy.class);

    final Range range = display.getVisibleRange();
    final IssueContext ctx = f.issue();

    // List<IssueProxy> list = storage.get(categoryId, IssueProxy.class);
    // if (list != null) {
    // updateRowData(range.getStart(), list);
    // updateRowCount(list.size(), true);
    // return;
    // }

    new BaseReceiver<CategoryProxy>() {

      @Override
      public void onSuccessAndCached(final CategoryProxy response) {
        // new BaseReceiver<Long>() {
        //
        // @Override
        // public void onSuccessAndCached(final Long response) {
        // updateRowCount(response.intValue(), true);
        //
        // }
        //
        // @Override
        // public Request<Long> provideRequest() {
        // return ctx.count();
        // }
        // }.fire();
        new BaseReceiver<List<IssueProxy>>() {

          @Override
          public void onSuccessAndCached(final List<IssueProxy> response) {
            updateRowData(range.getStart(), response);

          }

          @Override
          public Request<List<IssueProxy>> provideRequest() {
            return ctx.findByCategory(response, 0, SQLConstant.MAX_RESULTS_ALL).with("image");
          }
        }.setKeyForList(categoryId, IssueProxy.class.getName()).fire();
      }

      @Override
      public Request<CategoryProxy> provideRequest() {
        return f.find(categoryId);
      }
    }.setKeyForProxy(categoryId).fire();

  }
}
