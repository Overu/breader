package com.goodow.web.example.shared;

import com.goodow.web.core.shared.*;
import com.google.inject.*;

@Singleton
public class ExampleFactory extends Factory {
  
  @Inject
  public static Provider<Book> Book;
  
  @Inject
  public static Provider<Library> Library;
}