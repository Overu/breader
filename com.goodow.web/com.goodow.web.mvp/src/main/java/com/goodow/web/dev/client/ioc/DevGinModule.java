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
package com.goodow.web.dev.client.ioc;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.dev.client.ui.ShellUiDev;
import com.goodow.web.dev.client.ui.TreeNodeEditor;
import com.goodow.web.dev.client.ui.TreeNodeListView;
import com.goodow.web.mvp.shared.tree.TreeNodeProxy;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


import java.util.logging.Logger;

public final class DevGinModule extends AbstractGinModule {

  @Singleton
  public static class Binder {
  }
  @Singleton
  public static class BinderProvider implements Provider<Binder> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Inject
    private MapBinder<String, IsWidget> isWidgetMapBinder;
    @Inject
    private AsyncProvider<TreeNodeEditor> treeNodeEditor;
    @Inject
    private AsyncProvider<TreeNodeListView> treeNodeListView;

    @Override
    public Binder get() {
      logger.finest("EagerSingleton begin");

      isWidgetMapBinder.bind(TreeNodeListView.class.getName()).toAsyncProvider(
          treeNodeListView);
      isWidgetMapBinder.bind(TreeNodeProxy.class.getName()).toAsyncProvider(treeNodeEditor);

      logger.finest("EagerSingleton end");
      return null;
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();
    bind(ShellUiDev.class).asEagerSingleton();
  }

}
