package com.goodow.web.mvp.shared.layout;

import com.goodow.web.mvp.shared.BasePlace;

import com.google.inject.Inject;
import com.google.inject.Provider;


public final class PlaceBuilder {

  private final Provider<BasePlace> placeProvider;
  private BasePlace place;
  private static final String[] EMPTY = new String[] {""};

  @Inject
  PlaceBuilder(Provider<BasePlace> placeProvider) {
    this.placeProvider = placeProvider;
    place = placeProvider.get();
  }

  public BasePlace build() {
    try {
      return place;
    } finally {
      place = placeProvider.get();
    }
  }

  public PlaceBuilder excludeAll() {
    place.setParameter(Search.KEY, EMPTY);
    place.setParameter(Nav.KEY, EMPTY);
    place.setParameter(Footer.KEY, EMPTY);
    return this;
  }
}
