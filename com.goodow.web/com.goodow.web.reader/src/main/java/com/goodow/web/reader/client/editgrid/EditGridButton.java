package com.goodow.web.reader.client.editgrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class EditGridButton extends Composite {

  interface Bundle extends ClientBundle {

    ImageResource addpage();

    @Source("editgridbutton.css")
    Style css();

    ImageResource deletepage();

    ImageResource okstack();

    ImageResource save();

  }

  interface Style extends CssResource {

    String addpage();

    String base();

    String cancel();

    String deletenowpage();

    String okstack();

    String save();

    String savetext();

  }

  static Bundle bundle;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.css().ensureInjected();
  }

  SimplePanel simplePanel;

  public EditGridButton(final String buttonCssName) {
    simplePanel = new SimplePanel();
    initWidget(simplePanel);
    simplePanel.addStyleName(bundle.css().base());
    simplePanel.addStyleName(buttonCssName);
    addStyleName(buttonCssName);
    simplePanel.getElement().setInnerHTML(getButtonText());
  }

  public abstract String getButtonText();

}
