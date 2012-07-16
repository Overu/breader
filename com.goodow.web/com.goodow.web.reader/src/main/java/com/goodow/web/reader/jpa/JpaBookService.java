/**
 * Documentation here.
 */
package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaResourceService;
import com.goodow.web.core.jpa.JpaWebService;
import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.BookService;

import com.google.inject.Inject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Generated;

import nl.siegmann.epublib.epub.EpubReader;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class JpaBookService extends JpaWebService<Book> implements BookService {

  @Inject
  JpaResourceService resService;

  @Override
  public Book extract(final Resource resource) {
    Book book = new Book();
    book.setTitle(resource.getFileName());
    String zipName = resource.getPath();
    try {
      FileInputStream fis = new FileInputStream(zipName);
      nl.siegmann.epublib.domain.Book b = new EpubReader().readEpub(fis);
      book.setTitle(b.getTitle());
      nl.siegmann.epublib.domain.Resource cover = b.getCoverImage();

      Resource coverRes =
          ServletMessage.createResource(cover.getInputStream(), cover.getTitle(), cover
              .getMediaType().toString());

      resService.save(coverRes);

      book.setCover(coverRes);

      List<String> descs = b.getMetadata().getDescriptions();
      if (descs != null && !descs.isEmpty()) {
        book.setDescription(descs.get(0));
      }
      save(book);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return book;
  }
}
