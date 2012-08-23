package com.goodow.web.reader.client.editgrid;

public class OkstackButton extends EditGridButton {

  public OkstackButton() {
    super(bundle.css().okstack());
  }

  @Override
  public String getButtonText() {
    return "完成编辑";
  }

}
