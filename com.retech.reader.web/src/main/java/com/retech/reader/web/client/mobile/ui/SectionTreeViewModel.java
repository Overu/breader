package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.PageDataProvider;
import com.retech.reader.web.shared.rpc.SectionDataProvider;

import org.cloudlet.web.mvp.shared.BasePlace;

public class SectionTreeViewModel implements TreeViewModel {
  private final SectionDataProvider sectionDataProvider;
  private final Provider<PageDataProvider> pageDataProviders;
  private final Cell<PageProxy> pageProxyCell = new AbstractCell<PageProxy>() {

    @Override
    public void render(final com.google.gwt.cell.client.Cell.Context context,
        final PageProxy value, final SafeHtmlBuilder sb) {
      if (value == null) {
        return;
      }
      sb.append(SafeHtmlUtils.fromString(value.getPageNum() + " " + value.getTitle()));
    }

  };
  private NoSelectionModel<PageProxy> selectionModel;

  @Inject
  SectionTreeViewModel(final SectionDataProvider sectionDataProvider,
      final Provider<PageDataProvider> pageDataProviders, final PlaceController placeController,
      final Provider<BasePlace> places) {
    this.sectionDataProvider = sectionDataProvider;
    this.pageDataProviders = pageDataProviders;

    selectionModel = new NoSelectionModel<PageProxy>();
    selectionModel.addSelectionChangeHandler(new Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        // String historyToken =
        // f.getHistoryToken(selectionModel.getLastSelectedObject().stableId());
        // placeController.goTo(places.get().setPath(historyToken));
        placeController.goTo(places.get().setPath(ContentEditor.class.getName()).setParameter(
            selectionModel.getLastSelectedObject().stableId()));
      }
    });
  }

  @Override
  public <T> NodeInfo<?> getNodeInfo(final T value) {
    if (value == null) {
      // Return top level categories.
      return new DefaultNodeInfo<SectionProxy>(sectionDataProvider, new SectionProxyCell());
    } else if (value instanceof SectionProxy) {
      PageDataProvider pageDataProvider = pageDataProviders.get();
      pageDataProvider.setSectionProxy((SectionProxy) value);
      return new DefaultNodeInfo<PageProxy>(pageDataProvider, pageProxyCell, selectionModel, null);
    }
    // Unhandled type.
    String type = value.getClass().getName();
    throw new IllegalArgumentException("Unsupported object type: " + type);
  }

  @Override
  public boolean isLeaf(final Object value) {
    return value instanceof PageProxy;
  }

}
