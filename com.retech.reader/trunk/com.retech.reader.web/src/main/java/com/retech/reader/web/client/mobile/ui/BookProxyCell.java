package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

import com.retech.reader.web.shared.proxy.IssueProxy;

/**
 * A {@link com.google.gwt.cell.client.Cell} used to render a {@link IssueProxy}.
 */
public class BookProxyCell extends AbstractCell<IssueProxy> {

  /**
   * The template used by this cell.
   * 
   */
  interface Template extends SafeHtmlTemplates {
    @SafeHtmlTemplates.Template("<table style='width:100%; margin:0px; padding: 0px;' border='0'><tr><td width='60px'><img src=\"{0}\" width='60px' height='60px'></td><td valign='top' style=\"position: relative;\">")
    SafeHtml img(SafeUri src);

    // @SafeHtmlTemplates.Template("{0}<div style=\"font-size:80%;color:#999;\">发布日期: {1}</div><div style=\"width: 100%;position: absolute;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;\">{2}</div></td><tr/></table>")
    // SafeHtml info(SafeHtml name, String date, String detail);

    @SafeHtmlTemplates.Template("<table style='width:100%; margin:0px; padding: 0px;' border='0'><tr><td width='60px' height='60px'></td><td valign='top' style=\"position: relative;\">{0}<div style=\"font-size:80%;color:#999;\">发布日期: {1}</div><div style=\"width: 100%;position: absolute;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;\">{2}</div></td><tr/></table>")
    SafeHtml info(SafeHtml name, String date, String detail);
  }

  private static Template template;
  private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);

  public BookProxyCell() {
    if (template == null) {
      template = GWT.create(Template.class);
    }
  }

  @Override
  public void render(final com.google.gwt.cell.client.Cell.Context context, final IssueProxy value,
      final SafeHtmlBuilder sb) {
    if (value == null) {
      return;
    }

    SafeHtml name;
    if (value.getTitle() == null) {
      name = SafeHtmlUtils.fromSafeConstant("<i>Unnamed</i>");
    } else {
      name = SafeHtmlUtils.fromString(value.getTitle());
    }
    // ResourceProxy image = value.getImage();
    // if (image != null) {
    // sb.append(template.img(UriUtils.fromTrustedString("data:" + image.getMimeType().getType()
    // + ";base64," + image.getDataString())));
    // }
    sb.append(template.info(name, dateFormat.format(value.getCreateTime()), value.getDetail()));
  }
}
