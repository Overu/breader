package com.goodow.web.example.server;

import com.goodow.web.core.jpa.JpaModule;
import com.goodow.web.example.shared.Library;
import com.goodow.web.example.shared.LibraryService;

import com.google.inject.Inject;

public final class ExampleServerPlugin extends JpaModule {

  public static class Startup {
    @Inject
    public Startup(final LibraryService libraryService) {
      Library lib = new Library();
      lib.setId("mylib");
      lib.setTitle("Frank");
      libraryService.save(lib, true, 10);
    }
  }

  @Override
  protected void configure() {
    // bind(ExamplePackage.class).asEagerSingleton();
    // bind(Startup.class).asEagerSingleton();
  }

}
