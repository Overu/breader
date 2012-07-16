package com.goodow.web.reader.test;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.BookService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class BookTest extends ExampleTest {

  @Inject
  BookService bookService;

  @Inject
  JSONMarshaller jsonMarshaller;

  @Inject
  Provider<Message> messageProvider;

  @Test
  public void testEPubBook() {

    try {
      InputStream in = BookTest.class.getResourceAsStream("/books/book1.epub");
      Resource resource = ServletMessage.createResource(in, "book1.epub", "application/epub+zip");
      Book book = bookService.extract(resource);
      assertEquals("大汉雄师", book.getTitle());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
