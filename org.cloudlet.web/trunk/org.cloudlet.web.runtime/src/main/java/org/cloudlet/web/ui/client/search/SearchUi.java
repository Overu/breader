package org.cloudlet.web.ui.client.search;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class SearchUi extends Composite implements ActivityAware {

  interface SearchUiBinder extends UiBinder<Widget, SearchUi> {
  }

  private static SearchUiBinder uiBinder = GWT.create(SearchUiBinder.class);

  @Inject
  SearchUi() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void onStart(ActivityState state) {

  }
}
