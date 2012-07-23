package com.goodow.web.reader.client;

import com.goodow.web.core.client.CheckField;
import com.goodow.web.core.client.ImageField;
import com.goodow.web.core.client.ListBoxField;
import com.goodow.web.core.client.ResourceField;
import com.goodow.web.core.client.RichTextField;
import com.goodow.web.core.client.TextField;
import com.goodow.web.core.shared.AsyncCategoryService;
import com.goodow.web.core.shared.Category;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceUploadedEvent;
import com.goodow.web.core.shared.ResourceUploadedHandler;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.OrganizeButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ReplyButton;

import java.util.List;
import java.util.logging.Logger;

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

  Logger logger = Logger.getLogger(BookForm.class.getName());

  private static Bundle bundle;

  @Inject
  ResourceField sourceField;

  @Inject
  TextField titleField;

  @Inject
  TextField authorField;

  @Inject
  ImageField coverField;

  @Inject
  RichTextField descField;

  @Inject
  CheckField selectedField;

  @Inject
  ButtonBar buttonBar;

  @Inject
  OrganizeButton submitButton;

  @Inject
  ReplyButton cancelButton;

  @Inject
  AsyncBookService bookService;

  @Inject
  AsyncCategoryService categoryService;

  @Inject
  PlaceController placeController;

  @Inject
  ReaderPlugin reader;

  @Inject
  ListBoxField listBoxField;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.bookFormCss().ensureInjected();
  }

  protected Book book;

  @Override
  public void onResourceUpload(final ResourceUploadedEvent event) {
    Resource resource = event.getNewResource();
    bookService.extract(resource).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        setInput(result);
      }
    });
  }

  @Override
  public void refresh() {
    categoryService.getCategory().fire(
        new Receiver<java.util.List<com.goodow.web.core.shared.Category>>() {

          @Override
          public void onSuccess(final List<Category> result) {
            listBoxField.clear();
            for (Category category : result) {
              listBoxField.setValue(category);
            }
          }
        });
  }

  public void setInput(final Book book) {
    this.book = book;
    titleField.setValue(book.getTitle());
    authorField.setValue(book.getAuthor());
    coverField.setValue(book.getCover());
    descField.setValue(book.getDescription());
    selectedField.setValue(book.isSelected());
    listBoxField.setValue(book.getCategory());
  }

  public void submit() {
    book.setTitle(titleField.getValue());
    book.setDescription(descField.getValue());
    book.setCover(coverField.getValue());
    book.setAuthor(authorField.getValue());
    book.setSelected(selectedField.getValue());
    book.setCategory(listBoxField.getValue());
    bookService.save(book).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        logger.info("book created: " + result.getId());
        placeController.goTo(reader.myBooksPlace);
      }
    });
  }

  @Override
  protected void start() {
    sourceField.setLabel("上传图书内容 （支持EPUB, PDF, TXT, HTML）");
    sourceField.addStyleName(bundle.bookFormCss().resourceFieldCss());
    titleField.setLabel("书名");
    titleField.addStyleName(bundle.bookFormCss().utilityCss());
    authorField.setLabel("作者");
    authorField.addStyleName(bundle.bookFormCss().utilityCss());
    coverField.addStyleName(bundle.bookFormCss().resourceFieldCss());
    descField.setLabel("简介");
    descField.addStyleName(bundle.bookFormCss().richTextField());
    selectedField.addStyleName(bundle.bookFormCss().utilityCss());
    selectedField.setLabel("标记为精品");
    listBoxField.addStyleName(bundle.bookFormCss().utilityCss());
    listBoxField.setLabel("分类");

    buttonBar.add(submitButton);
    buttonBar.add(cancelButton);

    main.add(sourceField);
    main.add(titleField);
    main.add(coverField);
    main.add(descField);
    main.add(selectedField);
    main.add(listBoxField);
    main.add(buttonBar);

    sourceField.addResourceUploadedHandler(this);
    submitButton.addTapHandler(new TapHandler() {
      @Override
      public void onTap(final TapEvent event) {
        submit();
      }
    });

    cancelButton.addTapHandler(new TapHandler() {
      @Override
      public void onTap(final TapEvent event) {
        placeController.goTo(reader.myBooksPlace);
      }
    });
  }

}