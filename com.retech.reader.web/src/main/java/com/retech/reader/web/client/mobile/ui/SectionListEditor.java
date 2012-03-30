package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.retech.reader.web.shared.proxy.PageProxy;
import com.retech.reader.web.shared.proxy.SectionProxy;
import com.retech.reader.web.shared.rpc.SectionDataProvider;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseListEditor;
import org.cloudlet.web.mvp.shared.rpc.RangeLabelPager;
import org.cloudlet.web.mvp.shared.rpc.ShowMorePagerPanel;

@Singleton
public class SectionListEditor extends BaseListEditor<SectionProxy> {
  interface Binder extends UiBinder<Widget, SectionListEditor> {
  }

  private static Binder binder = GWT.create(Binder.class);
  private final SectionDataProvider dataProvider;
  /**
   * The pager used to change the range of data.
   */
  @UiField
  public ShowMorePagerPanel pagerPanel;

  /**
   * The pager used to display the current range.
   */
  @UiField
  public RangeLabelPager rangeLabelPager;

  @Inject
  SectionListEditor(final SectionDataProvider dataProvider, final PlaceController placeController,
      final Provider<BasePlace> places) {
    this.dataProvider = dataProvider;
    super.initEditor();

    // Set the cellList as the display of the pagers. This example has two
    // pagers. pagerPanel is a scrollable pager that extends the range when the
    // user scrolls to the bottom. rangeLabelPager is a pager that displays the
    // current range, but does not have any controls to change the range.
    pagerPanel.setIncrementSize(5);
    pagerPanel.setDisplay(cellList);
    rangeLabelPager.setDisplay(cellList);

    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        BasePlace place = places.get().setPath(PageProxy.class.getName());
        place.setParameter(selectionModel.getLastSelectedObject().stableId());
        placeController.goTo(place);
      }
    });
  }

  @Override
  protected Cell<SectionProxy> provideCell() {
    return new SectionProxyCell();
  }

  @Override
  protected AbstractDataProvider<SectionProxy> provideDataProvider() {
    return dataProvider;
  }

  @Override
  protected UiBinder provideUiBinder() {
    return binder;
  }

}
