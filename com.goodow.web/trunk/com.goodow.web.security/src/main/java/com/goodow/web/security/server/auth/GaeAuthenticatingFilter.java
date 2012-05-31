package com.goodow.web.security.server.auth;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * An {@link AuthenticatingFilter} which uses UserService to control the authentication process
 */
public class GaeAuthenticatingFilter extends AuthenticatingFilter {
  public GaeAuthenticatingFilter() {
    setLoginUrl(null);
  }

  @Override
  protected AuthenticationToken createToken(final ServletRequest request,
      final ServletResponse response) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    return createToken(user.getUserId(), null, request, response);
  }

  @Override
  protected boolean isRememberMe(final ServletRequest request) {
    return true;
  }

  @Override
  protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response)
      throws Exception {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    if (user == null) {
      saveRequest(request);

      String requestURI = WebUtils.getRequestUri(WebUtils.toHttp(request));

      String loginUrl = this.getLoginUrl();
      if (loginUrl == null) {
        loginUrl = userService.createLoginURL(requestURI);
        WebUtils.issueRedirect(request, response, loginUrl);
      } else {
        request.setAttribute("requestURI", requestURI);
        request.getRequestDispatcher(loginUrl).forward(request, response);
      }

      return false;
    } else {
      // Perform the internal login process
      boolean result = executeLogin(request, response);
      return result;
    }
  }
}