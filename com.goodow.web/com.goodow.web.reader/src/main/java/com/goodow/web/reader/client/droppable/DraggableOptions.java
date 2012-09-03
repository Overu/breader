package com.goodow.web.reader.client.droppable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class DraggableOptions {

  public static enum HelperType {
    CLONE {

      @Override
      public Element createHelper(final Element original, final Element helperFromOptions) {
        return DOM.clone((com.google.gwt.user.client.Element) original, true);
      }

    },
    ELEMENT {

      @Override
      public Element createHelper(final Element original, final Element helperFromOptions) {
        return helperFromOptions;
      }

    };

    public abstract Element createHelper(Element original, Element helperFromOptions);
  }

  public static final String DEFAULT_SCOPE = "default";

  private Element helper;
  private HelperType helperType;
  private String scope;

  public DraggableOptions() {
    scope = DEFAULT_SCOPE;
  }

  public Element getHelper() {
    return helper;
  }

  public HelperType getHelperType() {
    return helperType;
  }

  public String getScope() {
    return scope;
  }

  public void setHelper(final Element helper) {
    this.helper = helper;
    this.helperType = HelperType.ELEMENT;
  }

  public void setHelperType(final HelperType helperType) {
    this.helperType = helperType;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

}
