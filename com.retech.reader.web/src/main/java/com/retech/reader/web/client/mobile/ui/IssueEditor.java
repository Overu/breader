package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.ActivityAware;
import org.cloudlet.web.mvp.shared.ActivityState;

@Singleton
public class IssueEditor extends Composite implements ActivityAware {

  interface Binder extends UiBinder<Widget, IssueEditor> {

  }

  @UiField(provided = true)
  IssueListImage issueListImage;
  @UiField(provided = true)
  IssueNews issueNews;

  private static Binder binder = GWT.create(Binder.class);

  @Inject
  public IssueEditor(final IssueListImage issueListImage, final IssueNews issueNews) {

    this.issueListImage = issueListImage;
    this.issueNews = issueNews;
    initWidget(binder.createAndBindUi(this));
  }

  @Override
  public void onStart(final ActivityState state) {
    issueListImage.onStart(state);
    issueNews.onStart(state);
  }

}
