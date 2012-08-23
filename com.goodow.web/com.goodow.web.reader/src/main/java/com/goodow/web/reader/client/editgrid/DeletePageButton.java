package com.goodow.web.reader.client.editgrid;

public class DeletePageButton extends EditGridButton {

  public DeletePageButton() {
    super(bundle.css().deletenowpage());
  }

  @Override
  public String getButtonText() {
    return "删除本页";
  }

}
