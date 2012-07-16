package com.goodow.web.reader.client;

import com.goodow.web.core.client.ImageField;
import com.goodow.web.core.client.ResourceField;
import com.goodow.web.core.client.RichTextField;
import com.goodow.web.core.client.TextField;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceUploadedEvent;
import com.goodow.web.core.shared.ResourceUploadedHandler;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.inject.Inject;

public class BookForm extends FormView implements ResourceUploadedHandler {

  @Inject
  ResourceField sourceField;

  @Inject
  TextField titleField;

  @Inject
  ImageField coverField;

  @Inject
  RichTextField descField;

  @Inject
  AsyncBookService bookService;

  @Override
  public void onResourceUpload(final ResourceUploadedEvent event) {
    Resource resource = event.getNewResource();
    bookService.extract(resource).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        titleField.setValue(result.getTitle());
        coverField.setValue(result.getCover());
        descField.setValue(result.getDescription());
      }
    });
  }

  @Override
  protected void start() {
    sourceField.setLabel("上传图书内容 （支持EPUB, PDF, TXT, HTML）");
    titleField.setLabel("书名");
    descField.setLabel("简介");

    main.add(sourceField);
    main.add(titleField);
    main.add(coverField);
    main.add(descField);

    sourceField.addResourceUploadedHandler(this);
  }

}