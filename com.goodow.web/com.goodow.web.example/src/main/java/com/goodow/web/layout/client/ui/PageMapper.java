package com.goodow.web.layout.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class PageMapper implements PlaceHistoryMapper {

  private final Provider<Presenter> provider;

  private Presenter root;

  @Inject
  public PageMapper(Provider<Presenter> presenter) {
    this.provider = presenter;
    root = provider.get();
    root.setPath("");
  }

  @Override
  public Place getPlace(String token) {
    String[] paths = token.split("/");
    Presenter result = root;
    for (String p : paths) {
      result = result.getChild(p);
    }
    return result;
  }

  public Presenter getRoot() {
    return root;
  }

  @Override
  public String getToken(Place place) {
    Presenter p = (Presenter) place;
    return p.getUri();
  }
}
