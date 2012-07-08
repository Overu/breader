package com.goodow.web.reader.client;

import com.goodow.web.reader.client.style.ReaderResources;
import com.goodow.web.reader.shared.AsyncLibraryService;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderProductClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {

    public Bind() {
      ReaderResources.INSTANCE();
    }

  }

  public static class ReaderUI {
    @Inject
    public ReaderUI(final AsyncLibraryService libraryService) {
      // Window.Location
    }

    public native void redirect() /*-{
			$wnd.location.href = "http://fantongx.vicp.net:8888/index.html";
    }-*/;

    // @Inject
    // public ReaderUI(final AsyncLibraryService libraryService) {
    // final Library lib = ReaderPackage.Library.get();
    // lib.setTitle("ggg");
    // Request<Library> request = libraryService.save(lib, false, 5);
    // request.fire(new Receiver<Library>() {
    // @Override
    // public void onSuccess(final Library result) {
    // String msg =
    // "Created: id=" + result.getId() + " version=" + result.getVersion() + " title="
    // + result.getTitle();
    // logger.info(msg);
    // }
    // });
    // }
  }

  private static final Logger logger = Logger.getLogger(ReaderProductClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(ReaderUI.class).asEagerSingleton();
    bind(Bind.class).asEagerSingleton();
  }

}
