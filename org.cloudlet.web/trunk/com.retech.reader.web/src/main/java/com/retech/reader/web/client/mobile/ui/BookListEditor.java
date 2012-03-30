package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.rpc.BookDataProvider;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.rpc.BaseListEditor;
import org.cloudlet.web.mvp.shared.rpc.ShowMorePagerPanel;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

/**
 * View used to display the list of Books.
 */
@Singleton
public class BookListEditor extends BaseListEditor<IssueProxy> {
  /**
   * The UiBinder interface.
   */
  interface Binder extends UiBinder<Widget, BookListEditor> {
  }

  private static Binder binder = GWT.create(Binder.class);
  private final BookDataProvider dataProvider;

  @UiField
  public ShowMorePagerPanel pagerPanel;

  @UiField
  SimplePanel simplePanel;

  private final PlaceController placeController;
  private final Provider<org.cloudlet.web.mvp.shared.BasePlace> place;
  private final WavePanel wavePanel;
  private final ReaderFactory f;

  /**
   * Construct a new {@link BookListEditor}.
   */
  @Inject
  BookListEditor(final PlaceController placeController, final Provider<BasePlace> place,
      final ReaderFactory f, final BookDataProvider dataProvider, final WavePanel wavePanel) {
    this.placeController = placeController;
    this.place = place;
    this.f = f;
    this.dataProvider = dataProvider;
    this.wavePanel = wavePanel;
    wavePanel.setContent(binder.createAndBindUi(this));
    initWidget(wavePanel);
    pagerPanel.setIncrementSize(5);
  }

  @Override
  public void onStart(final ActivityState state) {
    final NoSelectionModel<IssueProxy> selectionIssueProxy = new NoSelectionModel<IssueProxy>();

    selectionIssueProxy.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        placeController.goTo(place.get().setPath(IssueEditor.class.getName()).setParameter(
            selectionIssueProxy.getLastSelectedObject().stableId()));
      }
    });

    BasePlace basePlace = (BasePlace) placeController.getWhere();
    final EntityProxyId<CategoryProxy> categoryId = basePlace.getProxyId();

    new BaseReceiver<CategoryProxy>() {

      @Override
      public void onSuccessAndCached(final CategoryProxy response) {
        wavePanel.title().setText(response.getTitle());
      }

      @Override
      public Request<CategoryProxy> provideRequest() {
        return f.find(categoryId);
      }

    }.setKeyForProxy(categoryId).fire();

    CellList<IssueProxy> cellList = new CellList<IssueProxy>(provideCell());
    cellList.setPageSize(5);
    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
    cellList.setSelectionModel(selectionIssueProxy);
    simplePanel.clear();
    this.cellList = cellList;
    super.onStart(state);
    simplePanel.add(pagerPanel);
    pagerPanel.setDisplay(this.cellList);
  }

  @Override
  protected Cell<IssueProxy> provideCell() {
    return new BookProxyCell();
  }

  @Override
  protected AbstractDataProvider<IssueProxy> provideDataProvider() {
    return dataProvider;
  }

  @Override
  protected UiBinder<?, ?> provideUiBinder() {
    return binder;
  }
}
