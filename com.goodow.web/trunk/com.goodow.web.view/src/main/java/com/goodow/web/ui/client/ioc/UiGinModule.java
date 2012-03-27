package com.goodow.web.ui.client.ioc;

import com.goodow.web.ui.client.footer.FooterUi;
import com.goodow.web.ui.client.nav.NavUi;
import com.goodow.web.ui.client.nav.TreeNodeDataProvider;
import com.goodow.web.ui.client.search.SearchUi;
import com.goodow.web.ui.client.shell.ShellUi;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import org.cloudlet.web.boot.shared.MapBinder;
import org.cloudlet.web.logging.client.loading.LoadingIndicator;
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
      RootLayoutPanel.get().getElement().appendChild(loadingIndicator.getElement());

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
