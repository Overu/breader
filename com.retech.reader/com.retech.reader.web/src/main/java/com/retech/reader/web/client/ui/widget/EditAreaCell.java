package com.retech.reader.web.client.ui.widget;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class EditAreaCell extends EditTextCell {
  interface Template extends SafeHtmlTemplates {
    @Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\"></input>")
    SafeHtml input(String value);
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final String value,
      final SafeHtmlBuilder sb) {
    // TODO Auto-generated method stub
    super.render(context, value, sb);
  }
}
