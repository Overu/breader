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
package com.goodow.web.dev.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;
import org.cloudlet.web.mvp.shared.BasePlace;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeContext;
import org.cloudlet.web.mvp.shared.tree.rpc.TreeNodeFactory;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

public class TreeNodeEditor extends Composite implements Editor<TreeNodeProxy>, ActivityAware {

  interface Binder extends UiBinder<Widget, TreeNodeEditor> {
  }

  interface Driver extends RequestFactoryEditorDriver<TreeNodeProxy, TreeNodeEditor> {
  }

  private static Binder binder = GWT.create(Binder.class);
  Driver driver = GWT.create(Driver.class);

  private final TreeNodeFactory f;

  @UiField
  ValueBoxEditorDecorator<String> path;
  @UiField
  ValueBoxEditorDecorator<String> name;
  @UiField
  ValueBoxEditorDecorator<String> type;

  @UiField
  Button save;
  private final Provider<Map<String, String[]>> params;
  private final Provider<BasePlace> place;
  private ActivityState state;
  private final PlaceController placeController;

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  TreeNodeEditor(final TreeNodeFactory f,
      @RequestParameters final Provider<Map<String, String[]>> params,
      final Provider<BasePlace> place, final PlaceController placeController) {
    this.place = place;
    this.placeController = placeController;
    this.f = f;
    this.params = params;
    initWidget(binder.createAndBindUi(this));
  }

  @Override
  public void onStart(final ActivityState state) {
    this.state = state;
    driver.initialize(state.getEventBus(), f, this);
    String[] id = params.get().get(state.getName());
    // 编辑
    try {
      EntityProxyId<TreeNodeProxy> proxyId = f.getProxyId(id[0]);
      f.find(proxyId).to(new Receiver<TreeNodeProxy>() {

        @Override
        public void onSuccess(final TreeNodeProxy response) {
          edit(response);
        }
      }).fire();

    } catch (RuntimeException e) {
      // 新建
      edit(null);
    }
  }

  void edit(TreeNodeProxy proxy) {
    TreeNodeContext ctx = f.treeNodeContext();
    if (proxy == null) {
      proxy = ctx.create(TreeNodeProxy.class);
    }
    driver.edit(proxy, ctx);
    ctx.put(proxy);
  }

  @UiHandler("save")
  void onSaveClick(final ClickEvent event) {
    RequestContext req = driver.flush();
    req.fire(new Receiver<Void>() {
      @Override
      public void onConstraintViolation(final Set<ConstraintViolation<?>> violations) {
        driver.setConstraintViolations(violations);
      }

      @Override
      public void onSuccess(final Void response) {
        BasePlace newPlace =
            place.get().setParameter(state.getName(), TreeNodeListView.class.getName());
        placeController.goTo(newPlace);
        logger.info("创建成功");
      }
    });
  }
}
