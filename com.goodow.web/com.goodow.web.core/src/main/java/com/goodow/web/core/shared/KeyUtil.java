/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.web.core.shared;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

@Singleton
public class KeyUtil {
  public static final String ANY_SEPARATOR_PATTERN = "@[plsm]@";
  public static final String PROXY_SEPARATOR = "@p@";
  public static final String LIST_SEPARATOR = "@l@";
  public static final String SET_SEPARATOR = "@s@";
  public static final String MEDIA_SEPARATOR = "@m@";
  private final RequestFactory f;
  private static final int KEY_TOKEN_INDEX = 0;
  private static final int TYPE_TOKEN_INDEX = 1;

  @Inject
  KeyUtil(final RequestFactory f) {
    this.f = f;
  }

  public boolean isEncodedKey(final String key) {
    return key != null && key.matches(".*" + ANY_SEPARATOR_PATTERN + ".*");
  }

  public boolean isMedia(final String encodedKey) {
    return encodedKey != null && encodedKey.contains(MEDIA_SEPARATOR);
  }

  public String listKey(final String listKey) {
    return LIST_SEPARATOR + listKey;
  }

  public String proxy(final EntityProxyId<?> proxyId) {
    return f.getHistoryToken(proxyId);
  };

  public String proxyAndKey(final EntityProxyId<?> parentId, final String key) {
    return f.getHistoryToken(parentId) + PROXY_SEPARATOR + key;
  }

  public String proxyAndListKey(final EntityProxyId<?> parentId, final String listKey) {
    return f.getHistoryToken(parentId) + LIST_SEPARATOR + listKey;
  }

  public String proxyAndMediaKey(final EntityProxyId<?> parentId, final String meidaKey) {
    return f.getHistoryToken(parentId) + MEDIA_SEPARATOR + meidaKey;
  }
}
