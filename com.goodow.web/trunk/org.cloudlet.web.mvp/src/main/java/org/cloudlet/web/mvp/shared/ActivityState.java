package org.cloudlet.web.mvp.shared;

import com.google.web.bindery.event.shared.EventBus;

@Deprecated
public interface ActivityState {
  EventBus getEventBus();

  String getName();
}
