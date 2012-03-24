package org.cloudlet.web.mvp.shared;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

import org.cloudlet.web.boot.shared.MapBinder;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BaseActivity extends AbstractActivity implements TakesValue<BasePlace>,
    ActivityState, HasName {

  private final Logger logger = Logger.getLogger(getClass().getName());
  private BasePlace place;

  private final MapBinder<String, IsWidget> isWidgetMapBinder;
  private String name;
  private String viewId;
  private final Provider<Map<String, String[]>> params;
  private EventBus eventBus;

  @Inject
  BaseActivity(final MapBinder<String, IsWidget> isWidgetMapBinder,
      @RequestParameters final Provider<Map<String, String[]>> params) {
    this.isWidgetMapBinder = isWidgetMapBinder;
    this.params = params;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BaseActivity other = (BaseActivity) obj;
    if (place == null) {
      if (other.place != null) {
        return false;
      }
    } else if (!place.equals(other.place)) {
      return false;
    }
    return true;
  }

  @Override
  public EventBus getEventBus() {
    return eventBus;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public BasePlace getValue() {
    return place;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((place == null) ? 0 : place.hashCode());
    return result;
  }

  @Override
  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public void setValue(final BasePlace place) {
    this.place = place;
  }

  @Override
  public void start(final AcceptsOneWidget containerWidget,
      final com.google.gwt.event.shared.EventBus eventBus) {
    this.eventBus = eventBus;
    AsyncProvider<IsWidget> asyncProvider = null;
    String[] values = params.get().get(name);
    if (values != null) {
      viewId = values[0];
    } else if (name == null || name.isEmpty() || Default.KEY.equals(name)) {
      viewId = place.getPath();
    } else {
      viewId = name;
    }

    Class<? extends EntityProxy> proxyClass = place.getProxyClass();
    if (proxyClass != null) {
      viewId = proxyClass.getName();
    }
    asyncProvider = isWidgetMapBinder.getAsyncProvider(viewId);
    if (asyncProvider == null) {
      containerWidget.setWidget(null);
      return;
    }

    asyncProvider.get(new AsyncCallback<IsWidget>() {
      @Override
      public void onFailure(final Throwable caught) {
        if (LogConfiguration.loggingIsEnabled()) {
          logger.log(Level.WARNING, "加载" + viewId + "失败. 请检查网络连接, 并刷新后重试", caught);
        }
      }

      @Override
      public void onSuccess(final IsWidget result) {
        if (result instanceof ActivityAware) {
          ((ActivityAware) result).onStart(BaseActivity.this);
        }
        containerWidget.setWidget(result);
      }
    });

  }

}
