package com.goodow.web.reader.test;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.BookService;
import com.goodow.web.reader.shared.Category;
import com.goodow.web.reader.shared.CategoryService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BookTest extends ExampleTest {

  @Inject
  BookService bookService;

  @Inject
  CategoryService categoryService;

  @Inject
  JSONMarshaller jsonMarshaller;

  @Inject
  Provider<Message> messageProvider;

  @Test
  public void testCategory() {
    // for (int i = 0; i < 5; i++) {
    // Category category = new Category();
    // category.setId(UUID.randomUUID().toString());
    // category.setTitle("分类" + i);
    // categoryService.save(category);
    // }

    List<Category> categorys = categoryService.getCategory();
    for (Category category : categorys) {
      System.out.print(category.getTitle());
    }
  }

  @Test
  public void testEPubBook() {
    try {
      InputStream in = BookTest.class.getResourceAsStream("/test-data/book1.epub");
      Resource resource = ServletMessage.createResource(in, "book1.epub", "application/epub+zip");
      Book book = bookService.extract(resource);
      String json = jsonMarshaller.serialize(book);
      System.out.println(json);
      assertEquals("大汉雄师", book.getTitle());
      List<Book> books = bookService.getMyBooks();
      assertTrue(books.size() > 0);
      Book b = books.get(0);
      assertEquals("大汉雄师", b.getTitle());
      bookService.remove(b);
      assertNull(bookService.find(b.getId()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSelectedBooks() {
    try {
      InputStream in = BookTest.class.getResourceAsStream("/test-data/book1.epub");
      Resource resource = ServletMessage.createResource(in, "book2.epub", "application/epub+zip");
      Book book = bookService.extract(resource);
      book.setSelected(true);
      bookService.save(book);
      List<Book> books = bookService.getSelectedBooks();
      assertTrue(books.size() > 0);
      Book firstBook = books.get(0);
      assertEquals(book.getTitle(), firstBook.getTitle());

      for (Book b : books) {
        assertTrue(b.isSelected());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
