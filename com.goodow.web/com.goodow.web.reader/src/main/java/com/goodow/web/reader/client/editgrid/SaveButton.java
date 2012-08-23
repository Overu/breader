package com.goodow.web.reader.client.editgrid;

public class SaveButton extends EditGridButton {

  public SaveButton() {
    super(bundle.css().save());
  }

  @Override
  public String getButtonText() {
    return "保存进度";
  }

}
