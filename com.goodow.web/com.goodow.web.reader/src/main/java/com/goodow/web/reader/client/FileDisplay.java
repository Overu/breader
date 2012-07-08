package com.goodow.web.reader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.Dialogs;
import com.googlecode.mgwt.ui.client.dialog.Dialogs.ButtonType;
import com.googlecode.mgwt.ui.client.dialog.Dialogs.OptionCallback;
import com.googlecode.mgwt.ui.client.dialog.Dialogs.OptionsDialogEntry;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.MTextArea;
import com.googlecode.mgwt.ui.client.widget.base.ButtonBase;
import com.googlecode.mgwt.ui.client.widget.celllist.BasicCell;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

import java.util.ArrayList;
import java.util.LinkedList;

public class FileDisplay extends Composite {

  public interface Presenter {

    public void createFile(String fileName);

    public void onActionButtonPressed();

    public void onBackButtonPressed();

    public void onEntrySelected(int index);

    public void overWriteFile();

  }

  interface FileDisplayGwtImplUiBinder extends UiBinder<Widget, FileDisplay> {
  }

  private static FileDisplayGwtImplUiBinder uiBinder = GWT.create(FileDisplayGwtImplUiBinder.class);

  @UiField(provided = true)
  CellList<FileDemo> cellList;
  private Presenter presenter;

  @UiField
  HeaderButton backButton;

  @UiField
  ButtonBase plusButton;

  @UiField
  ButtonBase actionButton;

  @UiField
  HTML status;

  @UiField
  MTextArea content;

  public FileDisplay() {

    BasicCell<FileDemo> cell = new BasicCell<FileDemo>() {

      @Override
      public boolean canBeSelected(final FileDemo model) {
        return true;
      }

      @Override
      public String getDisplayString(final FileDemo model) {
        return model.getName();
      }
    };

    cellList = new CellList<FileDemo>(cell);

    initWidget(uiBinder.createAndBindUi(this));

    if (MGWT.getOsDetection().isTablet()) {
      backButton.setBackButton(false);
      backButton.setText("Modules");
      backButton.addStyleName(MGWTStyle.getTheme().getMGWTClientBundle().getUtilCss()
          .portraitonly());
    }

  }

  public boolean confirm(final String string) {
    return Window.confirm(string);
  }

  public HasText getFileContent() {
    return content;
  }

  public HasHTML getStatus() {
    return status;
  }

  public void render(final LinkedList<FileDemo> list) {
    cellList.render(list);

  }

  public void setPresenter(final Presenter presenter) {
    this.presenter = presenter;

  }

  public void setSelected(final int index) {
    cellList.setSelectedIndex(index, true);

  }

  public void showSelectMenu() {
    ArrayList<OptionsDialogEntry> list = new ArrayList<OptionsDialogEntry>();

    list.add(new OptionsDialogEntry("Overwrite", ButtonType.IMPORTANT));
    list.add(new OptionsDialogEntry("Cancel", ButtonType.NORMAL));

    Dialogs.options(list, new OptionCallback() {

      @Override
      public void onOptionSelected(final int index) {
        if (presenter != null) {
          if (index == 0) {
            presenter.overWriteFile();
          }
        }

      }
    });

  }

  @UiHandler("backButton")
  protected void oBackButtonPressed(final TapEvent event) {
    if (presenter != null) {
      presenter.onBackButtonPressed();
    }
  }

  @UiHandler("actionButton")
  protected void onActionButtonPressed(final TapEvent event) {
    if (presenter != null) {
      presenter.onActionButtonPressed();
    }
  }

  @UiHandler("cellList")
  protected void onCellSelected(final CellSelectedEvent event) {
    if (presenter != null) {
      presenter.onEntrySelected(event.getIndex());
    }
  }

  @UiHandler("plusButton")
  protected void onPlusButtonTapped(final TapEvent event) {
    if (presenter != null) {
      String fileName = Window.prompt("filename", "");

      presenter.createFile(fileName);
    }
  }
}
