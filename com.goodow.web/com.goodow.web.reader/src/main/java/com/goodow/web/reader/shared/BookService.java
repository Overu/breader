package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebEntityService;
import com.goodow.web.core.shared.Resource;

import java.util.List;

public interface BookService extends WebEntityService<Book> {

  Book extract(Resource resource);

  List<Book> getMyBooks();

  List<Book> getSelectedBooks();

  @Override
  void remove(Book book);

  @Override
  Book save(Book book);

}
