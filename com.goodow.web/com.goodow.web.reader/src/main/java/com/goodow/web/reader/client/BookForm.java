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
import com.goodow.web.core.shared.WebPlaceMapper;
import com.goodow.web.reader.shared.AsyncBookService;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.ReaderPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.OrganizeButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ReplyButton;

import java.util.List;
import java.util.logging.Logger;

public class BookForm extends FormView<Book> implements ResourceUploadedHandler, Editor<Book> {

  interface Bundle extends ClientBundle {

    @Source("BookForm.css")
    Style bookFormCss();
  }

  interface Driver extends SimpleBeanEditorDriver<Book, BookForm> {
  }

  interface Style extends CssResource {

    String main();

    String resourceFieldCss();

    String richTextField();

    String utilityCss();
  }

  public static final String NAME = "form";

  private static Driver driver = GWT.create(Driver.class);

  private static Logger logger = Logger.getLogger(BookForm.class.getName());

  private static Bundle bundle;

  @Inject
  @Ignore
  ResourceField source;

  @Inject
  TextField title;

  @Inject
  TextField author;

  @Inject
  ImageField cover;

  @Inject
  RichTextField description;

  @Inject
  CheckField selected;

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
  WebPlaceMapper placeManager;

  @Inject
  ReaderPlace reader;

  @Inject
  ListBoxField category;

  static {
    bundle = GWT.create(Bundle.class);
    bundle.bookFormCss().ensureInjected();
  }

  protected Book book;

  @Override
  public Book getValue() {
    return book;
  }

  @Override
  public void onResourceUpload(final ResourceUploadedEvent event) {
    Resource resource = event.getNewResource();
    bookService.extract(resource).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        setValue(result);
      }
    });
  }

  @Override
  public void refresh() {
    categoryService.getCategory().fire(
        new Receiver<java.util.List<com.goodow.web.core.shared.Category>>() {
          @Override
          public void onSuccess(final List<Category> result) {
            category.clear();
            for (Category cate : result) {
              category.setValue(cate);
            }
          }
        });
  }

  @Override
  public void setValue(final Book value) {
    this.book = value;
    driver.edit(book);
  }

  public void submit() {

    driver.flush();

    if (driver.hasErrors()) {
      // A sub-editor reported errors
    }

    bookService.save(book).fire(new Receiver<Book>() {
      @Override
      public void onSuccess(final Book result) {
        logger.info("book created: " + result.getId());
        placeManager.gotoContent(result);
      }
    });
  }

  @Override
  protected void start() {
    source.setLabel("上传图书内容 （支持EPUB, PDF, TXT, HTML）");
    source.addStyleName(bundle.bookFormCss().resourceFieldCss());
    title.setLabel("书名");
    title.addStyleName(bundle.bookFormCss().utilityCss());
    author.setLabel("作者");
    author.addStyleName(bundle.bookFormCss().utilityCss());
    cover.addStyleName(bundle.bookFormCss().resourceFieldCss());
    description.setLabel("简介");
    description.addStyleName(bundle.bookFormCss().richTextField());
    selected.addStyleName(bundle.bookFormCss().utilityCss());
    selected.setLabel("标记为精品");
    category.addStyleName(bundle.bookFormCss().utilityCss());
    category.setLabel("分类");

    buttonBar.add(submitButton);
    buttonBar.add(cancelButton);

    main.addStyleName(bundle.bookFormCss().main());

    HTMLPanel htmlPanel = new HTMLPanel("");
    htmlPanel.add(source);
    htmlPanel.add(title);
    htmlPanel.add(cover);
    htmlPanel.add(description);
    htmlPanel.add(selected);
    htmlPanel.add(category);

    main.add(htmlPanel);
    main.add(buttonBar);

    source.addResourceUploadedHandler(this);
    submitButton.addTapHandler(new TapHandler() {
      @Override
      public void onTap(final TapEvent event) {
        submit();
      }
    });

    cancelButton.addTapHandler(new TapHandler() {
      @Override
      public void onTap(final TapEvent event) {
        placeManager.gotoFeed(null, "books");
      }
    });

    driver.initialize(this);
    setValue(new Book());
  }

}
