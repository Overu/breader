package org.cloudlet.web.security.shared;

import com.google.gwt.event.shared.EventHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.Event;

import org.cloudlet.web.security.shared.AuthEvent.Handler;

import java.io.Serializable;
import java.util.logging.Logger;

class AuthEvent extends Event<Handler> implements Serializable {

  public interface Handler extends EventHandler {

    void onAuthFailure(AuthEvent event);

    void onAuthSuccess(AuthEvent event);
  }

  public static transient final Type<Handler> TYPE = new Type<Handler>();

  private String payload;

  private transient final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  AuthEvent() {
  }

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public void setRootNodePayload(final String rootPayload) {
    this.payload = rootPayload;
  }

  @Override
  protected void dispatch(final Handler handler) {
    // check tree note is loaded?
    if (true) {
      handler.onAuthSuccess(this);
    } else {
      handler.onAuthFailure(this);
    }
  }
}