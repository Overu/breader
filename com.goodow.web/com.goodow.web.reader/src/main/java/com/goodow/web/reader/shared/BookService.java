package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebService;

public interface BookService extends WebService<Book> {

  Book extract(Resource resource);

  @Override
  Book save(Book book);

}
