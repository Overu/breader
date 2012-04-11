package org.cloudlet.web.mvp.shared;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

import org.cloudlet.web.boot.shared.MapBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BaseActivity implements Activity, TakesValue<BasePlace>, HasName {

  private final Logger logger = Logger.getLogger(getClass().getName());
  private BasePlace place;
  private List<Activity> wrappedActivities;
  private final MapBinder<String, IsWidget> isWidgetMapBinder;
  private String name;
  private String viewId;
  private final Provider<Map<String, String[]>> params;
  private Widget widget;

  @Inject
  BaseActivity(final MapBinder<String, IsWidget> isWidgetMapBinder,
      @RequestParameters final Provider<Map<String, String[]>> params) {
    this.isWidgetMapBinder = isWidgetMapBinder;
    this.params = params;
    ViewBundle.INSTANCE.style().ensureInjected();
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
  public String mayStop() {
    if (wrappedActivities == null || wrappedActivities.isEmpty()) {
      return null;
    }
    for (Activity activity : wrappedActivities) {
      if (activity == null) {
        continue;
      }
      String toReturn = activity.mayStop();
      if (toReturn != null) {
        return toReturn;
      }
    }
    return null;
  }

  @Override
  public void onCancel() {
    if (wrappedActivities == null || wrappedActivities.isEmpty()) {
      return;
    }
    for (Activity activity : wrappedActivities) {
      if (activity == null) {
        continue;
      }
      activity.onCancel();
    }
  }

  @Override
  public void onStop() {
    if (wrappedActivities == null || wrappedActivities.isEmpty()) {
      return;
    }
    for (Activity activity : wrappedActivities) {
      if (activity == null) {
        continue;
      }
      activity.onStop();
    }
    widget.removeStyleName(ViewBundle.INSTANCE.style().viewTransition());
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
        widget = result.asWidget();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

          @Override
          public void execute() {
            widget.addStyleName(ViewBundle.INSTANCE.style().viewTransition());
          }
        });
        containerWidget.setWidget(result);
        if (result instanceof Activity) {
          ensureWrappedActivities();
          wrappedActivities.add((Activity) result);
        }
        if (result instanceof ProvideActivities) {
          ensureWrappedActivities();
          wrappedActivities.addAll(((ProvideActivities) result).provideActivities());
        }
        if (wrappedActivities == null || wrappedActivities.isEmpty()) {
          return;
        }
        for (Activity activity : wrappedActivities) {
          if (activity == null) {
            continue;
          }
          activity.start(containerWidget, eventBus);
        }
      }
    });

  }

  private void ensureWrappedActivities() {
    if (wrappedActivities == null) {
      wrappedActivities = new ArrayList<Activity>();
    }
  }
}
