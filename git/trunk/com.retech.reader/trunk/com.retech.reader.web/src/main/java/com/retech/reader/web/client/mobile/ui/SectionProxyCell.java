package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.retech.reader.web.shared.proxy.SectionProxy;

class SectionProxyCell extends AbstractCell<SectionProxy> {

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context,
      final SectionProxy value, final SafeHtmlBuilder sb) {
    if (value == null) {
      return;
    }

    SafeHtml name;
    if (value.getTitle() == null) {
      name = SafeHtmlUtils.fromSafeConstant("<i>Unnamed</i>");
    } else {
      name = SafeHtmlUtils.fromString(value.getTitle());
    }
    sb.append(name);
  }
}
