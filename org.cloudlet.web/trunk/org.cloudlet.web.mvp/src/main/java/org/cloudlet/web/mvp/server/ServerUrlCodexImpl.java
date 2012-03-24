package org.cloudlet.web.mvp.server;

import com.google.inject.Inject;

import org.cloudlet.web.mvp.shared.SimplePlaceTokenizer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerUrlCodexImpl implements SimplePlaceTokenizer.UrlCodex {
  /**
   * Characters that don't need %-escaping (minus letters and digits), according
   * to ECMAScript 5th edition for the {@code encodeURI} function.
   */
  static final String DONT_NEED_ENCODING = ";/:@+$," // uriReserved
      + "-_.!~*'()" // uriMark
      + "[]"; // could be used in IPv6 addresses
  private final Logger logger;

  @Inject
  ServerUrlCodexImpl(Logger logger) {
    this.logger = logger;
  }

  @Override
  public String decodeQueryString(String encodedUrlComponent) {
    try {
      return URLDecoder.decode(encodedUrlComponent, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      logger.log(Level.SEVERE, "\"" + encodedUrlComponent + "\"不能解码为UTF-8格式", e);
      return null;
    }
  }

  @Override
  public String encodeQueryString(String decodedUrlComponent) {
    StringBuilder sb = new StringBuilder();
    byte[] utf8bytes;
    try {
      utf8bytes = decodedUrlComponent.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      // UTF-8 is guaranteed to be implemented, this code won't ever run.
      logger.log(Level.SEVERE, "\"" + decodedUrlComponent + "\"不能编码为UTF-8格式", e);
      return null;
    }
    for (byte b : utf8bytes) {
      int c = b & 0xFF;
      // This works because characters that don't need encoding are all
      // expressed as a single UTF-8 byte
      if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9')
          || DONT_NEED_ENCODING.indexOf(c) != -1) {
        sb.append((char) c);
      } else {
        String hexByte = Integer.toHexString(c).toUpperCase();
        if (hexByte.length() == 1) {
          hexByte = "0" + hexByte;
        }
        sb.append('%').append(hexByte);
      }
    }
    return sb.toString();
  }
}