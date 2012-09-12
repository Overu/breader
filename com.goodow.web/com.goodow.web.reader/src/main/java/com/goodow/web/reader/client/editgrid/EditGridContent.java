package com.goodow.web.reader.client.editgrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EditGridContent extends Composite {

  interface Binder extends UiBinder<Widget, EditGridContent> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  DivElement titleElm;
  @UiField
  DivElement desElm;
  @UiField
  DivElement snippetElm;

  private String caption;
  private String des;
  private String snippet;

  public EditGridContent() {
    initWidget(binder.createAndBindUi(this));
  }

  public String getCaption() {
    return caption == null ? "" : caption;
  }

  public String getDes() {
    return des == null ? "" : des;
  }

  public String getSnippet() {
    return snippet == null ? "" : snippet;
  }

  public void setCaption(final String caption) {
    this.caption = caption;
    if (isNull(caption, titleElm)) {
      return;
    }
    titleElm.setInnerHTML(caption);
  }

  public void setDes(final String des) {
    this.des = des;
    if (isNull(des, desElm)) {
      return;
    }
    desElm.setInnerHTML(des);
  }

  public void setSnippet(final String snippet) {
    this.snippet = snippet;
    if (isNull(snippet, snippetElm)) {
      return;
    }
    snippetElm.setInnerHTML(snippet);
  }

  private boolean isNull(final String s, final Element elm) {
    boolean b = s == null || "".equals(s);
    if (b) {
      setDisplayNone(elm);
      return b;
    }
    setClearDisplay(elm);
    return false;
  }

  private void setClearDisplay(final Element elm) {
    elm.getStyle().clearDisplay();
  }

  private void setDisplayNone(final Element elm) {
    elm.getStyle().setDisplay(Display.NONE);
  }
}
