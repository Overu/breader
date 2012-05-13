package com.retech.reader.web.domain.testdata;

import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.gwt.dev.util.Util;
import com.google.gwt.util.tools.Utility;

import com.retech.reader.web.domain.DomainTest;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class TestDataUtil {

  public static final int PAGE_COUNT = 35;

  public static byte[] getCss() throws IOException {
    InputStream inputStream = TestDataUtil.class.getResourceAsStream("1/page_font.css");
    byte[] pageFont = IOUtils.toByteArray(inputStream);
    inputStream.close();
    return pageFont;
  }

  public static byte[] getHtml(final int folder, final int num) throws IOException {
    InputStream inputStream =
        TestDataUtil.class.getResourceAsStream(folder + "/" + getHtmlFilename(num) + ".html");
    byte[] pageHtml = IOUtils.toByteArray(inputStream);
    inputStream.close();
    return pageHtml;
  }

  public static String getHtmlFilename(final int num) {
    return "page_" + String.format("%02d", num) + "html";
  }

  public static byte[] getImage(final int folder, final int num) throws IOException {
    InputStream inputStream =
        TestDataUtil.class.getResourceAsStream(folder + "/" + getImageFilename(num) + ".jpg");
    if (inputStream == null) {
      return null;
    }
    byte[] pageImage =
        org.apache.geronimo.mail.util.Base64.encode(IOUtils.toByteArray(inputStream));
    inputStream.close();
    return pageImage;
  }

  public static String getImageFilename(final int num) {
    return "page_" + String.format("%02d", num) + "image";
  }

  public static byte[] getSingleHtml(final int folder, final int num) throws IOException {
    String html = null;
    html =
        Utility.getFileFromClassPath(DomainTest.class.getPackage().getName().replace('.', '/')
            + "/testdata/" + folder + "/" + getHtmlFilename(num) + ".html");
    InputStream img =
        TestDataUtil.class.getResourceAsStream(folder + "/" + getImageFilename(num) + ".jpg");
    String encode = img != null ? Base64.encode(Util.readStreamAsBytes(img)) : "";
    return html.replace("<img src=\"" + getImageFilename(num) + ".jpg\" width=\"100%\" />",
        "<img src=\"" + "data:image/jpeg" + ";base64," + encode + "\" width='100%' >").getBytes(
        "UTF-8");
  }
}
