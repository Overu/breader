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
package com.retech.reader.web.client.home;

import com.goodow.wave.client.search.SearchBox;
import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.wave.client.wavepanel.WavePanelResources;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.client.mobile.ui.BookProxyCell;
import com.retech.reader.web.client.mobile.ui.IssueEditor;
import com.retech.reader.web.shared.proxy.IssueProxy;
import com.retech.reader.web.shared.proxy.ResourceProxy;
import com.retech.reader.web.shared.rpc.BookDataProvider;
import com.retech.reader.web.shared.rpc.ReaderFactory;

import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.service.shared.rpc.BaseReceiver;

import java.util.List;

@Singleton
public class SearchPanel extends WavePanel implements Activity {

  public static String SEARCH = "search_issue";

  private final SearchBox searchBox;
  private final BookDataProvider bookDataProvider;
  private final BookProxyCell cell;
  private final Resources resources;
  private CellList<IssueProxy> cellList;
  private final ReaderFactory f;
  private final NoSelectionModel<IssueProxy> selectModel;

  private final PlaceController placeController;

  private final Provider<BasePlace> base;

  @Inject
  SearchPanel(final SearchBox searchBox, final BookDataProvider bookDataProvider,
      final BookProxyCell cell, final CellList.Resources resources, final ReaderFactory f,
      final NoSelectionModel<IssueProxy> selectModel, final PlaceController placeController,
      final Provider<BasePlace> base) {
    this.searchBox = searchBox;
    this.bookDataProvider = bookDataProvider;
    this.cell = cell;
    this.resources = resources;
    this.f = f;
    this.selectModel = selectModel;
    this.placeController = placeController;
    this.base = base;
    this.getWaveTitle().setText("搜索");
    searchBox.addStyleName(WavePanelResources.css().waveHeader());
    searchBox.getElement().getStyle().setProperty("padding", "16px 5px 16px 6px");
    searchBox.getTextBox().getElement().setAttribute("placeholder", "搜索");
    this.add(searchBox);

    FlowPanel toDo = new FlowPanel();
    toDo.addStyleName(WavePanelResources.css().waveWarning());
    toDo.add(new HTML("<b>已完成：<b>"));
    toDo.add(new Label("2.1 静态界面部分展现"));
    toDo.add(new HTML("<br>"));
    toDo.add(new HTML("<b>待实现：<b>"));
    toDo.add(new Label("2.2 静态界面呈现（中）"));
    toDo.add(new Label("2.3 搜索功能（中）"));
    toDo.add(new Label("2.4 搜索关键字自动建议（中）"));
    toDo.add(new Label("2.5 全文检索（难）"));
    add(toDo);
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

    selectModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

      @Override
      public void onSelectionChange(final SelectionChangeEvent event) {
        placeController.goTo(base.get().setPath(IssueEditor.class.getName()).setParameter(
            selectModel.getLastSelectedObject().stableId()));
      }
    });

    cellList = new CellList<IssueProxy>(cell, resources);
    cellList.setSelectionModel(selectModel);

    cellList.addHandler(new LoadingStateChangeEvent.Handler() {

      @Override
      public void onLoadingStateChanged(final LoadingStateChangeEvent event) {
        event.getLoadingState();
        CellList cellListProxy = (CellList) event.getSource();
        cellListProxy.getVisibleItems();
      }
    }, LoadingStateChangeEvent.TYPE);
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

    if (!bookDataProvider.getDataDisplays().contains(cellList)) {
      bookDataProvider.addDataDisplay(cellList);
    }
  }

  @Override
  protected void onUnload() {
    if (bookDataProvider.getDataDisplays().contains(cellList)) {
      bookDataProvider.removeDataDisplay(cellList);
    }
    super.onUnload();
    remove(3);
  }
}
