package org.cloudlet.web.mvp.shared;

import com.google.web.bindery.event.shared.EventBus;

public interface ActivityState {
  EventBus getEventBus();

  String getName();
}
