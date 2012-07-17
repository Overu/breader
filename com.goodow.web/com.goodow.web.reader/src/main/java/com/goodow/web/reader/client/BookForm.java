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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.inject.Inject;

public class BookForm extends FormView implements ResourceUploadedHandler {

  interface Bundle extends ClientBundle {

    @Source("BookForm.css")
    Style bookFormCss();
  }

  interface Style extends CssResource {
    String resourceFieldCss();

    String richTextField();

    String utilityCss();
  }

  private static Bundle bundle;

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

  static {
    bundle = GWT.create(Bundle.class);
    bundle.bookFormCss().ensureInjected();
  }

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
    sourceField.addStyleName(bundle.bookFormCss().resourceFieldCss());
    titleField.setLabel("书名");
    titleField.addStyleName(bundle.bookFormCss().utilityCss());
    coverField.addStyleName(bundle.bookFormCss().utilityCss());
    descField.setLabel("简介");
    descField.addStyleName(bundle.bookFormCss().richTextField());

    main.add(sourceField);
    main.add(titleField);
    main.add(coverField);
    main.add(descField);

    sourceField.addResourceUploadedHandler(this);
  }

}