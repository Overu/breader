package com.goodow.web.reader.client.editgrid;

public class SaveTextButton extends EditGridButton {

  public SaveTextButton() {
    super(bundle.css().savetext());
  }

  @Override
  public String getButtonText() {
    return "保存";
  }

}
