package com.goodow.web.example.shared;

import com.goodow.web.core.shared.*;

@com.google.inject.Singleton
public class ExamplePackage extends com.goodow.web.core.shared.Package {
  public static class BookAccessor<T extends Book> extends com.goodow.web.core.shared.CorePackage.ContentAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      return super.getProperty(entity, property);
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      super.setProperty(entity, property, value);
    }
  }
  public static class LibraryAccessor<T extends Library> extends com.goodow.web.core.shared.CorePackage.ContentAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("title".equals(property.getName())) {
        return entity.getTitle();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("title".equals(property.getName())) {
        entity.setTitle((java.lang.String) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static final EntityInfo Book = new EntityInfo(Book.class, ExampleFactory.Book, null);
  public static final EntityInfo Library = new EntityInfo(Library.class, ExampleFactory.Library, LibraryService.class);
  public static final OperationInfo<Library> LibraryService_save = new OperationInfo<Library>("save");
  public ExamplePackage() {
    setName("com.goodow.web.example.shared");
    addEntityTypes(Book.as(), Library.as());
    addValueTypes();
    Book.as().setSuperType(com.goodow.web.core.shared.CorePackage.Content.as());
    Book.as().setAccessor(new BookAccessor());
    
    Library.as().setSuperType(com.goodow.web.core.shared.CorePackage.Content.as());
    Library.as().setAccessor(new LibraryAccessor());
    addProperty(Library.as(), "title", CorePackage.String.as(), false);
    
    addOperations(Library.as(), LibraryService_save);
    addParameter(LibraryService_save.as(), "library", ExamplePackage.Library.as());
    addParameter(LibraryService_save.as(), "flag", CorePackage.BOOLEAN.as());
    addParameter(LibraryService_save.as(), "size", CorePackage.INT.as());
    
  }
}
