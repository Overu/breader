package com.goodow.web.reader.client;

import com.goodow.web.core.client.TextResourceEditor;
import com.goodow.web.core.client.UIManager;
import com.goodow.web.core.shared.ContainerViewer;
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
    public Bind(final Provider<TextResourceEditor> textEditor, final UIManager registry,
        final Provider<BooksBrowser> booksBrowser, final Provider<BookForm> bookForm,
        final Provider<MyBookList> mybooksList, final Provider<SelectedBookList> selectedBooks,
        final Provider<BookEditor> bookEditor, final ReaderApp readerApp,
        @HomePlace final WebPlace homePlace, final WebPlace editGridPlace,
        final WebPlace cellListDragPlace, final EditGridPanel editGridPanel,
        final Provider<CellListDrag> cellListDrag) {

      ReaderPackage.Library.as().addViewer(ContainerViewer.ENTRY, readerApp);

      ReaderPackage.Book.as().addViewer(EntryViewer.EDIT, bookEditor);

      ReaderPackage.Book.as().addViewer(ContainerViewer.FEED, booksBrowser);
      ReaderPackage.Book.as().addViewer(FeedViewer.NEW, bookForm);
      ReaderPackage.Book.as().addViewer(FeedViewer.MY_CONTENT, mybooksList);
      ReaderPackage.Book.as().addViewer(FeedViewer.SELECTED_CONTENT, selectedBooks);

      homePlace.setWidget(readerApp);

      WebPlace booksPlace = homePlace.getChild("books");

      booksPlace.addChild(editGridPlace);
      booksPlace.addChild(cellListDragPlace);

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
