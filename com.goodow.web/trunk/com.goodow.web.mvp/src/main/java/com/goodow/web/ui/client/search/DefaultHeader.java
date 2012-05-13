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
package com.goodow.web.ui.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import java.util.Date;

@Singleton
public class DefaultHeader extends Composite implements Header {

  interface DefaultHeaderUiBinder extends UiBinder<Widget, DefaultHeader> {
  }

  private static DefaultHeaderUiBinder uiBinder = GWT.create(DefaultHeaderUiBinder.class);

  @UiField
  HTMLPanel titleBar;
  /**
   * A drop box used to change the locale.
   */
  @UiField
  ListBox localeBox;
  /**
   * The container around locale selection.
   */
  @UiField
  TableCellElement localeSelectionCell;

  public DefaultHeader() {
    initWidget(uiBinder.createAndBindUi(this));
    initializeLocaleBox();
  }

  /**
   * Initialize the {@link ListBox} used for locale selection.
   */
  private void initializeLocaleBox() {
    final String cookieName = LocaleInfo.getLocaleCookieName();
    final String queryParam = LocaleInfo.getLocaleQueryParam();
    if (cookieName == null && queryParam == null) {
      // if there is no way for us to affect the locale, don't show the selector
      localeSelectionCell.getStyle().setDisplay(Display.NONE);
      return;
    }
    String currentLocale = LocaleInfo.getCurrentLocale().getLocaleName();
    if (currentLocale.equals("default")) {
      currentLocale = "zh";
    }
    String[] localeNames = LocaleInfo.getAvailableLocaleNames();
    for (String localeName : localeNames) {
      if (!localeName.equals("default")) {
        String nativeName = LocaleInfo.getLocaleNativeDisplayName(localeName);
        localeBox.addItem(nativeName, localeName);
        if (localeName.equals(currentLocale)) {
          localeBox.setSelectedIndex(localeBox.getItemCount() - 1);
        }
      }
    }
    localeBox.addChangeHandler(new ChangeHandler() {
      @Override
      @SuppressWarnings("deprecation")
      public void onChange(final ChangeEvent event) {
        String localeName = localeBox.getValue(localeBox.getSelectedIndex());
        if (cookieName != null) {
          // expire in one year
          Date expires = new Date();
          expires.setYear(expires.getYear() + 1);
          Cookies.setCookie(cookieName, localeName, expires);
        }
        if (queryParam != null) {
          UrlBuilder builder = Location.createUrlBuilder().setParameter(queryParam, localeName);
          Window.Location.replace(builder.buildString());
        } else {
          // If we are using only cookies, just reload
          Window.Location.reload();
        }
      }
    });
  }
}
