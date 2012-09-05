package com.goodow.web.reader.client;

import com.goodow.web.core.shared.FeedViewer;
import com.goodow.web.core.shared.WebPlace;

import java.util.List;

public class FeedViewerList extends PlaceList {

  @Override
  protected List<WebPlace> getPlaces() {
    List<WebPlace> result = place.getChildren();
    for (WebPlace child : place.getViewerPlaces()) {
      if (child.getViewType() instanceof FeedViewer) {
        result.add(child);
      }
    }
    return result;
  }

}
