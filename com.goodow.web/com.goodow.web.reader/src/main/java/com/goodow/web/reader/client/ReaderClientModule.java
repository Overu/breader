package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Request;
import com.goodow.web.reader.shared.AsyncLibraryService;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.ReaderPackage;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

import java.util.logging.Logger;

public final class ReaderClientModule extends AbstractGinModule {

  public static class ReaderUI {
    @Inject
    public ReaderUI(final AsyncLibraryService libraryService) {
      final Label l = new Label("hello");
      RootPanel.get().add(l);
      final Library lib = ReaderPackage.Library.get();
      lib.setTitle("ggg");
      Request<Library> request = libraryService.save(lib, false, 5);
      request.fire(new Receiver<Library>() {
        @Override
        public void onSuccess(final Library result) {
          l.setText("Created: id=" + result.getId() + " version=" + result.getVersion() + " title="
              + result.getTitle());
        }
      });
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.info("config ReaderClientModule");
    bind(ReaderUI.class).asEagerSingleton();
  }

}
