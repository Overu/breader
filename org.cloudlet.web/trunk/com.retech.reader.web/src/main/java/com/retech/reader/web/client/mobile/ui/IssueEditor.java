package com.retech.reader.web.client.mobile.ui;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IssueEditor extends Composite implements Activity {

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
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {

  }

  @Override
  public void onStop() {

  }

  @Override
  public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
    issueListImage.start(panel, eventBus);
    issueNews.start(panel, eventBus);
  }

}
