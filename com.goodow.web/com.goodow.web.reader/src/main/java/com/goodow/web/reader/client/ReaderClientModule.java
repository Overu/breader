package com.goodow.web.reader.client;

import com.goodow.web.core.client.TextResourceEditor;
import com.goodow.web.core.client.UIManager;
import com.goodow.web.core.shared.ContainerViewer;
import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.EntryViewer;
import com.goodow.web.core.shared.FeedViewer;
import com.goodow.web.core.shared.HomePlace;
import com.goodow.web.core.shared.WebPlace;
import com.goodow.web.core.shared.WebPlatform;
import com.goodow.web.reader.client.droppable.CellListDrag;
import com.goodow.web.reader.client.editgrid.EditGridPanel;
import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.client.style.ReadResources.CellListResources;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.ReaderPackage;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {
    @Inject
    public Bind(final Provider<TextResourceEditor> textEditor, final UIManager registry, final Provider<BooksBrowser> booksBrowser,
        final Provider<BookForm> bookForm, final Provider<MyBookList> mybooksList, final Provider<SelectedBookList> selectedBooks,
        final Provider<SectionsExplorer> sectionsExplorer, final ReaderApp readerApp, @HomePlace final WebPlace homePlace,
        final WebPlace editGridPlace, final WebPlace cellListDragPlace, final EditGridPanel editGridPanel,
        final Provider<CellListDrag> cellListDrag, final Provider<BooksViewBrowser> booksViewBrowser,
        final Provider<RecommendedBookList> recommend, final Provider<FavoriteBooks> favorite, final Provider<DiscountBookList> discount,
        final Provider<MostViewedBookList> mostViewed, final Provider<CategorizedBookList> categorized,
        final Provider<BookDetail> bookDetail, final Provider<SearchBookList> searchBookList) {

      ReaderPackage.Library.as().addViewer(ContainerViewer.ENTRY, readerApp);

      CorePackage.Section.as().addViewer(ContainerViewer.FEED, sectionsExplorer);

      ReaderPackage.Book.as().addViewer(EntryViewer.EDIT, sectionsExplorer);
      ReaderPackage.Book.as().addViewer(EntryViewer.BOOKDETAIL, bookDetail);

      ReaderPackage.Book.as().addViewer(ContainerViewer.FEED, booksBrowser);
      ReaderPackage.Book.as().addViewer(FeedViewer.NEW, bookForm);
      ReaderPackage.Book.as().addViewer(FeedViewer.MY_CONTENT, booksViewBrowser);
      ReaderPackage.Book.as().addViewer(FeedViewer.SELECTED_CONTENT, selectedBooks);
      ReaderPackage.Book.as().addViewer(FeedViewer.RECOMMEND, recommend);
      ReaderPackage.Book.as().addViewer(FeedViewer.FAVORITES, favorite);
      ReaderPackage.Book.as().addViewer(FeedViewer.MOSTVIEWED, mostViewed);
      ReaderPackage.Book.as().addViewer(FeedViewer.DISCOUNTED, discount);
      ReaderPackage.Book.as().addViewer(FeedViewer.CATEGORIZED, categorized);
      ReaderPackage.Book.as().addViewer(FeedViewer.BOOKSEARCH, searchBookList);

      homePlace.setWidget(readerApp);

      WebPlace magazinesPlace = homePlace.getChild("magazines");
      magazinesPlace.setTitle("杂志");
      magazinesPlace.setWidget(booksBrowser);

      magazinesPlace.addChild(editGridPlace);
      magazinesPlace.addChild(cellListDragPlace);

      editGridPlace.setPath("editgrid");
      editGridPlace.setTitle("制作杂志");
      editGridPlace.setWidget(editGridPanel);

      cellListDragPlace.setPath("celllistdrag");
      cellListDragPlace.setTitle("拖动实验");
      cellListDragPlace.setWidget(cellListDrag);
      homePlace.setObjectType(WebPlatform.getInstance().getObjectType(Library.class.getName()));

      ReadResources.INSTANCE();
      registry.bind("text/plain").toProvider(textEditor);
      registry.bind("application/xhtml+xml").toProvider(textEditor);
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderClientModule.class.getName());

  @HomePlace
  @Provides
  @Singleton
  public WebPlace getHomePage(final WebPlace homePlace) {
    homePlace.setObjectType(WebPlatform.getInstance().getObjectType(Library.class.getName()));
    return homePlace;
  }

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(Bind.class).asEagerSingleton();
    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
  }

}
