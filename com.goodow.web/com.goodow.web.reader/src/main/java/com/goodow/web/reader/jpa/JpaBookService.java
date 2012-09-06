/**
 * Documentation here.
 */
package com.goodow.web.reader.jpa;

import com.goodow.web.core.jpa.JpaEntityService;
import com.goodow.web.core.jpa.JpaResourceService;
import com.goodow.web.core.servlet.ServletMessage;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.Section;
import com.goodow.web.reader.shared.Book;
import com.goodow.web.reader.shared.BookService;
import com.goodow.web.reader.shared.Library;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Documentation 22.
 */
@Generated("cloudlet")
public class JpaBookService extends JpaEntityService<Book> implements BookService {

  @Inject
  JpaResourceService resService;

  @Inject
  JpaSectionService sectionService;

  @Inject
  JpaLibraryService libraryService;

  @Override
  @Transactional
  public Book extract(final Resource resource) {
    Book book = new Book();
    book.setTitle(resource.getFileName());
    try {
      File file = ServletMessage.getFile(resource);
      FileInputStream fis = new FileInputStream(file);
      nl.siegmann.epublib.domain.Book b = new EpubReader().readEpub(fis);
      book.setTitle(b.getTitle());
      nl.siegmann.epublib.domain.Resource cover = b.getCoverImage();

      Resource coverRes =
          ServletMessage.createResource(cover.getInputStream(), cover.getTitle(), cover
              .getMediaType().toString());

      int i = 0;
      for (TOCReference toc : b.getTableOfContents().getTocReferences()) {
        nl.siegmann.epublib.domain.Resource tocRes = toc.getResource();
        Resource sectionRes =
            ServletMessage.createResource(tocRes.getInputStream(), tocRes.getTitle(), tocRes
                .getMediaType().toString());
        resService.save(sectionRes);

        Section section = new Section();
        section.setResource(sectionRes);
        section.setDisplayOrder(++i);
        section.setTitle(toc.getTitle());
        section.setContainer(book);
        sectionService.save(section);
      }

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

  @Override
  public List<Book> getMyBooks() {
    List<Book> result =
        em().createQuery("select b from Book b order by b.dateCreated desc", Book.class)
            .getResultList();
    return result;
  }

  @Override
  public List<Book> getSelectedBooks() {
    List<Book> result =
        em().createQuery("select b from Book b where b.selected=true order by b.dateCreated desc",
            Book.class).getResultList();
    return result;
  }

  @Override
  @Transactional
  public Book save(final Book entity) {
    Date currentTime = new Date(System.currentTimeMillis());
    if (entity.getDateCreated() == null) {
      entity.setDateCreated(currentTime);
    }
    if (entity.getDateModified() == null) {
      entity.setDateModified(currentTime);
    }
    if (entity.getContainer() == null) {
      Library library = libraryService.getMyLibrary();
      entity.setContainer(library);
      entity.setPath("books");
    }
    return super.save(entity);
  }
}
