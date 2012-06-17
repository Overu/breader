package com.goodow.web.reader.client;

import java.util.logging.Logger;

import com.goodow.web.core.client.ClientMessage;
import com.goodow.web.core.shared.Message;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Request;
import com.goodow.web.reader.shared.ExampleFactory;
import com.goodow.web.reader.shared.ExamplePackage;
import com.goodow.web.reader.shared.Library;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public final class ReaderClientPlugin extends AbstractGinModule {

  public static class Startup {
    @Inject
    public Startup(final ClientLibraryService libraryService) {
      Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
        @Override
        public boolean execute() {
          onStart(libraryService);
          return false;
        }
      }, 20);
    }

    /**
     * @param libraryService
     */
    private void onStart(final ClientLibraryService libraryService) {
      final Label l = new Label("hello");
      RootPanel.get().add(l);
      final Library lib = ExampleFactory.Library.get();
      lib.setTitle("ggg");
      Request<Library> request = libraryService.save(lib, false, 5);
      request.fire(new Receiver<Library>() {

        @Override
        public void onSuccess(final Library result) {
          l.setText("Created: " + result.getId() + "@" + result.getVersion());
        }
      });
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    logger.finest("config start");

    bind(Message.class).to(ClientMessage.class);

    bind(ExamplePackage.class).asEagerSingleton();
    bind(Startup.class).asEagerSingleton();
  }
}
