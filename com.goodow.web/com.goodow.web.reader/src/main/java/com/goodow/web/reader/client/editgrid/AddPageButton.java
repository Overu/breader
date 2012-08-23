package com.goodow.web.reader.client.editgrid;

public class AddPageButton extends EditGridButton {

  public AddPageButton() {
    super(bundle.css().addpage());
  }

  @Override
  public String getButtonText() {
    return "添加一页";
  }

}
