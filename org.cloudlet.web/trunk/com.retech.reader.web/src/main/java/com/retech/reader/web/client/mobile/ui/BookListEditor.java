package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.WavePanel;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
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

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

/**
 * View used to display the list of Books.
 */
@Singleton
public class BookListEditor extends WavePanel implements ActivityAware {
  /**
   * The UiBinder interface.
   */
  // interface Binder extends UiBinder<Widget, BookListEditor> {
  // }

  // private static Binder binder = GWT.create(Binder.class);

  // @UiField
  // public ShowMorePagerPanel pagerPanel;
  //
  // @UiField
  // SimplePanel simplePanel;

  private final PlaceController placeController;
  private final Provider<org.cloudlet.web.mvp.shared.BasePlace> place;
  private final ReaderFactory f;
  private final BookDataProvider dataProvider;
  private CellList<IssueProxy> cellList;
  private NoSelectionModel<IssueProxy> selectionModel;
  private final BookProxyCell cell;
  private final Resources resources;

  /**
   * Construct a new {@link BookListEditor}.
   */
  @Inject
  BookListEditor(final PlaceController placeController, final Provider<BasePlace> place,
      final ReaderFactory f, final BookDataProvider dataProvider, final BookProxyCell cell,
      final CellList.Resources resources) {
    this.placeController = placeController;
    this.place = place;
    this.f = f;
    this.dataProvider = dataProvider;
    this.cell = cell;
    this.resources = resources;
  }

  @Override
  public void onStart(final ActivityState state) {
    selectionModel = new NoSelectionModel<IssueProxy>();
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        placeController.goTo(place.get().setPath(IssueEditor.class.getName()).setParameter(
            selectionModel.getLastSelectedObject().stableId()));
      }
    });

    cellList = new CellList<IssueProxy>(cell, resources);
    cellList.setSelectionModel(selectionModel);
    this.setContent(cellList);

    if (!dataProvider.getDataDisplays().contains(cellList)) {
      dataProvider.addDataDisplay(cellList);
    }

    BasePlace basePlace = (BasePlace) placeController.getWhere();
    final EntityProxyId<CategoryProxy> categoryId = basePlace.getProxyId();
    new BaseReceiver<CategoryProxy>() {

      @Override
      public void onSuccessAndCached(final CategoryProxy response) {
        BookListEditor.this.title().setText(response.getTitle());
      }

      @Override
      public Request<CategoryProxy> provideRequest() {
        return f.find(categoryId);
      }

    }.setKeyForProxy(categoryId).fire();
  }

  @Override
  protected void onUnload() {
    if (dataProvider.getDataDisplays().contains(cellList)) {
      dataProvider.removeDataDisplay(cellList);
    }
    super.onUnload();
    this.remove(1);
  }
}
