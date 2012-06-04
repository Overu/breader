package com.goodow.web.mvp.shared;

import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.logging.Logger;

@Singleton
public final class SimplePlaceTokenizer<T extends BasePlace> implements PlaceTokenizer<T> {

  public static class ClientUrlCodexImpl implements UrlCodex {

    @Override
    public String decodeQueryString(final String encodedUrlComponent) {
      return URL.decodeQueryString(encodedUrlComponent);
    }

    @Override
    public String encodeQueryString(final String decodedUrlComponent) {
      return URL.encodeQueryString(decodedUrlComponent);
    }
  }

  @ImplementedBy(ClientUrlCodexImpl.class)
  public interface UrlCodex {
    String decodeQueryString(String encodedUrlComponent);

    String encodeQueryString(String decodedUrlComponent);
  }

  private Provider<? extends T> placeProvider;

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final UrlCodex urlCodex;

  @Inject
  SimplePlaceTokenizer(final Provider<T> placeProvider, final UrlCodex urlCodex) {
    this.placeProvider = placeProvider;
    this.urlCodex = urlCodex;
  }

  @Override
  public T getPlace(final String tokenStr) {
    String[] split = tokenStr.split("\\?", 2);
    String path = split[0];
    String queryString = split.length > 1 ? split[1] : null;

    T place = placeProvider.get();
    place.setPath(path);

    if (queryString != null) {
      for (String kvPair : queryString.split("&")) {
        String[] kv = kvPair.split("=", 2);
        if (kv[0].length() == 0) {
          continue;
        }
        String key = urlCodex.decodeQueryString(kv[0]);
        String value = kv.length > 1 ? urlCodex.decodeQueryString(kv[1]) : "";
        String[] values = place.getParams().get(key);
        if (values == null) {
          place.setParameter(key, value);
        } else {
          // String[] newValues = Arrays.copyOf(values, values.length + 1);
          String[] newValues = new String[values.length + 1];
          System.arraycopy(values, 0, newValues, 0, values.length);
          newValues[newValues.length - 1] = value;
          place.setParameter(key, newValues);
        }
      }
    }

    return place;
  }

  @Override
  public String getToken(final BasePlace place) {
    String path = place.getPath();

    StringBuilder token = new StringBuilder(path == null ? "" : path);
    Map<String, String[]> params = place.getParams();
    char prefix = '?';
    for (Map.Entry<String, String[]> entry : params.entrySet()) {
      for (String val : entry.getValue()) {
        token.append(prefix).append(urlCodex.encodeQueryString(entry.getKey())).append('=');
        if (val != null) {
          token.append(urlCodex.encodeQueryString(val));
        }
        prefix = '&';
      }
    }

    return token.toString();
  }

}