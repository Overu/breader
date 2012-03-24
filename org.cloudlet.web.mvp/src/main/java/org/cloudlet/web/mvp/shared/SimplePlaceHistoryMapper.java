package org.cloudlet.web.mvp.shared;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public final class SimplePlaceHistoryMapper extends AbstractPlaceHistoryMapper<Void> {

  private final Logger logger = Logger.getLogger(getClass().getName());

  private final Map<String, PlaceTokenizer<?>> placeToTokenizer = new HashMap<String, PlaceTokenizer<?>>();
  private final Map<String, String> placeToPrefix = new HashMap<String, String>();
  private final Map<String, PlaceTokenizer<?>> prefixToTokenizer = new HashMap<String, PlaceTokenizer<?>>();

  public <T extends Place> void addBinding(Class<T> place, PlaceTokenizer<T> tokenizer,
      String prefix) {
    String clazz = place.getName();
    if (null != placeToTokenizer.put(clazz, tokenizer)) {
      logger.warning("注册了重复的place = " + clazz);
    }
    placeToPrefix.put(clazz, prefix);
    if (null != prefixToTokenizer.put(prefix, tokenizer)) {
      logger.warning("注册了重复的prefix = " + prefix);
    }
    logger.finest("bind " + clazz + " with prefix \"" + prefix + "\"");
  }

  @Override
  protected PrefixAndToken getPrefixAndToken(Place newPlace) {
    String prefix = placeToPrefix.get(newPlace.getClass().getName());
    PlaceTokenizer<Place> placeTokenizer = (PlaceTokenizer<Place>) placeToTokenizer.get(newPlace.getClass().getName());
    return new PrefixAndToken(prefix, placeTokenizer.getToken(newPlace));
  }

  @Override
  protected PlaceTokenizer<?> getTokenizer(String prefix) {
    return prefixToTokenizer.get(prefix);
  }

}
