package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.PopupContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EditGridDialogBox extends Composite {

  interface Binder extends UiBinder<Widget, EditGridDialogBox> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @Inject
  PopupContainer pp;

  @UiField
  CancelButton cancelButton;

  @UiField
  SaveTextButton saveTextButton;

  public EditGridDialogBox() {
    initWidget(binder.createAndBindUi(this));

    cancelButton.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
        hide();
      }
    }, ClickEvent.getType());

    saveTextButton.addDomHandler(new ClickHandler() {

      @Override
      public void onClick(final ClickEvent event) {
      }
    }, ClickEvent.getType());
  }

  public void dialog() {
    pp.dialog(this);
  }

  public void hide() {
    pp.hide();
  }

}
