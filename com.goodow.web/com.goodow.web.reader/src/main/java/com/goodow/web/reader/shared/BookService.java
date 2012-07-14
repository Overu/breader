package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.WebService;

public interface BookService extends WebService<Book> {

  @Override
  Book save(Book book);

}
