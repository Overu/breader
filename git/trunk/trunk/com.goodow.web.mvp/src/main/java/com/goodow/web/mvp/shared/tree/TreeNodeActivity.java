package com.goodow.web.mvp.shared.tree;

import com.goodow.wave.bootstrap.shared.MapBinder;
import com.goodow.web.mvp.shared.ActivityAware;
import com.goodow.web.mvp.shared.Default;
import com.goodow.web.mvp.shared.tree.event.RefreshEvent;
import com.goodow.web.mvp.shared.tree.event.RefreshEvent.Handler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;


import java.util.logging.Level;
import java.util.logging.Logger;

public final class TreeNodeActivity extends AbstractActivity implements TakesValue<TreeNodePlace> {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private TreeNodePlace place;

  private final MapBinder<String, IsWidget> isWidgetMapBinder;

  private boolean started;

  private final TreeNodeUtil util;

  private final TreeNodeProxy root;

  @Inject
  TreeNodeActivity(MapBinder<String, IsWidget> isWidgetMapBinder, TreeNodeUtil util,
      @Default TreeNodeProxy root) {
    this.isWidgetMapBinder = isWidgetMapBinder;
    this.util = util;
    this.root = root;
  }

  @Override
  public TreeNodePlace getValue() {
    return place;
  }

  @Override
  public void setValue(TreeNodePlace place) {
    this.place = place;
  }

  @Override
  public void start(final AcceptsOneWidget containerWidget, final EventBus eventBus) {
    startSync(containerWidget, eventBus);

    Handler<TreeNodeProxy> handler = new RefreshEvent.Handler<TreeNodeProxy>() {

      @Override
      public void onRefresh(RefreshEvent<TreeNodeProxy> event) {
        TreeNodeProxy parent = event.getValue();
        util.find(root, parent.getPath()).setChildren(parent.getChildren());
        logger.finest(root.toString());

        startSync(containerWidget, eventBus);
      }
    };
    eventBus.addHandler(RefreshEvent.TYPE, handler);
  }

  private boolean startSync(final AcceptsOneWidget containerWidget, final EventBus eventBus) {
    if (started) {
      return true;
    }

    TreeNodeProxy node = place.getTreeNode();
    if (node == null) {
      return false;
    }
    String clazz = node.getType();
    if (clazz == null) {
      return false;
    }

    final String title = node.getName() == null ? node.getPath() : node.getName();
    Window.setTitle(title);

    AsyncProvider<IsWidget> asyncProvider = isWidgetMapBinder.getAsyncProvider(clazz);
    if (asyncProvider == null) {
      containerWidget.setWidget(new Label("该功能尚未实现:" + title));

      return started = true;
    }

    asyncProvider.get(new AsyncCallback<IsWidget>() {
      @Override
      public void onFailure(Throwable caught) {
        if (LogConfiguration.loggingIsEnabled()) {
          logger.log(Level.WARNING, "加载" + title + "失败. 请检查网络连接, 并刷新后重试", caught);
        }
      }

      @Override
      public void onSuccess(IsWidget result) {
        if (result instanceof ActivityAware) {
          // ((ActivityAware) result).onStart(eventBus);
        }
        containerWidget.setWidget(result);
      }
    });
    return started = true;
  }
}
