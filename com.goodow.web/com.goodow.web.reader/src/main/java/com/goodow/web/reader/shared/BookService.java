package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebService;

import java.util.List;

public interface BookService extends WebService<Book> {

  Book extract(Resource resource);

  List<Book> getMyBooks();

  List<Book> getSelectedBooks();

  @Override
  void remove(Book book);

  @Override
  Book save(Book book);

}
