package com.goodow.web.reader.client;

import com.goodow.web.core.client.RichTextEditor;
import com.goodow.web.core.client.WebView;

import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;

public class BookForm extends WebView {

  @Inject
  Label fileLabel;

  @Inject
  FileUpload fileUpload;

  @Inject
  Label titleLabel;

  @Inject
  TextBox titleBox;

  @Inject
  Label summaryLabel;

  @Inject
  RichTextEditor summaryEditor;

  @Override
  protected void start() {
    fileLabel.setText("图书内容 （支持EPUB, PDF, TXT, HTML）");
    titleLabel.setText("书名");
    summaryLabel.setText("简介");

    main.add(fileLabel);
    main.add(fileUpload);
    main.add(titleLabel);
    main.add(titleBox);
    main.add(summaryLabel);
    main.add(summaryEditor);
  }

}