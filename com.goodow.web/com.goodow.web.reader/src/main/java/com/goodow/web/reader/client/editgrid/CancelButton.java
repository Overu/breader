package com.goodow.web.reader.client.editgrid;

public class CancelButton extends EditGridButton {

  public CancelButton() {
    super(bundle.css().cancel());
  }

  @Override
  public String getButtonText() {
    return "取消";
  }

}
