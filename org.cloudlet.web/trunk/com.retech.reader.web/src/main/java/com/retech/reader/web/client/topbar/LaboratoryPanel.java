/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.retech.reader.web.client.topbar;

import com.goodow.web.view.wave.client.WaveTest;
import com.goodow.web.view.wave.client.contact.ContactPanel;
import com.goodow.web.view.wave.client.panel.WavePanel;
import com.goodow.web.view.wave.client.tree.TrangleButtonCell;
import com.goodow.web.view.wave.client.tree.TreeTest;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.retech.reader.web.client.home.BookFlip;

import org.cloudlet.web.mvp.shared.BasePlace;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class LaboratoryPanel extends WavePanel implements Activity {

  interface Binder extends UiBinder<Widget, LaboratoryPanel> {
  }

  interface Bundle extends ClientBundle {

    ImageResource laboratory();

  }

  private static Bundle bunder = GWT.create(Bundle.class);
  private static Binder binder = GWT.create(Binder.class);

  @UiField
  SimplePanel simplePanel;

  private CellList<ImageAndHyperlink> cellList;
  private ListDataProvider<ImageAndHyperlink> listDataProvider =
      new ListDataProvider<ImageAndHyperlink>();
  private CompositeCell<ImageAndHyperlink> compositeCell;

  @Inject
  LaboratoryPanel(final CellList.Resources resource, final LaboratoryCell cell,
      final PlaceController placeController, final Provider<BasePlace> base) {
    this.setWaveContent(binder.createAndBindUi(this));

    List<ImageAndHyperlink> list = listDataProvider.getList();
    ImageAndHyperlink touch =
        new ImageAndHyperlink(bunder.laboratory(), new Hyperlink("拖动实验", WaveTest.class.getName()));
    list.add(touch);

    ImageAndHyperlink flip =
        new ImageAndHyperlink(bunder.laboratory(), new Hyperlink("3D滚动", BookFlip.class.getName()));
    list.add(flip);

    ImageAndHyperlink contact =
        new ImageAndHyperlink(bunder.laboratory(), new Hyperlink("分享与即时聊天", ContactPanel.class
            .getName()));
    list.add(contact);

    ImageAndHyperlink treeTest =
        new ImageAndHyperlink(bunder.laboratory(), new Hyperlink("TreeTest", TreeTest.class
            .getName()));
    list.add(treeTest);

    final SingleSelectionModel<ImageAndHyperlink> selectionModel =
        new SingleSelectionModel<ImageAndHyperlink>();
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        placeController.goTo(base.get().setPath(
            selectionModel.getSelectedObject().getLink().getTargetHistoryToken()));
      }
    });

    List<HasCell<ImageAndHyperlink, ?>> hasCells = new ArrayList<HasCell<ImageAndHyperlink, ?>>();
    hasCells.add(new HasCell<ImageAndHyperlink, ImageAndHyperlink>() {

      @Override
      public Cell<ImageAndHyperlink> getCell() {
        return cell;
      }

      @Override
      public FieldUpdater<ImageAndHyperlink, ImageAndHyperlink> getFieldUpdater() {
        return null;
      }

      @Override
      public ImageAndHyperlink getValue(final ImageAndHyperlink object) {
        return object;
      }
    });

    hasCells.add(new HasCell<ImageAndHyperlink, ImageAndHyperlink>() {

      private TrangleButtonCell<ImageAndHyperlink> tbc = new TrangleButtonCell<ImageAndHyperlink>();

      @Override
      public Cell<ImageAndHyperlink> getCell() {
        return tbc;
      }

      @Override
      public FieldUpdater<ImageAndHyperlink, ImageAndHyperlink> getFieldUpdater() {
        return null;
      }

      @Override
      public ImageAndHyperlink getValue(final ImageAndHyperlink object) {
        return object;
      }
    });

    compositeCell = new CompositeCell<ImageAndHyperlink>(hasCells) {

      @Override
      public void render(final com.google.gwt.cell.client.Cell.Context context,
          final ImageAndHyperlink value, final SafeHtmlBuilder sb) {
        super.render(context, value, sb);
      }

      @Override
      protected Element getContainerElement(final Element parent) {
        return parent;
      }

      @Override
      protected <X> void render(final com.google.gwt.cell.client.Cell.Context context,
          final ImageAndHyperlink value, final SafeHtmlBuilder sb,
          final HasCell<ImageAndHyperlink, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        sb.append(SafeHtmlUtils.fromTrustedString("<div>"));
        cell.render(context, hasCell.getValue(value), sb);
        sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
      }
    };

    cellList = new CellList<ImageAndHyperlink>(compositeCell, resource);
    cellList.setSelectionModel(selectionModel);
    simplePanel.add(cellList);
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
    cellList.setFocus(false);
    if (!listDataProvider.getDataDisplays().contains(cellList)) {
      listDataProvider.addDataDisplay(cellList);
    }
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    if (listDataProvider.getDataDisplays().contains(cellList)) {
      listDataProvider.removeDataDisplay(cellList);
    }
  }
}
