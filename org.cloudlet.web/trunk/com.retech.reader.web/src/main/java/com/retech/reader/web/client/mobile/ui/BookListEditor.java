package com.retech.reader.web.client.mobile.ui;

import com.goodow.web.view.wave.client.panel.WavePanel;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.proxy.CategoryProxy;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.BookDataProvider;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

/**
 * View used to display the list of Books.
 */
@Singleton
public class BookListEditor extends WavePanel implements Activity {
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

    cellList.addHandler(new ValueChangeHandler<IssueProxy>() {

      @Override
      public void onValueChange(final ValueChangeEvent<IssueProxy> event) {
        CellList<IssueProxy> cellListProxy = (CellList<IssueProxy>) event.getSource();
        List<IssueProxy> proxys = cellListProxy.getVisibleItems();
        int i = 0;
        for (final IssueProxy proxy : proxys) {
          final Element elm = cellListProxy.getRowElement(i);

          new BaseReceiver<ResourceProxy>() {

            @Override
            public void onSuccessAndCached(final ResourceProxy resourceProxy) {
              Element imageElm =
                  elm.getFirstChildElement().getFirstChildElement().getFirstChildElement()
                      .getFirstChildElement();
              imageElm.setInnerHTML("<img width='60px' height='60px' src='data:"
                  + resourceProxy.getMimeType().getType() + ";base64,"
                  + resourceProxy.getDataString() + "'/>");
            }

            @Override
            public Request<ResourceProxy> provideRequest() {
              return f.resource().getImage(proxy);
            }
          }.setKeyForProxy(proxy.stableId(), ResourceProxy.class.getName()).fire();

          i++;
        }
      }
    }, ValueChangeEvent.getType());

    this.setWaveContent(cellList);

    if (!dataProvider.getDataDisplays().contains(cellList)) {
      dataProvider.addDataDisplay(cellList);
    }

    BasePlace basePlace = (BasePlace) placeController.getWhere();
    final EntityProxyId<CategoryProxy> categoryId = basePlace.getProxyId();
    new BaseReceiver<CategoryProxy>() {

      @Override
      public void onSuccessAndCached(final CategoryProxy response) {
        BookListEditor.this.getWaveTitle().setText(response.getTitle());
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
