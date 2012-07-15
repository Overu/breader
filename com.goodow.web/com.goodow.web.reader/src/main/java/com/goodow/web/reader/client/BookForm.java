package com.goodow.web.reader.client;

import com.goodow.web.core.client.MediaField;
import com.goodow.web.core.client.RichTextField;
import com.goodow.web.core.client.TextField;

import com.google.inject.Inject;

public class BookForm extends FormView {

  @Inject
  MediaField mediaField;

  @Inject
  TextField titleField;

  @Inject
  RichTextField summaryField;

  @Override
  protected void start() {
    mediaField.setLabel("图书内容 （支持EPUB, PDF, TXT, HTML）");
    titleField.setLabel("书名");
    summaryField.setLabel("简介");

    main.add(mediaField);
    main.add(titleField);
    main.add(summaryField);
  }

}