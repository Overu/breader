package org.cloudlet.web.security.shared;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

import org.cloudlet.web.security.shared.AuthRequestEvent.Handler;

import java.io.Serializable;
import java.util.logging.Logger;

public class AuthRequestEvent extends Event<Handler> implements Serializable {

  public interface Handler extends EventHandler {
    void onAuthRequest(AuthRequestEvent event);
  }

  public static transient final Type<Handler> TYPE = new Type<Handler>();

  private String principal;

  private String credentials;

  private transient final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  public Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public String getCredentials() {
    return credentials;
  }

  public String getPrincipal() {
    return principal;
  }

  public AuthRequestEvent setCredentials(String credentials) {
    this.credentials = credentials;
    return this;
  }

  public AuthRequestEvent setPrincipal(String principal) {
    this.principal = principal;
    return this;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onAuthRequest(this);
  }
}