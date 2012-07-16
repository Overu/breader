package com.goodow.web.core.parser;

import junit.framework.TestCase;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.epub.EpubParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EpubParserTest extends TestCase {

  public void testXMLParser() throws Exception {
    InputStream input = EpubParserTest.class.getResourceAsStream("/test-documents/testEPUB.epub");
    try {
      Metadata metadata = new Metadata();
      ContentHandler handler = new BodyContentHandler();
      new EpubParser().parse(input, handler, metadata, new ParseContext());

      assertEquals("application/epub+zip", metadata.get(Metadata.CONTENT_TYPE));
      assertEquals("en", metadata.get(Metadata.LANGUAGE));
      assertEquals("This is an ePub test publication for Tika.", metadata.get(Metadata.DESCRIPTION));
      assertEquals("Apache", metadata.get(Metadata.PUBLISHER));

      String content = handler.toString();
      assertTrue(content.contains("Plus a simple div"));
      assertTrue(content.contains("First item"));
      assertTrue(content.contains("The previous headings were subchapters"));
      assertTrue(content.contains("Table data"));
    } finally {
      input.close();
    }

    String zipName = EpubParserTest.class.getResource("/test-documents/testEPUB.epub").getFile();
    try {
      FileInputStream fis = new FileInputStream(zipName);
      ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
      ZipEntry entry;

      //
      // Read each entry from the ZipInputStream until no
      // more entry found indicated by a null return value
      // of the getNextEntry() method.
      //
      while ((entry = zis.getNextEntry()) != null) {
        System.out.println("Unzipping: " + entry.getName());

        int size;
        byte[] buffer = new byte[2048];
        File file = new File(entry.getName());
        if (entry.isDirectory()) {
          file.mkdirs();
        } else {
          File parent = file.getParentFile();
          if (parent != null) {
            parent.mkdirs();
          }
          file.createNewFile();
          FileOutputStream fos = new FileOutputStream(file);
          BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
          while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, size);
          }
          bos.flush();
          bos.close();
        }
      }

      zis.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
