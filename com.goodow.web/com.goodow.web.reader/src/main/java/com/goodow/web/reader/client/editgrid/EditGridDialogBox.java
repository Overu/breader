package com.goodow.web.reader.client.editgrid;

import com.goodow.web.reader.client.PopupContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TextAreaElement;
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

  private EditGridCell currentCell;

  @UiField
  CancelButton cancelButton;
  @UiField
  SaveTextButton saveTextButton;
  @UiField
  InputElement titleElm;
  @UiField
  TextAreaElement desElm;
  @UiField
  TextAreaElement snippetElm;

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
        getContent();
        hide();
      }
    }, ClickEvent.getType());
  }

  public void dialog(final EditGridCell cell) {
    if (cell == null) {
      return;
    }
    claerElmContent();
    currentCell = cell;
    EditGridContent content = cell.getEditGridContent();
    setContent(content.getCaption(), content.getDes(), content.getSnippet());
    pp.dialog(this);
  }

  public void hide() {
    pp.hide();
  }

  private void claerElmContent() {
    titleElm.setValue("");
    desElm.setValue("");
    snippetElm.setValue("");
  }

  private void getContent() {
    if (currentCell == null) {
      return;
    }
    currentCell.addAndInsteadEditGridContent(titleElm.getValue(), desElm.getValue(), snippetElm
        .getValue());
  }

  private void setContent(final String capton, final String des, final String snippet) {
    titleElm.setValue(capton == null ? "" : capton);
    desElm.setValue(des == null ? "" : des);
    snippetElm.setValue(snippet == null ? "" : snippet);
  }

}
