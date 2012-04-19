package com.retech.reader.web.shared.rpc;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;

import org.cloudlet.web.service.shared.KeyUtil;
import org.cloudlet.web.service.shared.LocalStorage;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

public class PageDataProvider extends AsyncDataProvider<PageProxy> {

  private final ReaderFactory f;

  private final LocalStorage storage;

  private SectionProxy sectionProxy;

  private final KeyUtil keyUtil;

  @Inject
  PageDataProvider(final ReaderFactory f, final LocalStorage storage, final KeyUtil keyUtil) {
    this.f = f;
    this.storage = storage;
    this.keyUtil = keyUtil;
  }

  public void setSectionProxy(final SectionProxy sectionProxy) {
    this.sectionProxy = sectionProxy;
  }

  @Override
  protected void onRangeChanged(final HasData<PageProxy> display) {
    List<PageProxy> list =
        storage.get(keyUtil.proxyListKey(sectionProxy.stableId(), PageProxy.class.getName()));

    final Range range = display.getVisibleRange();
    if (list != null) {
      updateRowData(range.getStart(), list);
      updateRowCount(list.size(), true);
      return;
    }

    rpc(sectionProxy, range);
  }

  private void rpc(final SectionProxy parentProxy, final Range range) {
    final PageContext ctx = f.pageContext();
    new BaseReceiver<List<PageProxy>>() {
      @Override
      public void onSuccessAndCached(final List<PageProxy> response) {
        storage.put(parentProxy.stableId(), response);
        updateRowData(range.getStart(), response);

        response.get(0).stableId();
        updateRowCount(response.size(), true);

      }

      @Override
      public Request<List<PageProxy>> provideRequest() {
        return ctx.findPagesBySection(parentProxy).with(PageProxy.WITH);
      }
    }.setKeyForList(parentProxy.stableId(), SectionProxy.class.getName()).fire();
  }
}
