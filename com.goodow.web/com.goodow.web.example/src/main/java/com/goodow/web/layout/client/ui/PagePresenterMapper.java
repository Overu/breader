package com.goodow.web.layout.client.ui;

import com.goodow.web.layout.shared.Page;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


@Singleton
public class PagePresenterMapper implements PlaceHistoryMapper {

  private final Provider<Presenter> presenter;

  @Inject
  public PagePresenterMapper(final Provider<Presenter> presenter) {
    this.presenter = presenter;
  }

  @Override
  public Place getPlace(final String token) {
    Presenter p = presenter.get();
    // p.setUri(token);
    return p;
  }

  @Override
  public String getToken(final Place place) {
    Presenter p = (Presenter) place;
    Page page = p.getPage();
    return page.getUri();
  }

}
