package com.goodow.web.reader.test;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.CategoryService;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceService;
import com.goodow.web.core.shared.SectionService;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.BookService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BookTest extends ExampleTest {

  @Inject
  BookService bookService;

  @Inject
  ResourceService resService;

  @Inject
  CategoryService categoryService;

  @Inject
  SectionService sectionService;

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

    // List<Category> categorys = categoryService.getCategory();
    // for (Category category : categorys) {
    // System.out.print(category.getTitle());
    // }
    List<Book> books = bookService.getMyBooks();
    String s = "";
  }

  @Test
  public void testCreateBook() throws IOException {
    Book book = new Book();
    book.setTitle("Good");
    bookService.save(book);

    Resource resource = addResource(book);
    addResource(book);

    List<Resource> resources = resService.find(book);
    assertEquals(2, resources.size());

    Resource result = resService.getById(resource.getId());
    assertEquals(resource.getMimeType(), result.getMimeType());
  }

  @Test
  public void testEPubBook() {
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
    assertNull(bookService.getById(b.getId()));
  }

  @Test
  public void testGetResource() {
    Resource result = resService.getById("fe924b75-44d2-4467-995d-961424f152be");
    System.out.println(result.getMimeType());

    Book b = bookService.getById(result.getContainer().getId());
    assertEquals(b, result.getContainer());
  }

  @Test
  public void testSelectedBooks() {
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
  }

  /**
   * @param book
   * @return
   * @throws IOException
   */
  private Resource addResource(final Book book) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream("content".getBytes());
    Resource resource = ServletMessage.createResource(in, "book2.epub", "application/epub+zip");
    resource.setContainer(book);
    resService.save(resource);
    return resource;
  }
}
