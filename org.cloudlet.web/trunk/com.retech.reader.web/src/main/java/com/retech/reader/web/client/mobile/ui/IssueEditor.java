package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.cloudlet.web.mvp.shared.ProvideActivities;

import java.util.Arrays;
import java.util.List;

@Singleton
public class IssueEditor extends Composite implements ProvideActivities {

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
  public List<Activity> provideActivities() {
    return Arrays.<Activity> asList(issueListImage, issueNews);
  }

}
