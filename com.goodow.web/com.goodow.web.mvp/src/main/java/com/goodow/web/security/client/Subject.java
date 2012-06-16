package com.goodow.web.security.client;

import com.goodow.web.security.shared.Auth;
import com.goodow.web.security.shared.AuthRequestEvent;

import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.apache.shiro.authz.AuthorizationException;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;

@Singleton
public final class Subject {

  private boolean authenticated;
  // private String principal;
  private boolean remembered;
  /**
   * The default name of the underlying rememberMe cookie which is {@code rememberMe}.
   */
  public static final String DEFAULT_REMEMBER_ME_COOKIE_NAME = "rememberMe";
  public static final String DEFAULT_AUTHENTICATED_COOKIE_NAME = "authenticated";
  private transient final Logger logger = Logger.getLogger(getClass().getName());
  private final EventBus eventBus;
  private final Provider<AuthRequestEvent> authRequestEvent;

  @Inject
  Subject(final EventBus eventBus, final Provider<AuthRequestEvent> authRequestEvent) {
    Configuration cfg = null;
    this.eventBus = eventBus;
    this.authRequestEvent = authRequestEvent;
    authenticated = "true".equalsIgnoreCase(Cookies.getCookie(DEFAULT_AUTHENTICATED_COOKIE_NAME));
    remembered = !isAuthenticated() && Cookies.getCookie(DEFAULT_REMEMBER_ME_COOKIE_NAME) != null;
  }

  public void checkPermission(final String token) throws AuthorizationException {
    if (isAuthenticated() || isRemembered()) {
      return;
    }
    if (token.contains(":" + Auth.ANON_PLACE_PREFIX)) {
      return;
    }
    logger.info("已登出, 请重新登录");
    throw new AuthorizationException("未授权");
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public boolean isRemembered() {
    return remembered && !isAuthenticated();
  }

  public void logout() {
    setAuthenticated(false);
    setRemembered(false);
  }

  public void setAuthenticated(final boolean authenticated) {
    logger.finest("set authenticated to " + authenticated);
    this.authenticated = authenticated;
    if (!authenticated) {
      Cookies.removeCookie(DEFAULT_AUTHENTICATED_COOKIE_NAME);
    } else {
      Cookies.setCookie(DEFAULT_AUTHENTICATED_COOKIE_NAME, "true");
    }
  }

  public void setRemembered(final boolean remembered) {
    logger.finest("set remembered to " + remembered);
    this.remembered = remembered;
    if (!remembered) {
      Cookies.removeCookie(DEFAULT_REMEMBER_ME_COOKIE_NAME);
    }
  }

  private void login(AuthRequestEvent authRequestEvent) {
    logger.info("登录中...");
    if (authRequestEvent == null) {
      authRequestEvent = this.authRequestEvent.get();
    }
    eventBus.fireEvent(authRequestEvent);
  }

}
