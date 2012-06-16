package com.goodow.web.example.test;

import com.goodow.web.core.server.JsonBuilder;
import com.goodow.web.core.shared.Request;
import com.goodow.web.core.shared.Response;
import com.goodow.web.example.shared.ExamplePackage;
import com.goodow.web.example.shared.Library;
import com.goodow.web.example.shared.LibraryService;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.json.JSONException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class LibraryTest extends ExampleTest {

  @Inject
  LibraryService libraryService;

  @Inject
  JsonBuilder builder;
  @Inject
  Provider<Request> request;
  @Inject
  Provider<Response> response;

  @Test
  public void testCreateLibrary() throws JSONException {
    Library library = new Library();
    library.setTitle(generateId(Library.class));
    Library result = libraryService.save(library, true, 10);
    Request req = request.get();
    req.setOperation(ExamplePackage.LibraryService_save.as());
    Response res = response.get();
    res.setResult(result);
    String s = builder.serialize(req, res);
    System.out.println(s);
  }

  @Test
  public void testJAXB() throws JAXBException {
    Library library = new Library();
    library.setId(UUID.randomUUID().toString());
    library.setTitle("my-title");
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Marshaller marshaller = jc.createMarshaller();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    marshaller.marshal(library, os);
    System.out.println(os.toString());
  }
}
