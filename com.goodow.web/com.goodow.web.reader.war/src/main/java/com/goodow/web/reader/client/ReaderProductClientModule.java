package com.goodow.web.reader.client;

import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Request;
import com.goodow.web.reader.client.style.ReadResources;
import com.goodow.web.reader.shared.AsyncLibraryService;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.ReaderPackage;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class ReaderProductClientModule extends AbstractGinModule {

  @Singleton
  public static class Bind {

    public Bind() {
      ReadResources.INSTANCE();
    }

  }

  public static class ReaderUI {
    @Inject
    public ReaderUI(final AsyncLibraryService libraryService) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

        @Override
        public void execute() {
          create(libraryService);
        }
      });
    }

    /**
     * @param libraryService
     */
    private void create(final AsyncLibraryService libraryService) {
      final Library lib = ReaderPackage.Library.get();
      lib.setTitle("ggg");
      Request<Library> request = libraryService.save(lib, false, 5);
      request.fire(new Receiver<Library>() {
        @Override
        public void onSuccess(final Library result) {
          String msg =
              "Created: id=" + result.getId() + " version=" + result.getVersion() + " title="
                  + result.getTitle();
          logger.info(msg);
        }
      });
    }
  }

  private static final Logger logger = Logger.getLogger(ReaderProductClientModule.class.getName());

  @Override
  protected void configure() {
    logger.info("configure ReaderClientModule");
    bind(ReaderUI.class).asEagerSingleton();
    bind(Bind.class).asEagerSingleton();
  }

}
