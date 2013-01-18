package com.goodow.web.reader.shared;

import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.WebContentService;

import java.util.List;

public interface BookService extends WebContentService<Book> {

  Book extract(Resource resource);

  List<Book> getBookByTitl(String title);

  List<Book> getMyBooks();

  List<Book> getSelectedBooks();

}
