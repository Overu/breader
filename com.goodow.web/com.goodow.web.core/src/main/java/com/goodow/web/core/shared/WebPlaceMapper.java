package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class WebPlaceMapper implements PlaceHistoryMapper {

  @HomePlace
  @Inject
  WebPlace homePlace;

  @Inject
  PlaceController placeController;

  @Override
  public Place getPlace(final String token) {
    WebPlace place = homePlace.findChild(token);
    return place;
  }

  @Override
  public String getToken(final Place place) {
    WebPlace p = (WebPlace) place;
    return p.getUri();
  }

  public WebPlace getWhere() {
    return (WebPlace) placeController.getWhere();
  }

  public void goTo(final WebPlace place) {
    placeController.goTo(place);
  }

  public void gotoContent(final WebContent content) {
    goTo(content, EntryViewer.EDIT);
  }

  public void goTo(final WebContent content, final ViewType viewType) {
    String uri = content.getUri();
    WebPlace place = homePlace.findChild(uri);
    if (place != null) {
      place = place.getViewerPlace(viewType);
    }
    if (place != null) {
      placeController.goTo(place);
    }
  }

  public void gotoFeed(final WebContent content, final String feed) {

  }

  public void gotoFeed(final WebContent content, final String feed, final String presenterName) {

  }

}
