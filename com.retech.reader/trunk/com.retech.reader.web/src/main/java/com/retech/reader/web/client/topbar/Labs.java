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

import com.goodow.wave.client.account.ContactPanel;
import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.blip.BlipTest;
import com.goodow.wave.client.wavepanel.blip.NestedBlipTest;
import com.goodow.wave.client.wavepanel.blip.SetColor;
import com.goodow.wave.client.wavepanel.blip.TrangleButtonCell;
import com.goodow.wave.client.wavepanel.blip.TreeTest;
import com.goodow.wave.client.wavepanel.blip.WaveTest;
import com.goodow.wave.client.wavepanel.title.WaveTitleResources;
import com.goodow.wave.client.widget.button.icon.IconButtonTemplate;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.retech.reader.web.client.home.BookFlip;
import com.retech.reader.web.client.home.SearchPanel;

import org.cloudlet.web.mvp.shared.BasePlace;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class Labs extends WavePanel implements Activity {

  interface Binder extends UiBinder<Widget, Labs> {
  }

  interface Bundle extends ClientBundle {

    ImageResource laboratory();

  }

  private static Bundle bunder = GWT.create(Bundle.class);
  private static Binder binder = GWT.create(Binder.class);

  @UiField
  SimplePanel simplePanel;
  @UiField
  IconButtonTemplate minimize;
  @UiField
  SetColor setColor;

  private CellList<LabsIconDecorator> cellList;
  private ListDataProvider<LabsIconDecorator> listDataProvider =
      new ListDataProvider<LabsIconDecorator>();
  private CompositeCell<LabsIconDecorator> compositeCell;

  private Element lastElm;

  @Inject
  Labs(final CellList.Resources resource, final PlaceController placeController,
      final Provider<BasePlace> base) {
    this.setWaveContent(binder.createAndBindUi(this));

    setColor.setChangeElm(simplePanel.getElement());

    minimize.setIconElement(AbstractImagePrototype.create(
        WaveTitleResources.image().waveTitleMinimize()).createElement());

    // add Data
    List<LabsIconDecorator> list = listDataProvider.getList();
    LabsIconDecorator touch = new LabsIconDecorator(bunder.laboratory(), "拖动实验", WaveTest.class);
    list.add(touch);

    LabsIconDecorator flip = new LabsIconDecorator(bunder.laboratory(), "3D滚动", BookFlip.class);
    list.add(flip);

    LabsIconDecorator contact =
        new LabsIconDecorator(bunder.laboratory(), "分享与即时聊天", ContactPanel.class);
    list.add(contact);

    LabsIconDecorator treeTest =
        new LabsIconDecorator(bunder.laboratory(), "TreeTest", TreeTest.class);
    list.add(treeTest);

    LabsIconDecorator search = new LabsIconDecorator(bunder.laboratory(), "搜索", SearchPanel.class);
    list.add(search);

    LabsIconDecorator blipTest =
        new LabsIconDecorator(bunder.laboratory(), "BlipTest", BlipTest.class);
    list.add(blipTest);
    LabsIconDecorator blipTree =
        new LabsIconDecorator(bunder.laboratory(), "NestedBlipTest", NestedBlipTest.class);
    list.add(blipTree);

    // add cell
    List<HasCell<LabsIconDecorator, ?>> hasCells = new ArrayList<HasCell<LabsIconDecorator, ?>>();
    hasCells.add(new HasCell<LabsIconDecorator, LabsIconDecorator>() {

      LabsIconDecoratorCell cell = new LabsIconDecoratorCell(
          new LabsIconDecoratorCell.Delegate<LabsIconDecorator>() {

            @Override
            public void execute(final LabsIconDecorator object) {
              placeController.goTo(base.get().setPath(object.getClassName().getName()));
            }
          });

      @Override
      public Cell<LabsIconDecorator> getCell() {
        return cell;
      }

      @Override
      public FieldUpdater<LabsIconDecorator, LabsIconDecorator> getFieldUpdater() {
        return null;
      }

      @Override
      public LabsIconDecorator getValue(final LabsIconDecorator object) {
        return object;
      }
    });

    hasCells.add(new HasCell<LabsIconDecorator, LabsIconDecorator>() {

      private TrangleButtonCell<LabsIconDecorator> tbc = new TrangleButtonCell<LabsIconDecorator>();

      @Override
      public Cell<LabsIconDecorator> getCell() {
        return tbc;
      }

      @Override
      public FieldUpdater<LabsIconDecorator, LabsIconDecorator> getFieldUpdater() {
        return null;
      }

      @Override
      public LabsIconDecorator getValue(final LabsIconDecorator object) {
        return object;
      }
    });

    compositeCell = new CompositeCell<LabsIconDecorator>(hasCells) {

      @Override
      public void render(final com.google.gwt.cell.client.Cell.Context context,
          final LabsIconDecorator value, final SafeHtmlBuilder sb) {
        super.render(context, value, sb);
      }

      @Override
      protected Element getContainerElement(final Element parent) {
        return parent;
      }

      @Override
      protected <X> void render(final com.google.gwt.cell.client.Cell.Context context,
          final LabsIconDecorator value, final SafeHtmlBuilder sb,
          final HasCell<LabsIconDecorator, X> hasCell) {
        Cell<X> cell = hasCell.getCell();
        cell.render(context, hasCell.getValue(value), sb);
      }
    };

    // add cellList
    cellList = new CellList<LabsIconDecorator>(compositeCell, resource);

    // add cellPreviewHanler
    cellList.addCellPreviewHandler(new CellPreviewEvent.Handler<LabsIconDecorator>() {

      @Override
      public void onCellPreview(final CellPreviewEvent<LabsIconDecorator> event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        boolean isClick = nativeEvent.getType().equals(BrowserEvents.CLICK);
        if (isClick) {
          Element clickelm = cellList.getRowElement(event.getIndex());
          Element eventTarget = Element.as(nativeEvent.getEventTarget());
          if (clickelm.getFirstChildElement() == eventTarget) {
            if (Labs.this.lastElm == null) {
              Labs.this.lastElm = clickelm;
            }
            if (Labs.this.lastElm != clickelm) {
              Labs.this.lastElm.removeClassName(LabsResources.css().cellListSelectionItem());
              clickelm.addClassName(LabsResources.css().cellListSelectionItem());
              Labs.this.lastElm = clickelm;
            } else if (Labs.this.lastElm == clickelm) {
              clickelm.addClassName(LabsResources.css().cellListSelectionItem());
            }
          }
        }
      }
    });
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
    if (!listDataProvider.getDataDisplays().contains(cellList)) {
      listDataProvider.addDataDisplay(cellList);
    }
    if (lastElm != null) {
      lastElm.addClassName(LabsResources.css().cellListSelectionItem());
    }
  }

  @Override
  protected void onUnload() {
    super.onUnload();
    // if (listDataProvider.getDataDisplays().contains(cellList)) {
    // listDataProvider.removeDataDisplay(cellList);
    // }
  }
}
