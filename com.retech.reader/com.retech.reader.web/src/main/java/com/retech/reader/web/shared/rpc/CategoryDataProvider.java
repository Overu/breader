package com.retech.reader.web.shared.rpc;

import com.goodow.web.core.shared.rpc.BaseReceiver;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.common.SQLConstant;
import com.retech.reader.web.shared.proxy.CategoryProxy;


import java.util.List;

@Singleton
public class CategoryDataProvider extends AsyncDataProvider<CategoryProxy> {

  private final ReaderFactory f;

  @Inject
  CategoryDataProvider(final ReaderFactory f) {
    this.f = f;
  }

  @Override
  protected void onRangeChanged(final HasData<CategoryProxy> display) {

    final Range range = display.getVisibleRange();
    display.setVisibleRange(0, SQLConstant.MAX_RESULTS_ALL);

    new BaseReceiver<List<CategoryProxy>>() {

      @Override
      public void onSuccessAndCached(final List<CategoryProxy> response) {
        updateRowData(range.getStart(), response);
      }

      @Override
      public Request<List<CategoryProxy>> provideRequest() {
        return f.category().find(0, SQLConstant.MAX_RESULTS_ALL);
      }

    }.setKeyForList(CategoryProxy.CATEGORY).fire();
  }
}
