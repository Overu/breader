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
package com.goodow.web.ui.client.ioc;

import com.goodow.web.logging.client.loading.LoadingIndicator;
import com.goodow.web.ui.client.footer.FooterUi;
import com.goodow.web.ui.client.nav.NavUi;
import com.goodow.web.ui.client.nav.TreeNodeDataProvider;
import com.goodow.web.ui.client.search.SearchUi;
import com.goodow.web.ui.client.shell.ShellUi;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import org.cloudlet.web.boot.shared.MapBinder;
import org.cloudlet.web.mvp.shared.layout.Footer;
import org.cloudlet.web.mvp.shared.layout.Nav;
import org.cloudlet.web.mvp.shared.layout.Search;
import org.cloudlet.web.mvp.shared.tree.TreeNodeProxy;

import java.util.logging.Logger;

public final class UiGinModule extends AbstractGinModule {

  @Singleton
  public static class Binder {
  }

  @Singleton
  public static class BinderProvider implements Provider<Binder> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Inject
    LoadingIndicator loadingIndicator;
    @Inject
    private MapBinder<String, IsWidget> isWidgetMapBinder;
    @Inject
    private AsyncProvider<NavUi> defaultNavUi;
    @Inject
    private AsyncProvider<SearchUi> defaultSearchUi;
    @Inject
    private AsyncProvider<FooterUi> defaultFooterUi;

    @Inject
    private MapBinder<String, Provider<? extends AbstractDataProvider<?>>> dataProviderMapBinder;
    @Inject
    private Provider<TreeNodeDataProvider> defaultTreeNodeDataProvider;

    // @Inject
    // private AsyncProvider<ShellUi> defaultShellProvider;

    @Override
    public Binder get() {
      logger.finest("EagerSingleton start");
      RootPanel.get().getElement().appendChild(loadingIndicator.getElement());

      // 注册默认视图
      isWidgetMapBinder.addBinding(Search.KEY).toAsyncProvider(defaultSearchUi);
      isWidgetMapBinder.addBinding(Nav.KEY).toAsyncProvider(defaultNavUi);
      isWidgetMapBinder.addBinding(Footer.KEY).toAsyncProvider(defaultFooterUi);
      // isWidgetMapBinder.addBinding(ShellUi.class.getName()).toAsyncProvider(defaultShellProvider);

      // 注册DataProvider
      dataProviderMapBinder.addBinding(TreeNodeDataProvider.class.getName()).toInstance(
          defaultTreeNodeDataProvider);

      logger.finest("EagerSingleton end");
      return null;
    }
  }

  @Override
  protected void configure() {
    bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();

    bind(new TypeLiteral<MapBinder<String, Provider<? extends AbstractDataProvider<?>>>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<SingleSelectionModel<TreeNodeProxy>>() {
    }).in(Singleton.class);

    bind(ShellUi.class).asEagerSingleton();
  }
}
