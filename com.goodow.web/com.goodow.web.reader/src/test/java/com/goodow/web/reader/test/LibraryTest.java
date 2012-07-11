package com.goodow.web.reader.test;

import com.goodow.web.core.server.JSONMarshaller;
import com.goodow.web.core.shared.Accessor;
import com.goodow.web.core.shared.Message;
import com.goodow.web.reader.shared.Library;
import com.goodow.web.reader.shared.LibraryService;
import com.goodow.web.reader.shared.ReaderPackage;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.json.JSONException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class LibraryTest extends ExampleTest {

  @Inject
  LibraryService libraryService;

  @Inject
  JSONMarshaller jsonMarshaller;

  @Inject
  Provider<Message> messageProvider;

  @Test
  public void testCreateLibrary() throws JSONException {
    List<Library> list = new ArrayList<Library>();
    for (int i = 0; i < 3; i++) {
      Library library = new Library();
      library.setTitle(generateId(Library.class));
      Library result = libraryService.save(library, true, 10);
      System.out.println(result);
      Accessor access = ReaderPackage.Library.as().getAccessor();
      System.out.println(access);
      String json = jsonMarshaller.serialize(library);
      System.out.println(json);
      list.add(library);
    }

    String json = jsonMarshaller.serialize(list);
    System.out.println(json);

    json = jsonMarshaller.serialize(new Object[] {false, "File", 10});
    System.err.println(json);

    Message message = messageProvider.get();
    message.getResponse().setResult(list);
    json = jsonMarshaller.serialize(message.getResponse());
    System.out.println(json);
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
